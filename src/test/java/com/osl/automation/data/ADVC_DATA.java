package com.osl.automation.data;

import org.testng.annotations.DataProvider;

public class ADVC_DATA {

	@DataProvider(name="advc")
	public static Object[][] advc(){		
		return new Object[][]{
				{"382924682@qq.com","https://mail.qq.com/cgi-bin/loginpage","card_no","phone","quantiy"}
		};
	}
}
