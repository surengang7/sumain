package com.sunline.compare.util;

import com.aggrepoint.utils.JsonUtils;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
public class FileUtil {



    public static String readFile(File file) {
        if (!file.exists()) {
            return null;
        }
        log.info("Read file :" + file.getName());
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder str = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                str.append(temp);
            }
            return str.toString();
        } catch (FileNotFoundException e) {
            log.error(file.getName() + " is not found",e);
            return null;
        } catch (IOException e) {
            log.error("",e);
            return null;
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (IOException e) {
                log.error("",e);
            }
        }
    }



    public static boolean mv(File sourceFile, File toFile) {
        boolean b = sourceFile.renameTo(toFile);
        if (b) {
            log.info(sourceFile + " move to " + toFile + " successfully");
        } else {
            log.error(sourceFile + " move to " + toFile + " failed");
        }
        return b;
    }



    public static void mkdir(String path) {
        File file = new File(path);
        String parent = file.getParent();
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            @SuppressWarnings("unused")
            boolean mkdirYn = parentFile.mkdirs();
        }
    }



    public static List<String[]> parseCsvToArrays(String sourceFilePath) {
        log.info("Parse csv file to List<Arrays<String>>: " + sourceFilePath);
        List<String[]> csvFileList = new ArrayList<String[]>();
        try {
            CsvReader reader = new CsvReader(sourceFilePath, ',', StandardCharsets.UTF_8);
            while (reader.readRecord()) {
                csvFileList.add(reader.getValues());
            }
            reader.close();
        } catch (IOException e) {
            log.error("",e);
        }
        return csvFileList;
    }




    public static void writeCsv(String targetFilePath, List<String[]> dataList) {
        log.info("Write csv file: " + targetFilePath);
        mkdir(targetFilePath);
        try {
            CsvWriter csvWriter = new CsvWriter(targetFilePath, ',', StandardCharsets.UTF_8);
            csvWriter.writeRecord(dataList.get(0));
            dataList.remove(0);
            for (String[] strArray : dataList) {
                csvWriter.writeRecord(strArray);
            }
            csvWriter.close();
        } catch (IOException e) {
            log.error("",e);
        }
    }

    public static void writeCsvFile(List<List<String>> dataList, String fileName, String filePath) {
        if(dataList.size() <= 1){
            return;
        }
        String fileNamePath = filePath+"/"+fileName;
        log.info("Write csv file: " + fileNamePath);
        mkdir(fileNamePath);
        try {
            CsvWriter csvWriter = new CsvWriter(fileNamePath, ',', StandardCharsets.UTF_8);
            //注意dataList里面是否包含csv文件头
            for (List<String> data : dataList) {
                csvWriter.writeRecord(data.toArray(new String[0]));
            }
            csvWriter.close();
        } catch (IOException e) {
            log.error("",e);
        }
    }



    public static String csvToJson(String filePath) throws IOException {
        return JsonUtils.toJSON(parseCsvToMap(filePath));
    }


    public static List<List<String>> parseCsvToList(String sourceFilePath) {
        log.info("Parse csv file: " + sourceFilePath);
        List<List<String>> csvFileList = new ArrayList<>();
        try {
            CsvReader reader = new CsvReader(sourceFilePath, ',', StandardCharsets.UTF_8);
            reader.readRecord();
            String[] header = reader.getValues();
            while (reader.readRecord()) {
                csvFileList.add(Arrays.asList(reader.getValues()));
            }
            reader.close();
        } catch (IOException e) {
            log.error("",e);
        }
        return csvFileList;
    }


    public static List<Map<String, String>> parseCsvToMap(String sourceFilePath) {
        log.info("Parse csv file: " + sourceFilePath);
        List<Map<String, String>> csvFileList = new ArrayList<>();
        try {
            CsvReader reader = new CsvReader(sourceFilePath, ',', StandardCharsets.UTF_8);
            reader.readRecord();
            String[] header = reader.getValues();
            while (reader.readRecord()) {
                String[] values = reader.getValues();
                HashMap<String, String> map = new LinkedHashMap<>();
                for (int i = 0; i < Integer.min(header.length,values.length); i++) {
                    map.put(header[i], values[i]);
                }
                csvFileList.add(map);
            }
            reader.close();
        } catch (IOException e) {
            log.error("",e);
        }
        return csvFileList;
    }


    public static String csvToJson(Map<String, String> nodeMap, String filePath) throws IOException {
        Map<String, List<Map<String, String>>> dataMap = new HashMap<>();
        nodeMap.forEach((nodeName, fileName) -> {
            if (!new File(filePath + fileName).exists()) {
                return;
            }
            List<Map<String, String>> mapList = parseCsvToMap(filePath + fileName);
            dataMap.put(nodeName,mapList);
        });
        return JsonUtils.toJSON(dataMap);
    }


}
