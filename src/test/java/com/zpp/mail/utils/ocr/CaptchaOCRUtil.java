package com.zpp.mail.utils.ocr;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class CaptchaOCRUtil {

    private static final Logger log = LoggerFactory.getLogger(CaptchaOCRUtil.class);

    private static String DLLPATH = "51OCR"; // 原来是 CaptchaOCR

    public interface OCR extends Library {

        OCR INSTANCE = (OCR) Native.loadLibrary(DLLPATH, OCR.class);
        
        /* 函数功能：加载样本图片特征库,特征库可以任意改名。
         *           只需要调用一次,多线程要特别注意
         *           单线程可以不调用这个函数, dll会默认使用同目录下的51ocr.Templates
        */ 
        public int www_51ocr_com_InitOCR(String templatefile); 
        
        
        /* 函数功能：识别验证码图片
         * 参数：[in] imagefile:图片完整路径 
         *       [out] text:识别结果 使用前需要分配内存 如text = new char[256]
         * 返回：0：正常识别；-1：异常，如文件不存在等
         */
        public int www_51ocr_com_RECOG_1(String templatefile, byte[] text); 

        /*
         * 函数功能：识别验证码图片
         * 参数：[in] imagebuf: 图片文件内存数据
         *       [in] size:     imagebuf大小
         *       [in] type:     图片类型 1,BMP; 2,GIF; 3,JPG; 4,PNG
         *       [out] text:识别结果 使用前需要分配内存 如text = new char[256]
         * 返回：0：正常识别；-1：异常，如文件不存在等
         */
        public int www_51ocr_com_RECOG_2(byte[] imagebuf, int size, int type, byte[] text);
    }
    
    public static boolean init() {

        int uid = 0;
        
        try {
            File directory = new File("");
            String currentPath = directory.getCanonicalPath();
            System.out.println("当前路径：" + currentPath);
            uid = OCR.INSTANCE.www_51ocr_com_InitOCR(currentPath
                    + "\\51ocr.Templates");
        } catch (Exception ex) {
            log.error("failed to load the 51ocr.Templates.", ex);
        }
        
        if (uid == 0) {
            log.info("load the 51ocr.Templates successfully.");
            return true;
        } else {
            log.error("failed to load the 51ocr.Templates, return code：" + uid);
            return false;
        }
    }

    public static String DecodeByFileName(String filePath) {

        byte[] byteResult = new byte[12];
        int cid = OCR.INSTANCE.www_51ocr_com_RECOG_1(filePath, byteResult);

        String strResult = "";
        try {
            if (cid == 0) {
                strResult = new String(byteResult, "UTF-8").trim();
                log.info("captcha is ocrrd successfully, captcha code:" + strResult);
            } else {
                log.error("failed to ocr captcha , return code:" + cid);
            }
        } catch (Exception ex) {
            log.error("failed to ocr captcha , return code:" + cid, ex);
        }

        return strResult;
    }

    public static String DecodeByByBytes(byte[] captchaByte) {

        byte[] byteResult = new byte[12];
        int cid = OCR.INSTANCE.www_51ocr_com_RECOG_2(captchaByte,
                captchaByte.length, 3, byteResult);

        String strResult = "";
        try {
            if (cid == 0) {
                strResult = new String(byteResult, "UTF-8").trim();
                log.info("captcha is ocrrd successfully, captcha code:" + strResult);
            } else {
                log.error("failed to ocr captcha , return code:" + cid);
            }
        } catch (Exception ex) {
            log.error("failed to ocr captcha , return code:" + cid, ex);
        }

        return strResult;
    }

    // 我是做测试的。。。。。。。。
    public static void main(String[] args) {
        CaptchaOCRUtil.init();
        CaptchaOCRUtil.DecodeByFileName("D:/14443694544008.jpeg"); // D:/14443694544008.jpeg
    }
}
