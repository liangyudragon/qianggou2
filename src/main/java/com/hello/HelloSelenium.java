package com.hello;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.apache.commons.logging.LogFactory;

/**
 * 执行版本：
 * Selenium：2.45.0
 * firefox：34.0（38、36.0b10、35.0b8实验失败）
 * @author weiqz
 *
 */
public class HelloSelenium {
    /**      
     * @param args
     * 
     */
    public static void main(String[] args) {
        
        // TODO Auto-generated method stub
        //实例化Firefox Driver
        WebDriver driver = new FirefoxDriver();


//        System.setProperty("webdriver.chrome.driver", "D:/testSoft/chromeDriver_2.14.exe");
//        WebDriver driver = new ChromeDriver();

        //跳转到baidu首页
        driver.get("http://www.baidu.com");
        //对象识别         
        By tbSearchElement = By.name("wd");
        By btnSearchElement = By.xpath("//input[@value='百度一下']");
        WebElement tbSearch = driver.findElement(tbSearchElement);
        WebElement btnSearch = driver.findElement(btnSearchElement);
        //对象操作
        tbSearch.sendKeys("iquicktest");
        btnSearch.click();
    }
}