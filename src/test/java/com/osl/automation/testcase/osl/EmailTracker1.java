package com.osl.automation.testcase.osl;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.osl.automation.config.WebURL;
import com.osl.automation.facility.*;

public class EmailTracker1 {

	ITestContext context;
	WebDriver driver;
	PageProvider page = new PageProvider();
	int wait = 1000; //wait email display
	int index = 1;
	Utility ut = new Utility();

	@BeforeClass
	public void setUp(ITestContext context) throws Exception{
		System.out.println("emailtracker1");
		this.context = context;
		TestInit ti = new TestInit("FF");
		driver = ti.getDriver();
		context.setAttribute("CONTEXT_KEY_DRIVER", driver);

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
		driver.switchTo().frame(page.advcPage(driver).loginFrame);
		page.advcPage(driver).qqLogin.click();
		page.advcPage(driver).qqID.sendKeys(email);
		page.advcPage(driver).qqPwd.sendKeys(emailPwd);
		page.advcPage(driver).loginBtn.click();
		String emailListURL = driver.getCurrentUrl();
		driver.get(emailListURL);

		while(index<=9){
			page.advcPage(driver).inbox.click();
			driver.switchTo().frame(page.advcPage(driver).emailFrame);
			getEmail(wait,emailListURL,index);
			order(card_no,pwd,ln1,fn1,ln2,fn2,phone1,phone2,phone3);
			index = index +3;
		}
	}

	private void getEmail(int wait,String emailListURL,int emailIndex) throws Exception{
		int emailCount;
		int waitTime = wait;

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		emailCount = page.advcPage(driver).emailList.size();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		if(emailCount>=emailIndex){
			page.advcPage(driver).emailList.get(emailCount-emailIndex).click();
		}else{
			if(waitTime>0){
				waitTime--;
				getEmail(waitTime,emailListURL,emailIndex);
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

		page.advcPage(driver).advcLink.click();
		try{
			ut.switchToWindow(driver, "ご注文予約｜エラー");
		}catch(Exception ex){
			ut.switchToWindow(driver, "QQ邮箱 - 您将要访问的网页来源未知。");
			page.advcPage(driver).goOn.click();
		}
		try{
			page.advcPage(driver).card_no.sendKeys(card_no);
			page.advcPage(driver).submit.click();
			flag = true;
		}catch(Exception ex){
			try{
				if(page.advcPage(driver).error.getText().equals("DB接続に失敗しました。")){
					driver.close();
					ut.switchToWindow(driver, "QQ邮箱");
					driver.switchTo().frame(page.advcPage(driver).emailFrame);
					step1(card_no);
				}else{
					driver.close();
					ut.switchToWindow(driver, "QQ邮箱");
					System.out.println("邮件"+"index: "+index+"  error, go to next email.");
				}
			}catch(Exception ex1){
				driver.close();
				ut.switchToWindow(driver, "QQ邮箱");
				System.out.println("邮件"+"index: "+index+"  error, go to next email.");
			}
		}

		return flag;
	}

	private boolean step2(String pwd,String ln1,String fn1,String ln2,String fn2,String phone1,String phone2, String phone3) throws Exception{
		boolean flag = false;
		try{
			page.advcPage(driver).number.get(0).sendKeys(pwd);
			page.advcPage(driver).name.get(0).sendKeys(ln1);
			page.advcPage(driver).name.get(1).sendKeys(fn1);
			page.advcPage(driver).name.get(2).sendKeys(ln2);
			page.advcPage(driver).name.get(3).sendKeys(fn2);
			page.advcPage(driver).number.get(1).sendKeys(phone1);
			page.advcPage(driver).number.get(2).sendKeys(phone2);
			page.advcPage(driver).number.get(3).sendKeys(phone3);
			flag = true;
		}catch(Exception ex){
			try{
				if(page.advcPage(driver).error.getText().equals("DB接続に失敗しました。")){
					driver.get(driver.getCurrentUrl());
					step2(pwd,ln1,fn1,ln2,fn2,phone1,phone2,phone3);
				}else{
					driver.close();
					ut.switchToWindow(driver, "QQ邮箱");
					System.out.println("邮件"+"index: "+index+"  step1 error, go to next email.");
				}
			}catch(Exception ex1){
				driver.close();
				ut.switchToWindow(driver, "QQ邮箱");
				System.out.println("邮件"+"index: "+index+"  step1 error, go to next email.");
			}
		}

		return flag;
	}

	private void step2Submit() throws Exception{
		try{
			page.advcPage(driver).submit.click();
			page.advcPage(driver).submit.click();
			if(page.advcPage(driver).success.getText().equals("【テープ式】メリーズLサイズ(9～14kgまで)")){
				System.out.println("邮件"+"index: "+index+"  发送成功!!");
			}else{
				System.out.println("邮件"+"index: "+index+"  step2 发送验证失败, go to next email.");
			}
		}catch(Exception ex){
			try{
				if(page.advcPage(driver).error.getText().equals("DB接続に失敗しました。")){
					driver.get(driver.getCurrentUrl());
					step2Submit();
				}else{
					System.out.println("邮件"+"index: "+index+"  step2 error, go to next email.");
				}
			}catch(Exception ex1){
				System.out.println("邮件"+"index: "+index+"  step2 error, go to next email.");
			}
		}

		driver.close();
		ut.switchToWindow(driver, "QQ邮箱");
	}
}
