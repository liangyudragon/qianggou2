package com.zpp.mail.test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test92 {
    private static final Logger log = LoggerFactory.getLogger(Test92.class);

    public static void main(String[] args) {
        AtomicInteger count = new AtomicInteger(0);

        ScheduledThreadPoolExecutor ste = new ScheduledThreadPoolExecutor(5);
        // 方法一
        Thread t = new Thread(new Test92().new T1(count));
        ste.scheduleWithFixedDelay(t, 0, 1, TimeUnit.SECONDS);
        ste.scheduleWithFixedDelay(t, 0, 1, TimeUnit.SECONDS);
        ste.scheduleWithFixedDelay(t, 0, 1, TimeUnit.SECONDS);
        ste.scheduleWithFixedDelay(t, 0, 1, TimeUnit.SECONDS);
        ste.scheduleWithFixedDelay(t, 0, 1, TimeUnit.SECONDS);
//        // 方法二
//        ste.scheduleWithFixedDelay(new Thread(new Test92().new T1(count)), 0, 1, TimeUnit.SECONDS);
//        ste.scheduleWithFixedDelay(new Thread(new Test92().new T1(count)), 0, 1, TimeUnit.SECONDS);
//        ste.scheduleWithFixedDelay(new Thread(new Test92().new T1(count)), 0, 1, TimeUnit.SECONDS);
//        ste.scheduleWithFixedDelay(new Thread(new Test92().new T1(count)), 0, 1, TimeUnit.SECONDS);
//        ste.scheduleWithFixedDelay(new Thread(new Test92().new T1(count)), 0, 1, TimeUnit.SECONDS);
//        ste.scheduleWithFixedDelay(new Thread(new Test92().new T1(count)), 0, 1, TimeUnit.SECONDS);

    }

    public class T1 implements Runnable {
        AtomicInteger count = null;
        public T1 (AtomicInteger count) {
            this.count = count;
        }
        @Override
        public void run() {
            log.debug("gogogo"+Thread.currentThread().getName()+"   " + count);
            System.out.println("gogogo"+Thread.currentThread().getName()+"   " + count);
            count.getAndIncrement();
        }
    }
}
