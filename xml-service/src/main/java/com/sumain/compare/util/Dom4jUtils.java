package com.sumain.compare.util;


import com.sumain.compare.model.GeneralException;
import com.sumain.compare.model.XmlCompareReport;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Dom4jUtils {

    public static List<String> excludeXpath = new ArrayList<>(0);
    public static List<String> includeXpath = new ArrayList<>(0);

    public static List<XmlCompareReport> compare(String source,String target){
        Element sourceElement = Dom4jUtils.parseStr(source).getRootElement();
        Element targetElement = Dom4jUtils.parseStr(target).getRootElement();
        return compare(sourceElement,targetElement);
    }

    public static List<XmlCompareReport> compare(File source,File target){
        Element sourceElement = Dom4jUtils.parseFile(source).getRootElement();
        Element targetElement = Dom4jUtils.parseFile(target).getRootElement();
        return compare(sourceElement,targetElement);
    }


    public static List<XmlCompareReport> compare(Element source, Element target){
        ArrayList<XmlCompareReport> reports = new ArrayList<>();
        compare(source,target,reports);
        return reports;
    }


    public static void compare(Element source, Element target,List<XmlCompareReport> reports){
        // 先比较属性
        compareAttributes(source,target,reports);

        // 获取子节点
        List<Element> sourceChildren = source.elements();
        List<Element> targetChildren = target.elements();

        // 如果没有子节点，则比较文本值
        if (sourceChildren.isEmpty() && targetChildren.isEmpty()) {
            String sourceText = source.getTextTrim();
            String targetText = target.getTextTrim();
            if (!Objects.equals(sourceText, targetText)) {
                reports.add(new XmlCompareReport(ComparisonType.NODE_VALUE.name(), "节点文本不一致", source.getPath(), sourceText, targetText
                ));
            }
            return;
        }

        // 按子节点名字分组
        Map<String, List<Element>> sourceGrouped = sourceChildren.stream()
                .collect(Collectors.groupingBy(Element::getName));
        Map<String, List<Element>> targetGrouped = targetChildren.stream()
                .collect(Collectors.groupingBy(Element::getName));

        // 遍历 source 的子节点组
        for (Map.Entry<String, List<Element>> entry : sourceGrouped.entrySet()) {
            String name = entry.getKey();
            List<Element> sourceList = entry.getValue();
            List<Element> targetList = targetGrouped.getOrDefault(name, Collections.emptyList());

            if (targetList.isEmpty()) {
                // target 没有对应节点
                for (Element s : sourceList) {
                    reports.add(new XmlCompareReport(ComparisonType.NODE.name(), "target对应节点无此子节点", s.getPath(), s.asXML(), null));
                }
            } else {
                // 数量不同:数量不同比较没有意义
                if (sourceList.size() != targetList.size()) {
                    reports.add(new XmlCompareReport(
                            ComparisonType.NODE.name(),
                            "同名子节点数量不一致",
                            source.getPath() + "/" + name,
                            String.valueOf(sourceList.size()),
                            String.valueOf(targetList.size())
                    ));
                } else {
                    for (int i = 0; i < sourceList.size(); i++) {
                        Element sourceElement = sourceList.get(i);
                        // 处理id不为null的
                        if(sourceElement.attribute("id") != null){
                            String idValue = sourceElement.attribute("id").getValue();
                            Element targetElement = targetList.stream()
                                    .filter((targetElementTemp) -> targetElementTemp.attribute("id") != null && idValue.equals(targetElementTemp.attribute("id").getValue()))
                                    .findFirst().orElse(null);
                            if(targetElement != null){
                                compare(sourceElement,targetElement,reports);
                                continue;
                            }
                        }

                        // todo 处理href属性

                        // 没有同名id为null直接按照顺序比，必须按照顺序，否则比对没有意义
                        compare(sourceElement,targetList.get(i),reports);
                    }
                }
            }
        }

        // 遍历 target 的子节点组（找出 source 没有的）
        for (Map.Entry<String, List<Element>> entry : targetGrouped.entrySet()) {
            String name = entry.getKey();
            if (!sourceGrouped.containsKey(name)) {
                for (Element t : entry.getValue()) {
                    reports.add(new XmlCompareReport(
                            ComparisonType.NODE.name(),
                            "source对应节点无此子节点",
                            t.getPath(),
                            null,
                            t.asXML()
                    ));
                }
            }
        }

    }

    public static void compareAttributes(Element source, Element target,List<XmlCompareReport> reports){
        List<Attribute> sourceAttributes = source.attributes();
        List<Attribute> targetAttributes = target.attributes();

        int sourceSize = sourceAttributes.size();
        int targetSize = targetAttributes.size();

        Set<String> sourceNames = sourceAttributes.stream().map(Attribute::getName).collect(Collectors.toSet());
        Set<String> targetNames = targetAttributes.stream().map(Attribute::getName).collect(Collectors.toSet());
        Set<String> comparedNames = new HashSet<>();

        for (int i = 0; i < Math.max(sourceAttributes.size(), targetAttributes.size()); i++) {
            if(i < sourceSize){
                Attribute sourceAtt = sourceAttributes.get(i);
                String attributesName = sourceAtt.getName();
                if(!comparedNames.contains(attributesName) && targetNames.contains(attributesName)){
                    Attribute targetAtt = target.attribute(attributesName);
                    if(!targetAtt.getValue().equals(sourceAtt.getValue())){
                        reports.add(new XmlCompareReport(ComparisonType.ATTRIBUTE_VALUE.name(),"属性值不一致",sourceAtt.getPath(),sourceAtt.getValue(),targetAtt.getValue()));
                    }
                    comparedNames.add(attributesName);
                }else if(!comparedNames.contains(attributesName) && !targetNames.contains(attributesName)){
                    reports.add(new XmlCompareReport(ComparisonType.ATTRIBUTE.name(),"target对应节点无此属性",sourceAtt.getPath(),attributesName,""));
                    comparedNames.add(attributesName);
                }
            }

            if(i < targetSize){
                Attribute targetAtt = targetAttributes.get(i);
                String attributesName = targetAtt.getName();
                if(!comparedNames.contains(attributesName) && sourceNames.contains(attributesName)){
                    Attribute sourceAtt = source.attribute(attributesName);
                    if(!targetAtt.getValue().equals(sourceAtt.getValue())){
                        reports.add(new XmlCompareReport(ComparisonType.ATTRIBUTE_VALUE.name(),"属性值不一致",targetAtt.getPath(),sourceAtt.getValue(),targetAtt.getValue()));
                    }
                    comparedNames.add(attributesName);
                }else if(!comparedNames.contains(attributesName) && !sourceNames.contains(attributesName)){
                    reports.add(new XmlCompareReport(ComparisonType.ATTRIBUTE.name(),"source对应节点无此属性",targetAtt.getPath(),"",attributesName));
                    comparedNames.add(attributesName);
                }
            }
        }
    }


    public static Document parseFile(File file) {
        SAXReader reader = new SAXReader();
        try {
            return reader.read(file);
        } catch (DocumentException e) {
            log.error("解析xml文件异常",e);
            throw new GeneralException("解析xml文件异常");
        }
    }

    public static Document parseStr(String xml){
        try {
            return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("解析xml字符串异常",e);
            throw new RuntimeException(e);
        }
    }

    enum ComparisonType{
        ATTRIBUTE,
        ATTRIBUTE_VALUE,
        NODE_VALUE,
        NODE
    }



}
