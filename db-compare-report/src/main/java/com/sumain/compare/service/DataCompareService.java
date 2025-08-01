package com.sumain.compare.service;

import com.sumain.compare.util.ExcelWriter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DataCompareService {

    @Resource
    private DataSource sourceDataSource;

    @Resource
    private DataSource targetDataSource;


    public void compareData(String tableName, List<String> keyFields, List<String> compareFields,int pageSize) throws SQLException, IOException {
        Object lastKey = null;
        List<Map<String, Object>> allDiffs = new ArrayList<>();

        while (true) {
            List<Map<String, Object>> sourceRows = fetchSourceRows(tableName, keyFields, compareFields, lastKey, pageSize);
            if (sourceRows.isEmpty()) break;

            List<List<Object>> keyValuesList = extractKeys(sourceRows, keyFields);
            Map<String, Map<String, Object>> targetRows = fetchTargetByKeys(tableName, keyFields, compareFields, keyValuesList);

            for (Map<String, Object> sourceRow : sourceRows) {
                List<Object> keyValues = keyFields.stream().map(sourceRow::get).collect(Collectors.toList());
                String key = joinKey(keyValues);

                Map<String, Object> targetRow = targetRows.get(key);
                if (targetRow == null) {
                    Map<String, Object> diff = new LinkedHashMap<>();
                    for (int i = 0; i < keyFields.size(); i++) {
                        diff.put(keyFields.get(i), keyValues.get(i));
                    }
                    for (String col : compareFields) {
                        diff.put(col + "_SOURCE", sourceRow.get(col));
                        diff.put(col + "_TARGET", "<MISSING>");
                    }
                    allDiffs.add(diff);
                } else {
                    boolean hasDiff = false;
                    Map<String, Object> diff = new LinkedHashMap<>();
                    for (int i = 0; i < keyFields.size(); i++) {
                        diff.put(keyFields.get(i), keyValues.get(i));
                    }
                    for (String col : compareFields) {
                        Object sv = sourceRow.get(col);
                        Object tv = targetRow.get(col);
                        if (!Objects.equals(sv, tv)) {
                            diff.put(col + "_SOURCE", sv);
                            diff.put(col + "_TARGET", tv);
                            hasDiff = true;
                        }
                    }
                    if (hasDiff) allDiffs.add(diff);
                }
            }

            lastKey = sourceRows.get(sourceRows.size() - 1).get(keyFields.get(0));
        }

        String filePath = "diff-report-" + tableName + ".xlsx";
        ExcelWriter.writeDiffToExcel(filePath, keyFields, compareFields, allDiffs);
        System.out.println("✅ 差异报告已导出到: " + filePath);
    }

    private List<Map<String, Object>> fetchSourceRows(String tableName, List<String> keyFields, List<String> fields,
                                                      Object lastKey, int limit) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        String orderKey = keyFields.get(0); // 暂支持单字段排序主键

        String fieldList = String.join(", ", keyFields) + ", " + String.join(", ", fields);
        String sql = String.format("SELECT %s FROM %s WHERE %s > ? ORDER BY %s FETCH NEXT %d ROWS ONLY",
                fieldList, tableName, orderKey, orderKey, limit);

        try (Connection conn = sourceDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, lastKey != null ? lastKey : getMinKey(conn, tableName, orderKey));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (String col : keyFields) row.put(col, rs.getObject(col));
                    for (String col : fields) row.put(col, rs.getObject(col));
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    private Map<String, Map<String, Object>> fetchTargetByKeys(String tableName, List<String> keyFields, List<String> fields,
                                                               List<List<Object>> keyValuesList) throws SQLException {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        if (keyValuesList.isEmpty()) return result;

        String selectCols = String.join(", ", keyFields) + ", " + String.join(", ", fields);
        String whereClause = keyValuesList.stream().map(kv ->
                        "(" + keyFields.stream().map(k -> k + "=?").collect(Collectors.joining(" AND ")) + ")")
                .collect(Collectors.joining(" OR "));

        String sql = String.format("SELECT %s FROM %s WHERE %s", selectCols, tableName, whereClause);

        try (Connection conn = targetDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            for (List<Object> keyVals : keyValuesList) {
                for (Object val : keyVals) {
                    ps.setObject(paramIndex++, val);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    List<Object> keyVals = new ArrayList<>();
                    for (String k : keyFields) keyVals.add(rs.getObject(k));
                    String key = joinKey(keyVals);

                    Map<String, Object> row = new LinkedHashMap<>();
                    for (String col : fields) row.put(col, rs.getObject(col));
                    result.put(key, row);
                }
            }
        }
        return result;
    }

    private List<List<Object>> extractKeys(List<Map<String, Object>> rows, List<String> keyFields) {
        List<List<Object>> keys = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            List<Object> key = keyFields.stream().map(row::get).collect(Collectors.toList());
            keys.add(key);
        }
        return keys;
    }

    private String joinKey(List<Object> keyParts) {
        return keyParts.stream().map(String::valueOf).collect(Collectors.joining("|"));
    }

    private Object getMinKey(Connection conn, String tableName, String keyColumn) throws SQLException {
        String sql = String.format("SELECT MIN(%s) FROM %s", keyColumn, tableName);
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getObject(1) : null;
        }
    }
}
