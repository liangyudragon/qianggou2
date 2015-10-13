package com.zpp.mail.test;

import java.io.File;
import java.net.URL;

/**
 * 测试类加载器
 * @author weiqz
 *
 */
public class Test90 {
    
    public static void main(String[] args) {
        
        File file2 = new File("/E:/projects/workspace_hadoop/advc_automation/target/test-classes/com/osl/automation/config/FilePath.class");
        
        if (file2.isFile()) {
            System.out.println("OK");
        }
        
        URL resource = Test90.class.getClassLoader().getResource("com/osl/automation/config/FilePath.class");
        System.out.println(Test90.class.getClassLoader());
        System.out.println(resource);
        
        String resourceFile = resource.getFile();
        System.out.println(resourceFile);
        
        File file = new File(resourceFile);
        
        if (file.isFile()) {
            System.out.println("fileName is " + file.getPath());
        }
    }

}
