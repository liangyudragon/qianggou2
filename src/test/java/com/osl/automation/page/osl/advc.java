package com.osl.automation.page.osl;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class advc {

	@FindBy(how = How.CSS, css = ".btnReserv>form>input[name='sbmt']")
	public List<WebElement> orderBtn;

	@FindBy(how = How.CSS, css = ".btnReserv>form>input[name='sbmt']")
	public WebElement orderBtn1;

	@FindBy(how = How.NAME, name = "mail1")
	public WebElement email1;

	@FindBy(how = How.NAME, name = "mail2")
	public WebElement email2;

	@FindBy(how = How.CSS, css = ".inputTxt1")
	public WebElement card_no;

	@FindBy(how = How.NAME, name = "sbmt")
	public WebElement submit;

	@FindBy(how = How.ID, id = "login_frame")
	public WebElement loginFrame;

	@FindBy(how = How.ID, id = "switcher_plogin")
	public WebElement qqLogin;

	@FindBy(how = How.ID, id = "u")
	public WebElement qqID;

	@FindBy(how = How.ID, id = "p")
	public WebElement qqPwd;

	@FindBy(how = How.ID, id = "login_button")
	public WebElement loginBtn;

	@FindBy(how = How.PARTIAL_LINK_TEXT, partialLinkText = "收件箱")
	public WebElement inbox;

	@FindBy(how = How.ID, id = "mainFrame")
	public WebElement emailFrame;

	@FindBy(how = How.XPATH, xpath = ".//*[@id='div_showtoday']/*//u[text()='ご注文予約案内']")
	public List<WebElement> emailList;

	//@FindBy(how = How.XPATH, xpath = ".//*[@id='div_showyesterday']/*//u[text()='ご注文予約案内']")
	//public List<WebElement> emailList;

	@FindBy(how = How.CSS, css = "#mailContentContainer>a")
	public WebElement advcLink;

	@FindBy(how = How.CLASS_NAME, className = "error")
	public WebElement error;

	@FindBy(how = How.LINK_TEXT, linkText = "内容詳細")
	public List<WebElement> description;

	@FindBy(how = How.CLASS_NAME, className = "caution3")
	public List<WebElement> caution3;

	@FindBy(how = How.CLASS_NAME, className = "inputTxt1")
	public List<WebElement> name;

	@FindBy(how = How.CLASS_NAME, className = "inputTxt2")
	public List<WebElement> number;

	@FindBy(how = How.CLASS_NAME, className = "headline2")
	public WebElement success;

	@FindBy(how = How.CSS, css = ".c-footer-a1.btn_blue")
	public WebElement goOn;

	@FindBy(how = How.CSS, css = ".borT")
	public WebElement sendEmailVerify;

}
