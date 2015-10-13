package com.osl.automation.testcase.osl;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.osl.automation.config.WebURL;
import com.osl.automation.facility.*;
import com.osl.automation.page.osl.advc;

public class CopyOfEmailTracker1 {

	WebDriver driver;
	advc page = new advc();
	int wait = 1000; //wait email display
	int index = 1;
	String emailListURL;
	Utility ut = new Utility();

	@BeforeClass
	public void setUp() throws Exception{
		System.out.println("emailtracker"+index);
		FirefoxDriver driver = new FirefoxDriver();
		//HtmlUnitDriver driver = new HtmlUnitDriver();
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
	@Parameters({"Email","EmailPassword","CardNumber","LastName1","FirstName1","LastName2","FirstName2","Password","PhoneField1","PhoneField2","PhoneField3"})
	public void case1(String email,String emailPwd,String card_no,String ln1,String fn1,
				String ln2,String fn2,String pwd,String phone1,String phone2,String phone3) throws Exception{

		driver.get(WebURL.QQEmail);
		driver.switchTo().frame(page.loginFrame);
		page.qqLogin.click();
		page.qqID.sendKeys(email);
		page.qqPwd.sendKeys(emailPwd);
		page.loginBtn.click();
		emailListURL = driver.getCurrentUrl();
		//driver.get(emailListURL);

		while(index<=9){
			page.inbox.click();
			driver.switchTo().frame(page.emailFrame);
			getEmail(wait,index);
			order(card_no,pwd,ln1,fn1,ln2,fn2,phone1,phone2,phone3);
			index = index +3;
		}
	}

	private void getEmail(int wait,int emailIndex) throws Exception{
		int emailCount;
		int waitTime = wait;

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		emailCount = page.emailList.size();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		if(emailCount>=emailIndex){
			page.emailList.get(emailCount-emailIndex).click();
		}else{
			if(waitTime>0){
				waitTime--;
				getEmail(waitTime,emailIndex);
			}else{
				throw new Exception("Can not find email index: "+emailIndex);
			}
		}
	}

	private void order(String card_no,String pwd,String ln1,String fn1,String ln2,
			               String fn2,String phone1,String phone2, String phone3) throws Exception{

		if(true == step1(card_no)){
			if(true == step2(pwd,ln1,fn1,ln2,fn2,phone1,phone2,phone3)){
				step2Submit();
			}
		}
	}

	private boolean step1(String card_no) throws Exception{
		boolean flag = false;

		try{
			String advcLink = page.advcLink.getAttribute("href");
			driver.get(advcLink);
			page.card_no.sendKeys(card_no);
			page.submit.click();
			flag = true;
		}catch(Exception ex){
			try{
				if(page.error.getText().equals("DB接続に失敗しました。")){
					System.out.println("邮件"+"index: "+index+"  step1 DB接続に失敗しました。");
					driver.get(emailListURL);
					page.inbox.click();
					driver.switchTo().frame(page.emailFrame);
					page.emailList.get(index-1).click();
					step1(card_no);
				}else if(page.error.getText().equals("ただいま満席のため、応募受付ができません。")){
					System.out.println("邮件"+"index: "+index+"  step1 ただいま満席のため、応募受付ができません。");
				}else{
					System.out.println("邮件"+"index: "+index+" step1 error, go to next email.");
				}
			}catch(Exception ex1){
				System.out.println("邮件"+"index: "+index+"  step1 exception, go to next email.");
			}
			driver.get(emailListURL);
		}

		return flag;
	}

	private boolean step2(String pwd,String ln1,String fn1,String ln2,String fn2,String phone1,String phone2, String phone3) throws Exception{
		boolean flag = false;
		try{
			page.number.get(0).sendKeys(pwd);
			page.name.get(0).sendKeys(ln1);
			page.name.get(1).sendKeys(fn1);
			page.name.get(2).sendKeys(ln2);
			page.name.get(3).sendKeys(fn2);
			page.number.get(1).sendKeys(phone1);
			page.number.get(2).sendKeys(phone2);
			page.number.get(3).sendKeys(phone3);
			page.submit.click();
			flag = true;
		}catch(Exception ex){
			try{
				if(page.error.getText().equals("DB接続に失敗しました。")){
					System.out.println("邮件"+"index: "+index+"  step2 DB接続に失敗しました");
					driver.get(driver.getCurrentUrl());
					step2(pwd,ln1,fn1,ln2,fn2,phone1,phone2,phone3);
				}else if(page.error.getText().equals("不正なアクセスです。恐れ入りますが、もう一度最初から操作してください。")){
					System.out.println("邮件"+"index: "+index+"  step2 不正なアクセスです。恐れ入りますが、もう一度最初から操作してください。");
				}else{
					System.out.println("邮件"+"index: "+index+"  step2 error, go to next email.");
				}
			}catch(Exception ex1){
				System.out.println("邮件"+"index: "+index+"  step2 exception, go to next email.");
			}
			driver.get(emailListURL);
		}

		return flag;
	}

	private void step2Submit() throws Exception{
		try{
			page.submit.click();
			if(page.success.getText().equals("【テープ式】メリーズLサイズ(9～14kgまで)")){
				System.out.println("邮件"+"index: "+index+"  发送成功!!");
			}else{
				try{
					if(page.error.getText().equals("DB接続に失敗しました。")){
						System.out.println("邮件"+"index: "+index+"  step2submit DB接続に失敗しました。");
						driver.get(driver.getCurrentUrl());
						step2Submit();
					}else if(page.error.getText().equals("ただいま満席のため、応募受付ができません。")){
						System.out.println("邮件"+"index: "+index+"  step2submit ただいま満席のため、応募受付ができません。");
					}else{
						System.out.println("邮件"+"index: "+index+"  step2submit error, go to next email.");
					}
				}catch(Exception ex1){
					System.out.println("邮件"+"index: "+index+"  step2submit exception(ex1), go to next email.");
				}
			}
		}catch(Exception ex){
			System.out.println("邮件"+"index: "+index+"  step2submit exception(ex), go to next email.");
		}

		driver.get(emailListURL);
	}
}
