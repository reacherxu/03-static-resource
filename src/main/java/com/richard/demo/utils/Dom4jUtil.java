package com.richard.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dom4jUtil {

    private final static Logger logger = LoggerFactory.getLogger(Dom4jUtil.class);

    /**
     * note: 如果要用Xpath,必须要引入jar包，否则会报错
     * 从xml文件中读取，而且标签中有冒号
     */
    // @SuppressWarnings("unchecked")
    // @Test
    // public void readWithXpath() {
    //
    // SAXReader reader = new SAXReader();
    // try {
    // Document document = reader.read(new File("resource/bpmn.xml"));
    //
    // Element process = (Element) document.selectSingleNode("//bpmn2:process");
    // System.out.println(process);
    //
    // List<Element> endEvents = process.selectNodes("./bpmn2:service");
    // for (Element element : endEvents) {
    // Node element2 = element.selectSingleNode("./bpmn2:incoming");
    // System.out.println(element2);
    //
    // }
    //
    // } catch (DocumentException e) {
    // logger.error(e.getMessage());
    // }
    // }

    /**
     * read from a text
     */
    // @SuppressWarnings("unchecked")
    // @Test
    // public void readContent() {
    // String text = "<bpmn2:definitions id='b2493550-171b-4b3d-8cb6-f8016a27742b'
    // xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    // xmlns:bpmn2='http://www.omg.org/spec/BPMN/20100524/MODEL'
    // xmlns:bpmndi='http://www.omg.org/spec/BPMN/20100524/DI'
    // xmlns:di='http://www.omg.org/spec/DD/20100524/DI'
    // xmlns:dc='http://www.omg.org/spec/DD/20100524/DC'
    // targetNamespace='http://www.sap.com/bpmn2/'><bpmn2:collaboration
    // id='collaboration-b2493550-171b-4b3d-8cb6-f8016a27742b'><bpmn2:participant
    // id='87d27052-cce2-4234-b5e5-36778a324b4e' name='Participant1'
    // processRef='process-87d27052-cce2-4234-b5e5-36778a324b4e'/></bpmn2:collaboration><bpmn2:process
    // id='process-87d27052-cce2-4234-b5e5-36778a324b4e'><bpmn2:laneSet
    // id='laneset-87d27052-cce2-4234-b5e5-36778a324b4e'><bpmn2:lane
    // id='aa2177af-0129-4b10-a7b3-615cf951c34f'
    // name='Lane1'><bpmn2:flowNodeRef>5c5f4557-c25f-47d4-8202-02939157a21f</bpmn2:flowNodeRef><bpmn2:flowNodeRef>eac4240a-102d-42bc-a992-8a33e4b3d5bb</bpmn2:flowNodeRef><bpmn2:flowNodeRef>c70b3eaf-0d20-4188-9ecd-35fc350a7cc3</bpmn2:flowNodeRef><bpmn2:flowNodeRef>ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb</bpmn2:flowNodeRef></bpmn2:lane></bpmn2:laneSet><bpmn2:service
    // id='5c5f4557-c25f-47d4-8202-02939157a21f' name='Service211'
    // pId='1c0c8ecd-50cd-4182-bb2f-171c28b78395'><bpmn2:incoming>db75ac29-8000-4ddb-b234-dabcce716e2d</bpmn2:incoming><bpmn2:outgoing>c20f6e09-6556-4c3a-b7d3-13487b68d362</bpmn2:outgoing></bpmn2:service><bpmn2:startEvent
    // id='eac4240a-102d-42bc-a992-8a33e4b3d5bb'
    // name='StartEvent1'><bpmn2:outgoing>db75ac29-8000-4ddb-b234-dabcce716e2d</bpmn2:outgoing></bpmn2:startEvent><bpmn2:sequenceFlow
    // id='db75ac29-8000-4ddb-b234-dabcce716e2d' name='SequenceFlow3'
    // sourceRef='eac4240a-102d-42bc-a992-8a33e4b3d5bb'
    // targetRef='5c5f4557-c25f-47d4-8202-02939157a21f' isImmediate='false'
    // TrueOrFalse=''></bpmn2:sequenceFlow><bpmn2:sequenceFlow
    // id='aed1f7f0-c998-453b-9801-5490241cf9bf' name='SequenceFlow1'
    // sourceRef='ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb'
    // targetRef='c70b3eaf-0d20-4188-9ecd-35fc350a7cc3'
    // isImmediate='false'></bpmn2:sequenceFlow><bpmn2:sequenceFlow
    // id='c20f6e09-6556-4c3a-b7d3-13487b68d362' name='SequenceFlow4'
    // sourceRef='5c5f4557-c25f-47d4-8202-02939157a21f'
    // targetRef='ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb' isImmediate='false'
    // TrueOrFalse=''></bpmn2:sequenceFlow><bpmn2:endEvent id='c70b3eaf-0d20-4188-9ecd-35fc350a7cc3'
    // name='EndEvent1'><bpmn2:incoming>aed1f7f0-c998-453b-9801-5490241cf9bf</bpmn2:incoming></bpmn2:endEvent><bpmn2:sequence
    // id='ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb' name='AFA1(1)'
    // pId='3aa51ee1-d0ce-4321-89f7-6589d6e94486'
    // sId='6eef0484-29fa-4daa-9a03-acda9d022ea9'><bpmn2:outgoing>aed1f7f0-c998-453b-9801-5490241cf9bf</bpmn2:outgoing><bpmn2:incoming>c20f6e09-6556-4c3a-b7d3-13487b68d362</bpmn2:incoming></bpmn2:sequence></bpmn2:process><bpmndi:BPMNDiagram
    // id='diagram-b2493550-171b-4b3d-8cb6-f8016a27742b' resolution='96.0'><bpmndi:BPMNPlane
    // bpmnElement='collaboration-b2493550-171b-4b3d-8cb6-f8016a27742b'><bpmndi:BPMNShape
    // id='symbol-87d27052-cce2-4234-b5e5-36778a324b4e'
    // bpmnElement='87d27052-cce2-4234-b5e5-36778a324b4e' isHorizontal='true'><dc:Bounds x='20'
    // y='20' width='620' height='120'/><bpmndi:BPMNLabel/></bpmndi:BPMNShape><bpmndi:BPMNShape
    // id='symbol-aa2177af-0129-4b10-a7b3-615cf951c34f'
    // bpmnElement='aa2177af-0129-4b10-a7b3-615cf951c34f' isHorizontal='true'><dc:Bounds x='20'
    // y='20' width='620' height='120'/><bpmndi:BPMNLabel/></bpmndi:BPMNShape><bpmndi:BPMNShape
    // id='symbol-5c5f4557-c25f-47d4-8202-02939157a21f'
    // bpmnElement='5c5f4557-c25f-47d4-8202-02939157a21f'><dc:Bounds x='164' y='47' width='100'
    // height='50'/><bpmndi:BPMNLabel/></bpmndi:BPMNShape><bpmndi:BPMNShape
    // id='symbol-eac4240a-102d-42bc-a992-8a33e4b3d5bb'
    // bpmnElement='eac4240a-102d-42bc-a992-8a33e4b3d5bb'><dc:Bounds x='92' y='57' width='40'
    // height='40'/><bpmndi:BPMNLabel/></bpmndi:BPMNShape><bpmndi:BPMNShape
    // id='symbol-c70b3eaf-0d20-4188-9ecd-35fc350a7cc3'
    // bpmnElement='c70b3eaf-0d20-4188-9ecd-35fc350a7cc3'><dc:Bounds x='460' y='57' width='40'
    // height='40'/><bpmndi:BPMNLabel/></bpmndi:BPMNShape><bpmndi:BPMNShape
    // id='symbol-ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb'
    // bpmnElement='ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb'><dc:Bounds x='319.25' y='47' width='100'
    // height='60'/><bpmndi:BPMNLabel/></bpmndi:BPMNShape><bpmndi:BPMNEdge
    // id='symbol-db75ac29-8000-4ddb-b234-dabcce716e2d'
    // bpmnElement='db75ac29-8000-4ddb-b234-dabcce716e2d'
    // sourceElement='symbol-eac4240a-102d-42bc-a992-8a33e4b3d5bb'
    // targetElement='symbol-5c5f4557-c25f-47d4-8202-02939157a21f'><di:waypoint x='131'
    // y='74.5'></di:waypoint><di:waypoint x='165'
    // y='74.5'></di:waypoint><bpmndi:BPMNLabel/></bpmndi:BPMNEdge><bpmndi:BPMNEdge
    // id='symbol-aed1f7f0-c998-453b-9801-5490241cf9bf'
    // bpmnElement='aed1f7f0-c998-453b-9801-5490241cf9bf'
    // sourceElement='symbol-ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb'
    // targetElement='symbol-c70b3eaf-0d20-4188-9ecd-35fc350a7cc3'><di:waypoint x='418.25'
    // y='77'></di:waypoint><di:waypoint x='461'
    // y='77'></di:waypoint><bpmndi:BPMNLabel/></bpmndi:BPMNEdge><bpmndi:BPMNEdge
    // id='symbol-c20f6e09-6556-4c3a-b7d3-13487b68d362'
    // bpmnElement='c20f6e09-6556-4c3a-b7d3-13487b68d362'
    // sourceElement='symbol-5c5f4557-c25f-47d4-8202-02939157a21f'
    // targetElement='symbol-ce10e8f5-0417-4754-ae8f-cbc0c5cf45bb'><di:waypoint x='263'
    // y='74.5'></di:waypoint><di:waypoint x='320.25'
    // y='74.5'></di:waypoint><bpmndi:BPMNLabel/></bpmndi:BPMNEdge></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></bpmn2:definitions>";
    // Document document;
    // try {
    // document = DocumentHelper.parseText(text);
    //
    // Node process = document.selectSingleNode("//bpmn2:process");
    // System.out.println(process);
    //
    // List<Element> endEvents = process.selectNodes("./bpmn2:service");
    // for (Element element : endEvents) {
    // Node element2 = element.selectSingleNode("./bpmn2:incoming");
    // System.out.println(element2);
    // }
    // } catch (DocumentException e) {
    // logger.error(e.getMessage());
    // }
    //
    // }

    @Test
    public void readNormalXml() {
        // 创建解析器,读入xml文档
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File("resource/book.xml"));

            // 获取文档的根节点
            Element root = document.getRootElement();

            // 将文档的XML转化为字符串
            String rootXmlText = root.asXML();
            System.out.println(rootXmlText);

            // 取得某个节点的子节点
            Element book = root.element("书");

            // 对某个节点进行遍历
            List<Element> books = root.elements("书");
            for (Iterator<Element> it = books.iterator(); it.hasNext();) {
                Element elem = it.next();

                Element bookName = elem.element("书名");
                System.out
                    .println(String.format("%s = %s\n", bookName.getName(), bookName.getText()));
                Element bookAuthor = elem.element("作者");
                System.out.println(
                    String.format("%s = %s\n", bookAuthor.getName(), bookAuthor.getText()));
                Element bookPrice = elem.element("售价");
                Attribute currency = bookPrice.attribute("币种");
                System.out.println(String.format("%s = %s currency = %s\n", bookPrice.getName(),
                    bookPrice.getText(), currency.getText()));
            }

            // 添加内容
            book.addElement("售价").setText("209元");

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8"); // 给格式化输出器指定一个码表，xml文档什么编码，格式化输出器就是什么编码
            XMLWriter writer = new XMLWriter(new FileOutputStream("resource/book.xml"), format);
            writer.write(document);
            writer.close();
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
}
