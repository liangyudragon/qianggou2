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

public class Test1 {

	ITestContext context;
	WebDriver driver;
	PageProvider page = new PageProvider();
	int waitTime = 10000; //wait order button display
	int index = 0;
	String url = WebURL.url1;
	Utility ut = new Utility();

	@BeforeClass
	public void setUp(ITestContext context) throws Exception{
		System.out.println("URL1");
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
	public void case1() throws Exception{

		driver.get(url);
		ut.switchToWindow(driver, "ご注文予約｜開催予約販売一覧");
	}


}
