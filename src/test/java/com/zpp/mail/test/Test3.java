package com.zpp.mail.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPMessage;
import com.zpp.mail.utils.MailUtil;


/**
 * 测试类--登录邮箱,查找预约邮件
 * 
 */
public class Test3 {

    private static final Logger log = LoggerFactory.getLogger(Test3.class);
    
    public static void main(String[] args) throws Exception { 
        // 准备连接服务器的会话信息 
        Properties props = new Properties(); 
        

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        props.setProperty("mail.store.protocol", "imap"); 
        props.setProperty("mail.imap.host", "imap.mail.yahoo.co.jp"); 
//        props.setProperty("mail.imap.port", "143"); // 非SSL

        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");

        // 创建Session实例对象 
        Session session = Session.getInstance(props); 

        // 创建IMAP协议的Store对象 
        Store store = session.getStore("imap"); 

        // 连接邮件服务器 
        store.connect("taziqe@yahoo.co.jp", "woaiwojia1314"); 

        // 获得收件箱 
        Folder folder = store.getFolder("INBOX"); 
        // 以读写模式打开收件箱 
        folder.open(Folder.READ_WRITE); 

        // 获得收件箱的邮件列表 
        Message[] messages = folder.getMessages(); 

        // 打印不同状态的邮件数量 
        System.out.println("收件箱中共" + messages.length + "封邮件!"); 
        System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!"); 
        System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!"); 
        System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!"); 

        System.out.println("------------------------开始解析邮件----------------------------------"); 

        // 解析 [全部] 邮件 
//        for (Message message : messages) { 
//            IMAPMessage msg = (IMAPMessage) message; 
//            String subject = MimeUtility.decodeText(msg.getSubject()); 
//            System.out.println("[" + subject + "]未读，是否需要阅读此邮件（yes/no）？"); 
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
//            String answer = reader.readLine();
//            if ("yes".equalsIgnoreCase(answer)) { 
//                POP3ReceiveMailTest.parseMessage(msg);  // 解析邮件 
//                // 第二个参数如果设置为true，则将修改反馈给服务器。false则不反馈给服务器 
//                msg.setFlag(Flag.SEEN, true);   //设置已读标志 
//            } 
//        } 
        
        // 解析 [未读] 邮件 
        String subject = null;
        for (Message message : messages) {
            subject = MimeUtility.decodeText(message.getSubject());

            // 处理已读邮件
            if (MailUtil.isRead(message)) {
                log.debug("已读邮件：" + subject);
                message.setFlag(Flag.SEEN, true);   //设置已读标志 
                continue;
            }

            // 处理非预约文件
            // TODO 区分预约完成邮件
            IMAPMessage msg = (IMAPMessage) message; 
            if (!"ご注文予約案内".equals(subject)) {
                // 非预约邮件，标记为已读
                log.debug("！！！接收到非预约邮件：" + subject);
                message.setFlag(Flag.SEEN, true);   //设置已读标志 
                continue;
            }

//            MailUtil.parseMessage(msg);  // 打印邮件信息 
            MailUtil.getMailTextContent(msg, new StringBuffer(30));  // 解析邮件

            String url = MailUtil.getAdvcRegURL(msg);


            // TODO 将URL加入线程
            
            
            
            
            // FIXME 第二个参数如果设置为true，则将修改反馈给服务器。false则不反馈给服务器 
//            msg.setFlag(Flag.SEEN, true);   //设置已读标志 

        } 

        // 关闭资源 
        folder.close(false); 
        store.close(); 
    }

}
