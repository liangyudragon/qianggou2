package com.zpp.mail.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 实验log4j按类输出日志
 */
public class Test94 {
    private static final Logger log = LoggerFactory.getLogger(Test94.class);
    public static void main(String[] args) {
        new Test94A().syso();
        new Test94B().syso();
        new Test94C().syso();
    }

}
//class Test94A{
//    private static final Logger log = LoggerFactory.getLogger(Test94A.class);
//    public void syso(){
//        log.debug("this is Test94A");
//    }
//}
//class Test94B{
//    private static final Logger log = LoggerFactory.getLogger(Test94B.class);
//    public void syso(){
//        log.debug("this is Test94B");
//    }
//    
//}
//class Test94C{
//    private static final Logger log = LoggerFactory.getLogger(Test94C.class);
//    public void syso(){
//        log.debug("this is Test94C");
//    }
//}
class Test94Base {
    public static final Logger log = LoggerFactory.getLogger(Test94Base.class);
    public void syso() {
        log.debug("this is " + this.getClass().getName());
    }
}
class Test94A extends Test94Base {}
class Test94B extends Test94Base {}
class Test94C extends Test94Base {}