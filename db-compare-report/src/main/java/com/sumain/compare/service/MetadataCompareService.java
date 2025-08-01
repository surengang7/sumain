package com.sumain.compare.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


@Slf4j
@Service
public class MetadataCompareService {

    @Resource
    private DataSource sourceDataSource;

    @Resource
    private DataSource targetDataSource;


    public void compareTableStructure(String tableName) {
        Map<String, ColumnMeta> sourceCols = getTableColumns(sourceDataSource, tableName);
        Map<String, ColumnMeta> targetCols = getTableColumns(targetDataSource, tableName);

        Set<String> allCols = new TreeSet<>();
        allCols.addAll(sourceCols.keySet());
        allCols.addAll(targetCols.keySet());

        System.out.printf("%-30s %-25s %-25s\n", "字段名", "Source 类型", "Target 类型");
        System.out.println("----------------------------------------------------------------------------------");
        for (String col : allCols) {
            ColumnMeta s = sourceCols.get(col);
            ColumnMeta t = targetCols.get(col);
            if (!Objects.equals(s, t)) {
                System.out.printf("%-30s %-25s %-25s\n", col,
                        s != null ? s.toString() : "<缺失>",
                        t != null ? t.toString() : "<缺失>");
            }
        }
    }

    private Map<String, ColumnMeta> getTableColumns(DataSource dataSource, String tableName) {
        Map<String, ColumnMeta> columns = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            String schema = conn.getSchema();
            ResultSet rs = meta.getColumns(null, schema, tableName.toUpperCase(), null);
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME").toUpperCase();
                String type = rs.getString("TYPE_NAME");
                int size = rs.getInt("COLUMN_SIZE");
                int scale = rs.getInt("DECIMAL_DIGITS");
                columns.put(name, new ColumnMeta(type, size, scale));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columns;
    }

    private static class ColumnMeta {
        String type;
        int size;
        int scale;

        ColumnMeta(String type, int size, int scale) {
            this.type = type;
            this.size = size;
            this.scale = scale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColumnMeta that = (ColumnMeta) o;
            return size == that.size && scale == that.scale && Objects.equals(type, that.type);
        }

        @Override
        public String toString() {
            return type + "(" + size + (scale > 0 ? "," + scale : "") + ")";
        }
    }
}
