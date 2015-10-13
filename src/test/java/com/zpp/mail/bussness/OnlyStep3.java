package com.zpp.mail.bussness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.bussness.bean.EventBean;
import com.zpp.mail.bussness.handler.Handler1;
import com.zpp.mail.bussness.handler.Handler2;
import com.zpp.mail.bussness.handler.Handler3;
import com.zpp.mail.utils.InitPropertiesUtil;

/**
 * 入口程序
 * @author weiqz
 *
 */
public class OnlyStep3 {
    private static final Logger log = LoggerFactory.getLogger(OnlyStep3.class);

    static ScheduledThreadPoolExecutor step2 = null;
    static ScheduledThreadPoolExecutor step3 = null;
    

    /**
     * 流程概述：
     * 0.初始化，加载配置文件
     * 2.处理2：启动线程B，用户监视邮箱是否有新的邮件，并获取邮箱中的URL放入注册List
     * 3.处理3：启动线程C，用于监视注册List，当list中有数据时，发送请求，完成注册
     * 
     * 
     * @param args
     */
    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

        // 定时任务
        step2 = new ScheduledThreadPoolExecutor(1);
        step3 = new ScheduledThreadPoolExecutor(10);


        // 2.处理2：启动线程B，用户监视邮箱是否有新的邮件，并获取邮箱中的URL放入注册List
        AtomicInteger handler2Count = new AtomicInteger(0);
        Handler2 handler2 = new Handler2(queue, handler2Count, 9);
        step2.scheduleWithFixedDelay(handler2, 0, 2, TimeUnit.SECONDS);


        // 3.处理3：启动线程C，用于监视注册queue，当queue中有数据时，发送请求，完成注册
        while (true) {
            if (!queue.isEmpty()) {
                step3.scheduleWithFixedDelay(new Handler3(queue), 0, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
                step3.scheduleWithFixedDelay(new Handler3(queue), 5, 10, TimeUnit.SECONDS);
            }
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
