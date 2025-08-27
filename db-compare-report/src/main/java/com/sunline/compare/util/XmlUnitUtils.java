package com.sunline.compare.util;

import com.sunline.compare.model.XmlCompareReport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUnitUtils {

    private static final Map<ComparisonType, String> COMPARISON_TYPE_MAPPER = new HashMap<>();

    static {
        // 文档/声明
        COMPARISON_TYPE_MAPPER.put(ComparisonType.XML_VERSION, "XML 文档版本不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.XML_STANDALONE, "XML standalone 属性不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.XML_ENCODING, "XML 编码不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.HAS_DOCTYPE_DECLARATION, "是否有 DOCTYPE 声明不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.DOCTYPE_NAME, "DOCTYPE 名称不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.DOCTYPE_PUBLIC_ID, "DOCTYPE publicId 不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.DOCTYPE_SYSTEM_ID, "DOCTYPE systemId 不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.SCHEMA_LOCATION, "xsi:schemaLocation 不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.NO_NAMESPACE_SCHEMA_LOCATION, "xsi:noNamespaceSchemaLocation 不同");

        // 节点类型 / 命名空间
        COMPARISON_TYPE_MAPPER.put(ComparisonType.NODE_TYPE, "节点类型不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.NAMESPACE_PREFIX, "节点前缀不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.NAMESPACE_URI, "节点命名空间 URI 不同");

        // 文本 / 属性
        COMPARISON_TYPE_MAPPER.put(ComparisonType.TEXT_VALUE, "文本节点值不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.ATTR_VALUE_EXPLICITLY_SPECIFIED, "属性是否明确指定值不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.ELEMENT_NUM_ATTRIBUTES, "元素属性数量不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.ATTR_VALUE, "属性值不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.ATTR_NAME_LOOKUP, "属性名称不同");

        // 子节点 / 结构
        COMPARISON_TYPE_MAPPER.put(ComparisonType.CHILD_NODELIST_LENGTH, "子节点数量不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.CHILD_NODELIST_SEQUENCE, "子节点顺序不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.CHILD_LOOKUP, "子节点匹配失败");

        // 处理指令
        COMPARISON_TYPE_MAPPER.put(ComparisonType.PROCESSING_INSTRUCTION_TARGET, "处理指令目标不同");
        COMPARISON_TYPE_MAPPER.put(ComparisonType.PROCESSING_INSTRUCTION_DATA, "处理指令内容不同");

        // 元素名称
        COMPARISON_TYPE_MAPPER.put(ComparisonType.ELEMENT_TAG_NAME, "元素标签名不同");

    }

    public static List<XmlCompareReport> compare(Object sourceXml, Object targetXml) {
        List<XmlCompareReport> reports = new ArrayList<>();

        DiffBuilder builder = DiffBuilder.compare(sourceXml)
                .withTest(targetXml)
                .ignoreWhitespace()
                .ignoreComments();


        builder = builder.checkForSimilar()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes));


        Diff diff = builder.build();

        if (diff.hasDifferences()) {
            for (Difference difference : diff.getDifferences()) {
                Comparison comp = difference.getComparison();
                ComparisonType type = comp.getType();

                String xpath = comp.getControlDetails().getXPath();
                if (xpath == null) {
                    xpath = comp.getTestDetails().getXPath();
                }

                String controlValue = comp.getControlDetails().getValue() != null ?
                        comp.getControlDetails().getValue().toString() : "";
                String testValue = comp.getTestDetails().getValue() != null ?
                        comp.getTestDetails().getValue().toString() : "";

                // ====== 差异类型映射 ======
                if (type == ComparisonType.ATTR_VALUE) {
                    reports.add(new XmlCompareReport(
                            "ATTRIBUTE_VALUE",
                            "属性value不一致",
                            xpath,
                            controlValue,
                            testValue
                    ));
                } else if (type == ComparisonType.ATTR_NAME_LOOKUP) {
                    // 谁缺少属性？
                    if (controlValue != null && !controlValue.isEmpty() && (testValue == null || testValue.isEmpty())) {
                        reports.add(new XmlCompareReport(
                                "ATTRIBUTE",
                                "target对应节点无此属性",
                                xpath,
                                controlValue,
                                ""
                        ));
                    } else {
                        reports.add(new XmlCompareReport(
                                "ATTRIBUTE",
                                "source对应节点无此属性",
                                xpath,
                                "",
                                testValue
                        ));
                    }
                } else if (type == ComparisonType.CHILD_LOOKUP) {
                    // 子节点缺失
                    if (controlValue != null && !controlValue.isEmpty() && (testValue == null || testValue.isEmpty())) {
                        reports.add(new XmlCompareReport(
                                "NODE",
                                "target对应节点无此子节点",
                                xpath,
                                controlValue,
                                ""
                        ));
                    } else {
                        reports.add(new XmlCompareReport(
                                "NODE",
                                "source对应节点无此子节点",
                                xpath,
                                "",
                                testValue
                        ));
                    }
                } else if (type == ComparisonType.TEXT_VALUE) {
                    reports.add(new XmlCompareReport(
                            "TEXT_VALUE",
                            "节点文本不一致",
                            xpath,
                            controlValue,
                            testValue
                    ));
                }
            }
        }

        return reports;
    }


    /**
     * 比较两个 XML（字符串）
     */
    public static List<XmlCompareReport> compareXml(String sourceXml, String targetXml) {
        return compare(sourceXml, targetXml);
    }

    /**
     * 比较两个 XML（文件）
     */
    public static List<XmlCompareReport> compareXml(File sourceFile, File targetFile) {
        return compare(sourceFile, targetFile);
    }

    /**
     * 比较两个 XML（Element）
     */
    public static List<XmlCompareReport> compareXml(Element sourceElement, Element targetElement) {
        return compare(sourceElement, targetElement);
    }



    /**
     * 工具：从 XML 文件转 Element
     */
    public static Element parseFileToElement(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file).getDocumentElement();
        } catch (Exception e) {
            throw new RuntimeException("Parse XML File error: " + file.getAbsolutePath(), e);
        }
    }

    public static Element getElementByXPath(File xmlFile, String xpath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // 如果 XML 有命名空间需要 true
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        return getElement(xpath, doc);
    }

    public static Element getElementByXPath(String xmlContent, String xpath) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // 如果 XML 有命名空间需要 true
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

        return getElement(xpath, doc);
    }


    private static Element getElement(String xpath, Document doc) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node) xPath.evaluate(xpath, doc, XPathConstants.NODE);

        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
            return (Element) node;
        } else {
            return null;
        }
    }




}
