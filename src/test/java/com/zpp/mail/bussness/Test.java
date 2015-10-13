package com.zpp.mail.bussness;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.bussness.bean.EventBean;
import com.zpp.mail.bussness.constants.InitConstants;
import com.zpp.mail.bussness.handler.Handler1;
import com.zpp.mail.bussness.handler.Handler2;
import com.zpp.mail.bussness.handler.Handler3;
import com.zpp.mail.utils.InitPropertiesUtil;

/**
 * 入口程序
 * @author weiqz
 *
 */
public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

//  static ScheduledThreadPoolExecutor step1 = null;
  static ScheduledThreadPoolExecutor step2 = null;
  static ScheduledThreadPoolExecutor step3 = null;
  public static int loopCount = 1;

  /**
   * 流程概述：
   * 0.初始化，加载配置文件
   * 1.处理1：启动线程A，用于填写各个业务的邮箱
   * 2.处理2：启动线程B，用户监视邮箱是否有新的邮件，并获取邮箱中的URL放入注册List
   * 3.处理3：启动线程C，用于监视注册List，当list中有数据时，发送请求，完成注册
   * 
   * 
   * @param args
   */
  public static void main(String[] args) {
      InitPropertiesUtil.getInstance(InitConstants.INIT_FILE_PATH);
      Test.loopCount = Integer.parseInt(InitPropertiesUtil.getInitProperty("main.thread.loop.max.count"));
      
      ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

      // 定时任务
//      step1 = new ScheduledThreadPoolExecutor(3);
      step2 = new ScheduledThreadPoolExecutor(1);
      step3 = new ScheduledThreadPoolExecutor(10);

      // 0.初始化，加载配置文件
      // TODO 判断是否到运行时间
      // 自动获取多个页面多个的eventId,eventType
      List<EventBean> eventList = new ArrayList<EventBean>();
      parseHtml(InitPropertiesUtil.getInitProperty("test.businessTestD.url"), eventList);

//    boolean flag = true; 
//    if (flag) {
//        return;
//    }
      if (eventList.size() == 0) {
          log.info("抱歉，网站尚未开放预定，请稍后再试。");
          return;
      }
      // 1.处理1：启动线程A，用于填写各个业务的邮箱
      String eventId = null;
      String eventType = null;

      for(EventBean bean:eventList) {
          // 根据event对象数目，启动N个线程
          eventId = bean.getEventId();
          eventType = bean.getEventType();
          T1 t1 = new T1(eventId, eventType);
          Thread t = new Thread(t1);
          t.start();
      }
//      boolean flag = false; 
//      if (flag) {
//          return;
//      }

      // 2.处理2：启动线程B，用户监视邮箱是否有新的邮件，并获取邮箱中的URL放入注册List
      AtomicInteger handler2Count = new AtomicInteger(0);
      Handler2 handler2 = new Handler2(queue, handler2Count, eventList.size());
      step2.scheduleWithFixedDelay(handler2, 0, 2, TimeUnit.SECONDS);

//      boolean flag = true; 
//      if (flag) {
//          return;
//      }

      // 3.处理3：启动线程C，用于监视注册queue，当queue中有数据时，发送请求，完成注册
      while (true) {
          if (!queue.isEmpty()) {
              step3.scheduleWithFixedDelay(new Handler3(queue), 0, 10, TimeUnit.SECONDS);
              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
//              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
//              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
//              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
//              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
//              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
//              step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
          }

          if (handler2Count.get() >= eventList.size()) {
              step2.shutdown();
              if (step2.isShutdown()) {
                  log.info("线程池step2 停止......................... ");
                  break;
              }
          } else {
              log.info("未处理邮件数目: " + queue.size() + "，已发送邮件数：" + handler2Count.get() + "，需发送邮件数：" + eventList.size());
          }
          try {
              Thread.currentThread().sleep(5000);
          } catch (InterruptedException e) {
              log.error(e.getMessage(), e);
          }
      }
  }

  /*
   * 解析URL 找出eventId 和 eventType
   */
  public static void parseHtml(String url, List<EventBean> list) {
      Document doc = new Document(url);
      Connection conn = Jsoup.connect(url).timeout(3000); // 默认值是3000
      conn.header("User-Agent", InitConstants.REQUEST_AGENT);
      conn.header("Accept", InitConstants.REQUEST_ACCEPT);
      conn.header("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
      conn.header("Accept-Language", InitConstants.REQUEST_Accept_Language);
      conn.header("Cache-Control", InitConstants.REQUEST_Cache_Control);
      conn.header("Connection", InitConstants.REQUEST_CONNECTION);
      conn.header("Host", InitConstants.REQUEST_HOST);
      conn.header("Pragma", InitConstants.REQUEST_PRAGMA);

      try {
          doc = conn.get();
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
              log.info(url+"::::"+submit+"::"+eventId+"::"+eventType+"::");

              bean = new EventBean();
              bean.setEventId(eventId);
              bean.setEventType(eventType);
              list.add(bean);
          }
      } catch (SocketTimeoutException e) {
          String newUrl = "";
          log.error("出错前" + url);
          log.error(e.getMessage(), e);
          if (url.contains("aksale.advs.jp")) {
              newUrl = url.replace("aksale.advs.jp", "210.136.104.101");
          } else if (url.contains("210.136.104.101")) {
              newUrl = url.replace("210.136.104.101", "210.136.104.102");
          } else if (url.contains("210.136.104.102")) {
              newUrl = url.replace("210.136.104.102", "210.136.104.103");
          } else if (url.contains("210.136.104.103")) {
              newUrl = url.replace("210.136.104.103", "210.136.104.104");
          } else if (url.contains("210.136.104.104")) {
              newUrl = url.replace("210.136.104.104", "aksale.advs.jp");
          } else {
              log.error("出了不可能发生的错^_^");
          }
          log.error("新URL:" + newUrl);
          parseHtml(newUrl, list);
      } catch (IOException e) {
          log.error("出错前" + url);
          log.error(e.getMessage(), e);
          parseHtml(url, list);
      }
  }

}

class Ttest1 implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(Ttest1.class);
    ScheduledThreadPoolExecutor step1 = new ScheduledThreadPoolExecutor(3);

    String eventId;
    String eventType;
    AtomicInteger count = new AtomicInteger(1);
    public Ttest1(String eventId, String eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
    }
    @Override
    public void run() {
        // 隔N秒发送一次请求
        step1.scheduleWithFixedDelay(new Handler1(eventId, eventType, count), 0, 10, TimeUnit.SECONDS);
//        step1.scheduleWithFixedDelay(new Handler1(eventId, eventType, count), 5, 10, TimeUnit.SECONDS);
        while (true) {
            if (count.get() > Test.loopCount) {
                step1.shutdown();
                if (step1.isShutdown()) {
                    log.info("eventId: "+ eventId + " 已经停止 ");
                    break;
                }
            }
            if (count.get() == 0) {
                step1.shutdown();
                if (step1.isShutdown()) {
                    log.info("eventId: "+ eventId + " 商品已被抢光。 ");
                    break;
                }
            }
        }
    }
}
