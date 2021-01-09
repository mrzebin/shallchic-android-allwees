package com.project.app.utils;

import android.content.Context;

import com.hb.basemodel.utils.LoggerUtil;
import com.project.app.bean.Person;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DomParseHelper {

    public static List<Person> queryXml(Context context) throws ParserConfigurationException, IOException, SAXException {
        List<Person> personList = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc =  builder.parse(context.getAssets().open("index.html"));
        LoggerUtil.i("处理该文档的DomImplemention对象=" + doc.getImplementation());
        NodeList nodeList = doc.getElementsByTagName("person");
        for(int i=0;i<nodeList.getLength();i++){
            Element personElement = (Element) nodeList.item(i);
            Person p = new Person();
            Node childNode = nodeList.item(i);
            p.setId(Integer.parseInt(personElement.getAttribute("id")));

            if(childNode.getNodeType() == Node.ELEMENT_NODE){
                Element childElement = (Element) childNode;
                if("name".equals(childElement.getNodeName())){
                    p.setName(childElement.getFirstChild().getNodeValue());
                    LoggerUtil.i("解析name:" + childElement.getFirstChild().getNodeValue());
                }else if("age".equals(childElement.getNodeName())){
                    p.setAge(childElement.getFirstChild().getNodeValue());
                    LoggerUtil.i("解析age:" + childElement.getFirstChild().getNodeValue());
                }
            }
            personList.add(p);
        }
        return personList;
    }

}
