package com.osl.automation.facility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.osl.automation.page.osl.*;

public class PageProvider {
	
	public advc advcPage(WebDriver driver){
		return PageFactory.initElements(driver, advc.class);
	}
}
