package com.project.app.utils;

import android.util.Log;

import com.project.app.bean.Person;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * sax解析器的demo
 */
public class SaxParseHelper extends DefaultHandler {
    private Person person;
    private ArrayList<Person> persons;

    //当前解析的元素标签
    private String tagName = null;

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    //当读取到文档开始标志是触发，通常在这里完成一些初始化操作
    @Override
    public void startDocument() {
        this.persons  = new ArrayList<>();
        Log.i("SAX", "读取到文档头,开始解析xml");
    }

    /**
     * 读到一个开始标签时调用,第二个参数为标签名,最后一个参数为属性数组
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(localName.equals("person")){
            person = new Person();
            person.setId(Integer.parseInt(attributes.getValue("id")));
        }
        Log.i("SAX", "开始处理person元素~" + localName );
        this.tagName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if(tagName != null){
            String data = new String(ch,start,length);
            //读取标签中的内容
            if(this.tagName.equals("name")){
                this.person.setName(data);
                Log.i("SAX", "处理name元素内容");
            }else if(this.tagName.equals("age")){
                this.person.setAge(data);
                Log.i("SAX","处理age元素");
            }
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if(localName.equals("person")){
            this.persons.add(person);
            this.person = null;
            Log.i("SAX", "处理person元素结束~");
        }
        this.tagName = null;
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.i("SAX", "读取到文档尾,xml解析结束");
    }
}
