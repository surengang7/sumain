package com.su.atlas.service.impl;

import com.su.atlas.entity.Exchange;
import com.su.atlas.entity.Security;
import com.su.atlas.enums.ExchangeEnum;
import com.su.atlas.exception.GeneralException;
import com.su.atlas.mapper.SecurityMapper;
import com.su.atlas.service.ExchangeService;
import com.su.atlas.service.FileService;
import com.su.atlas.service.SecurityService;
import com.su.common.utils.DateUtils;
import com.su.common.utils.ExcelUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService, FileService {

    @Resource
    private SecurityMapper baseMapper;

    @Resource
    private ExchangeService exchangeService;


    @Override
    public void handlerMultipartFile(MultipartFile... files) {
        if(files == null || files.length == 0) return;

        List<Security> securityList = new ArrayList<>();
        List<Exchange> allExchanges = exchangeService.findAll();
        Map<String, Exchange> exchangeMap = allExchanges.stream().collect(Collectors.toMap(Exchange::getExchangeCode, e -> e));

        for (MultipartFile file : files) {
            // 获取文件
            if(file == null){
                throw new GeneralException("上传的文件不能为空");
            }
            File securityExcel ;
            try{
                securityExcel = File.createTempFile("security", ".xls");
                file.transferTo(securityExcel);
            }catch (IOException exception){
                log.error("",exception);
                throw new GeneralException(exception.getMessage());
            }


            // 解析文件
            List<Map<String, String>> securityMapList = ExcelUtils.read(securityExcel, true);

            // 组装实体
            for (Map<String, String> securityMap : securityMapList) {
                Security security = new Security();
                security.setSecurityCode(securityMap.get("A股代码"));
                security.setMarket("CN_A");
                security.setExchangeId(exchangeMap.get(ExchangeEnum.SSE.getCode()).getExchangeId());
                security.setSecurityName(securityMap.get("证券简称"));
                security.setSecurityNameEn(securityMap.get("公司英文全称"));
                security.setSecurityType("STOCK");
                security.setCurrency("CNY");
                security.setTradeMinSize(100);
                security.setListingDate(DateUtils.toLocalDate(securityMap.get("上市日期"),"yyyyMMdd"));
                security.setSecurityStatus("SUSPENDED");
                securityList.add(security);
            }


            // 删除临时文件
            @SuppressWarnings("unused")
            boolean delete = securityExcel.delete();
        }

        saveBatch(securityList,true,1000);
    }

    @Override
    public void saveBatch(List<Security> securityList,Boolean updateYn,int batchSize) {
        int defaultBatchSize = 500;
        if(batchSize != 0){
            defaultBatchSize = batchSize;
        }

        for (int i = 0; i < securityList.size(); i += defaultBatchSize) {
            List<Security> batch = securityList.subList(i, Math.min(i + defaultBatchSize, securityList.size()));
            if(updateYn){
                baseMapper.batchUpsertByMarketCode(batch);
            }else {
                baseMapper.insert(batch);
            }
        }
    }
}
