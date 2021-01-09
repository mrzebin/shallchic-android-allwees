package com.project.app.utils;

import com.hb.basemodel.utils.LoggerUtil;
import com.project.app.bean.CityBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxCityHelper extends DefaultHandler {
    List<CityBean> citys;
    CityBean cityBean;
    private String tagName = "";

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        LoggerUtil.i("开始解析");
        this.citys = new ArrayList<>();
    }

    /**
     * 读到一个开始标签时调用,第二个参数为标签名,最后一个参数为属性数组
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        LoggerUtil.i ("开始处理dict元素~:" + localName);
        if (localName.equals("dict")) {
            cityBean = new CityBean();
            LoggerUtil.i("开始处理dict元素~");
        }
        this.tagName = localName;
    }

    /**
     * 读到到内容,第一个参数为字符串内容,后面依次为起始位置与长度
     */
    @Override
    public void characters(char[] ch, int start, int length){
        if (this.tagName != null) {
            String data = new String(ch, start, length);
            //读取标签中的内容
            if (this.tagName.equals("string")) {
                LoggerUtil.i("处理name元素内容:" + data);
            }
            LoggerUtil.i("处理name元素内容1:" + data);
        }
    }

    /**
     * 处理元素结束时触发,这里将对象添加到结合中
     */
    @Override
    public void endElement(String uri, String localName, String qName){
        if (localName.equals("dict")) {
            LoggerUtil.i("处理person元素结束~");
        }
        this.tagName = null;
    }

    /**
     * 读取到文档结尾时触发，
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        LoggerUtil.i("读取到文档尾,xml解析结束");
    }

    public List<CityBean> getCitys() {
        return citys;
    }

}
