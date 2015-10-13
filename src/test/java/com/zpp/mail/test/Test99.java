package com.zpp.mail.test;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Test99 {
    static int i;

    public static void main(String[] args) {
//        for (i = 0; i < 3; i++) {
//            new Thread() {
//                public void run() {
//                    System.out.println(i);
//                }
//            }.start();
//        }
        String url = "http://aksale.advs.jp/cp/akachan_sale_pc/search_event_list.cgi?area2=%90_%93%de%90%ec%8c%a7&event_type=6&sid=37186&kmws=";

        if (url.contains("aksale.advs.jp")) {
            System.out.println("in IF");
            System.out.println(url.replace("aksale.advs.jp", "210.136.104.101"));
            System.out.println(url.replaceAll("aksale\\.advs\\.jp", "210.136.104.101"));
        } else {
            System.out.println("in ELSE");
        }
        System.out.println(url);
//        methodA();
    }
    
    public static void methodA() {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
        System.out.println(queue.peek()== null);
    }
}
