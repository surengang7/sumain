package com.sumain.compare.util;

import com.csvreader.CsvWriter;
import com.sumain.compare.model.GeneralException;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


@Slf4j
public class FileUtil {



    /**
     * 递归读取目录下所有文件（包括子目录）
     * @param dir 根目录路径
     * @return key: 文件名，value: File 对象
     */
    public static Map<String, File> readDir(String dir) {
        Map<String, File> fileMap = new HashMap<>();
        File rootDir = new File(dir);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            return fileMap; // 目录不存在或不是目录
        }
        scanDirectory(rootDir, fileMap);
        return fileMap;
    }

    /**
     * 递归扫描目录
     * @param current 当前目录
     * @param fileMap 保存结果
     */
    private static void scanDirectory(File current, Map<String, File> fileMap) {
        File[] files = current.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isFile()) {
                fileMap.put(f.getName(), f); // 以文件名作为 key
            } else if (f.isDirectory()) {
                scanDirectory(f, fileMap); // 递归子目录
            }
        }
    }






    /**
     * 移动文件到目标目录，如果目标文件存在则覆盖
     */
    public static void moveFile(File sourceFile, File targetFile) throws IOException {
        if (!sourceFile.exists()) {
            throw new IOException("源文件不存在: " + sourceFile.getAbsolutePath());
        }

        // 确保目标目录存在
        File parentDir = targetFile.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("创建目标目录失败: " + parentDir.getAbsolutePath());
            }
        }

        Path sourcePath = sourceFile.toPath();
        Path targetPath = targetFile.toPath();

        try {
            // 使用 REPLACE_EXISTING 覆盖已有文件
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            // 如果不支持原子移动，则退而使用非原子方式
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * 移动文件到目标目录，保留原文件名
     */
    public static void moveFileToDir(File sourceFile, String targetDir) {
        if (!sourceFile.exists()) {
            log.error("原文件不存在:");
            throw new GeneralException("源文件不存在: " + sourceFile.getAbsolutePath());
        }

        File targetFile = new File(targetDir, sourceFile.getName());
        try {
            moveFile(sourceFile, targetFile);
        }catch (IOException e){
            log.error("移动文件异常:",e);
            throw new GeneralException("移动文件异常");
        }

    }



    public static void mkdir(String path) {
        File file = new File(path);
        String parent = file.getParent();
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            boolean mkdirYn = parentFile.mkdirs();
            if(!mkdirYn){
                throw new RuntimeException("目录创建失败");
            }
        }
    }




    public static <T> void writeObjectsToCsv(List<T> list, String filePath){
        log.info("write csv file: {}",filePath);
        mkdir(filePath);
        if (list == null || list.isEmpty()) {
            return;
        }
        try(OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(Paths.get(filePath)), StandardCharsets.UTF_8)) {
            osw.write('\uFEFF');
            osw.flush(); // 确保 BOM 写入
            CsvWriter csvWriter = new CsvWriter(osw, ',');
            Class<?> clazz = list.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();

            // 写表头
            String[] header = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                header[i] = fields[i].getName();
            }
            csvWriter.writeRecord(header);

            // 写每行数据
            for (T obj : list) {
                String[] line = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(obj);
                    line[i] = value == null ? "" : value.toString();
                }
                csvWriter.writeRecord(line);
            }

            csvWriter.close();
        } catch (Exception e) {
            log.error("",e);
            throw new RuntimeException(e);
        }
    }
}
