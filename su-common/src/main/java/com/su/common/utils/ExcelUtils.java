package com.su.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.su.common.exceptions.GeneralException;

import java.io.File;
import java.util.*;

public class ExcelUtils {


    /**
     * 读取 Excel，返回 List<Map<String, String>>
     * key   = 表头
     * value = 每一行对应的单元格值
     *
     * @param file      Excel 文件
     * @param hasHeader 是否有表头（true：第一行作为 key）
     */
    public static List<Map<String, String>> read(File file, boolean hasHeader) {
        if (file == null || !file.exists()) {
            throw new GeneralException("Excel file not found");
        }

        List<Map<String, String>> result = new ArrayList<>();
        // 用列索引保存表头：0->"exchange_code" 1->"exchange_name"...
        Map<Integer, String> headers = new LinkedHashMap<>();

        EasyExcel.read(file, new AnalysisEventListener<Map<Integer, String>>() {

                    @Override
                    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                        if (!hasHeader) {
                            return;
                        }
                        if (headMap == null || headMap.isEmpty()) {
                            return;
                        }
                        headers.clear();
                        for (Map.Entry<Integer, String> e : headMap.entrySet()) {
                            headers.put(e.getKey(), StringUtils.trim(e.getValue()));
                        }
                    }

                    @Override
                    public void invoke(Map<Integer, String> rowMap, AnalysisContext context) {
                        if (rowMap == null || rowMap.isEmpty()) {
                            return;
                        }

                        // 没有表头：第一次读到数据行时，按最大列号生成 COL_0/COL_1...
                        if (!hasHeader && headers.isEmpty()) {
                            int maxCol = rowMap.keySet().stream().max(Integer::compareTo).orElse(0);
                            for (int i = 0; i <= maxCol; i++) {
                                headers.put(i, "COL_" + i);
                            }
                        }

                        Map<String, String> line = new LinkedHashMap<>();
                        for (Map.Entry<Integer, String> e : headers.entrySet()) {
                            Integer col = e.getKey();
                            String key = e.getValue();
                            String value = StringUtils.trim(rowMap.get(col));
                            line.put(key, value);
                        }
                        result.add(line);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        // nothing
                    }
                })
                .autoCloseStream(true)
                .headRowNumber(hasHeader ? 1 : 0) // 关键：告诉 EasyExcel 表头行数
                .sheet()
                .doRead();

        return result;
    }


}
