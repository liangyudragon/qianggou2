package com.osl.automation.testcase.osl;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.osl.automation.config.WebURL;
import com.osl.automation.page.osl.advc;

public class ADVC_URL3 {

	WebDriver driver;
	advc page = new advc();
	int waitTime = 10000; //wait order button display
	int index = 0;
	String url = WebURL.url3;

	@BeforeClass
	public void setUp() throws Exception{
		System.out.println(url);
		//FirefoxDriver driver = new FirefoxDriver();
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		this.driver = driver;
		page = PageFactory.initElements(driver, advc.class);
	}

	@AfterClass
	public void tearDown(){
		driver.quit();
	}

	@Test()
	@Parameters({"Email"})
	public void case1(String email) throws Exception{

		while(index<=2){
			sendEmail(url,email,index);
			index++;
		}
	}

	private void sendEmail(String url,String email,int index){
		int emailCount;
		driver.get(url);

		while(true){
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			emailCount = page.orderBtn.size();
			if(emailCount>0){
				break;
			}else{
				if(waitTime>0){
					waitTime--;
					driver.get(url);
					continue;
				}else{
					return;
				}
			}
		}

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		page.orderBtn.get(index).click();
		page.email1.sendKeys(email);
		page.email2.sendKeys(email);
		//String currURL = driver.getCurrentUrl();
		step1(email);
	}

	private void step1(String email){

		try{
			page.submit.click();
			page.submit.click();
			if(page.sendEmailVerify.isDisplayed()){
				System.out.println("url3 index "+index+"  发送成功");
			}
		}catch(Exception ex){
			try{
				if(page.error.getText().equals("DB接続に失敗しました。")){
					System.out.println("url3  index: "+index+"  step1 DB接続に失敗しました.");
					driver.get(driver.getCurrentUrl());
					page.email1.sendKeys(email);
					page.email2.sendKeys(email);
					step1(email);
				}else{
					System.out.println("url3  index: "+index+"  step1 error.");
				}
			}catch(Exception ex1){
				System.out.println("url3  index: "+index+"  step1 exception.");
			}
		}
	}
}
