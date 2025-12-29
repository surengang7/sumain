package com.su.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {


    /**
     * 读取 Excel，返回 Map<String, String>
     *
     * @param file      Excel 文件
     * @param hasHeader 是否有表头（true：跳过第一行）
     */
    public static Map<String, String> read(File file, boolean hasHeader) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Excel file not found");
        }

        Map<String, String> result = new HashMap<>();

        EasyExcel.read(file, new AnalysisEventListener<List<String>>() {

                    private boolean firstRowSkipped = false;

                    @Override
                    public void invoke(List<String> row, AnalysisContext context) {
                        // 跳过表头
                        if (hasHeader && !firstRowSkipped) {
                            firstRowSkipped = true;
                            return;
                        }

                        if (row == null || row.size() < 2) {
                            return;
                        }

                        String key = StringUtils.trim(row.get(0));
                        String value = StringUtils.trim(row.get(1));

                        if (key == null || key.isEmpty()) {
                            return;
                        }

                        result.put(key, value);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        // nothing
                    }
                })
                .autoCloseStream(true)
                .sheet()   // 默认第一个 sheet
                .doRead();

        return result;
    }


}
