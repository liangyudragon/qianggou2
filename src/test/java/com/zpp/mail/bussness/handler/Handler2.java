package com.zpp.mail.bussness.handler;

import java.security.Security;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPMessage;
import com.zpp.mail.bussness.Main;
import com.zpp.mail.bussness.constants.InitConstants;
import com.zpp.mail.utils.MailUtil;

public class Handler2 implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(Handler2.class);

    private ConcurrentLinkedQueue<String> queue = null;

    private AtomicInteger count = null; // 成功发送邮件数
    private int stopCount = 0;
    public Handler2(ConcurrentLinkedQueue<String> queue, AtomicInteger count, int stopCount) {
        this.queue = queue;
        this.count = count;
        this.stopCount = stopCount;
    }
    
    @Override
    public void run() {
        if (count.get() >= stopCount) {
            log.debug("count.get() is ::::"+String.valueOf(count.get()));
            return;
        }
        // 参考Test3.java
        // 准备连接服务器的会话信息 
        Properties props = new Properties(); 

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.fallback", "false");

        props.setProperty("mail.imap.host", "imap.mail.yahoo.co.jp");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");

        // 创建Session实例对象 
        Session session = Session.getInstance(props); 
        Store store = null;
        Folder folder = null;
        try {
            // 创建IMAP协议的Store对象 
            store = session.getStore("imap"); 

            // 连接邮件服务器 
            store.connect(InitConstants.HANDLER2_USERNAME, InitConstants.HANDLER2_PASSWORD); 

            // 获得收件箱 
            folder = store.getFolder("INBOX"); 
            // 以读写模式打开收件箱 
            folder.open(Folder.READ_WRITE); 

            // 获得收件箱的邮件列表 
            Message[] messages = folder.getMessages(); 

            // 打印不同状态的邮件数量 
            log.info("收件箱中共" + messages.length + "封邮件!"); 
            log.info("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!"); 
            log.info("收件箱中共" + folder.getNewMessageCount() + "封新邮件!"); 
            log.info("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!"); 

            log.info("------------------------开始解析邮件----------------------------------"); 

            // 解析 [未读] 邮件 
            String subject = null;
            for (Message message : messages) {
                subject = MimeUtility.decodeText(message.getSubject());

                // 处理已读邮件
                if (MailUtil.isRead(message)) {
                    log.debug("已读邮件：" + subject);
                    message.setFlag(Flag.SEEN, true);   //设置已读标志 
//                    message.setFlag(Flag.DELETED, true);
                    continue;
                }

                // 处理非预约文件
                // TODO 区分预约完成邮件
                IMAPMessage msg = (IMAPMessage) message; 
                if (!"ご注文予約案内".equals(subject)) {
                    // 非预约邮件，标记为已读
                    if ("【アカチャンホンポ】予約完了のご案内".equals(subject)) {
                        log.info("♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦有预约成功商品♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦♦");
                        count.getAndIncrement();
                    } else {
                        log.debug("！！！接收到非预约邮件：" + subject);
//                        msg.setFlag(Flag.DELETED, true);
                        continue;
                    }
                    msg.setFlag(Flag.SEEN, true);   //设置已读标志 
                    continue;
                }

//            MailUtil.parseMessage(msg);  // 打印邮件信息 
                MailUtil.getMailTextContent(msg, new StringBuffer(30));  // 解析邮件

                String url = MailUtil.getAdvcRegURL(msg);
                // XXX offer方法和add方法的区别？？
                queue.offer(url);

                //  设置已读标志 
                msg.setFlag(Flag.SEEN, true);

                // 删除邮件
//                msg.setFlag(Flag.DELETED, true);  

            }
//            count.getAndIncrement();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            
            // 关闭资源 
            try {
                folder.close(false);
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
            } 
            try {
                store.close();
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
            } 
        }
    }

}
