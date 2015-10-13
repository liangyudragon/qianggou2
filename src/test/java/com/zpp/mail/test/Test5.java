package com.zpp.mail.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zpp.mail.bussness.Main;
import com.zpp.mail.bussness.bean.EventBean;

/*
 * html 解析实验类
 */
public class Test5 {
    public static void parseHtml(String url, List<EventBean> list) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            // 获取当前页面所有[预约]按钮样式
            Elements ListDiv = doc.getElementsByAttributeValue("class","btnReserv");
            EventBean bean;
            for (Element element :ListDiv) {
                Element form = element.getElementsByTag("form").get(0);
                Element submitNode = (Element) form.childNode(1);
                Element eventIdNode = (Element) form.childNode(3);
                Element eventTypeNode = (Element) form.childNode(5);
                String submit = submitNode.attr("value");
                String eventId = eventIdNode.attr("value");
                String eventType = eventTypeNode.attr("value");
                System.out.println(submit+"::"+eventId+"::"+eventType+"::");
                
                bean = new EventBean();
                bean.setEventId(eventId);
                bean.setEventType(eventType);
                list.add(bean);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url ="http://localhost:7000/zpp/%E3%81%94%E6%B3%A8%E6%96%87%E4%BA%88%E7%B4%84%EF%BD%9C%E9%96%8B%E5%82%AC%E4%BA%88%E7%B4%84%E8%B2%A9%E5%A3%B2%E4%B8%80%E8%A6%A7.html";
//        Test5.parseHtml(url, new ArrayList<EventBean>());
        url = "http://aksale.advs.jp/cp/akachan_sale_pc/search_event_list.cgi?area2=%8d%e9%8b%ca%8c%a7&event_type=6&sid=37316&kmws=";
        new Main().parseHtml(url, new ArrayList<EventBean>());
    }
}
