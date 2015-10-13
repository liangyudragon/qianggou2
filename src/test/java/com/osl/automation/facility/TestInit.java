package com.osl.automation.facility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.osl.automation.config.FilePath;

public class TestInit {
	
    private WebDriver driver;
	

	//remote driver should start with "Remote" and slipt with "_" such as Remote_FF_10_XP
	private void setRemoteDriver(String remoteDriver) throws Exception{
		DesiredCapabilities capabilities = null;
		String browser;
		String version;
		String platform;
		
		String [] remote = remoteDriver.split("_");
		browser = remote[1];
		version = remote[2];
		platform = remote[3];
		
		//set browser
		switch(browser){
		case"FF": capabilities = DesiredCapabilities.firefox(); break;
		case"IE": capabilities = DesiredCapabilities.internetExplorer(); break;
		case"Chrome": capabilities = DesiredCapabilities.chrome(); break;
		case"Safari": capabilities = DesiredCapabilities.safari(); break;
		}
		
		//set version for the specific browser
		capabilities.setCapability("version",version);
		
		//set platform
		switch(platform){
		case"XP": capabilities.setCapability("platform",Platform.XP); break;
		case"WIN8": capabilities.setCapability("platform",Platform.WIN8); break;
		case"WINDOWS": capabilities.setCapability("platform",Platform.WINDOWS); break;
		case"WIN8_1": capabilities.setCapability("platform",Platform.WIN8_1); break;
		case"MAC": capabilities.setCapability("platform",Platform.MAC); break;
		case"LINUX": capabilities.setCapability("platform",Platform.LINUX); break;
		}
		
		//set driver
		this.driver = new RemoteWebDriver(new URL("http://" + FilePath.saucelabUsername + ":" 
								+ FilePath.saucelabAccess + "@ondemand.saucelabs.com:80/wd/hub"), capabilities);
		//this.edriver.manage().window().maximize();
	}
	
	
    private void selectDriver(String driverName) {
    	switch(driverName) {
    	case "IE": setDriver(new InternetExplorerDriver()); break;
    	case "FF": setDriver(new FirefoxDriver(setCertificates())); break;
    	case "Chrome": System.setProperty("webdriver.chrome.driver", FilePath.chromeDriver);setDriver(new ChromeDriver()); break;
    	}
    }

    public TestInit(String driverName) throws Exception {
    	
    	if(driverName.startsWith("Remote")){
    		setRemoteDriver(driverName);
    	}else{
	    	switch(driverName) {
	    	case "IE": setDriver(new InternetExplorerDriver()); break;
	    	case "FF": setDriver(new FirefoxDriver(setCertificates())); break;
	    	//case "FF": setDriver(new FirefoxDriver()); break;
	    	case "Chrome": System.setProperty("webdriver.chrome.driver", FilePath.chromeDriver);setDriver(new ChromeDriver()); break;
	    	}
    	}
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getDriver().manage().window().maximize();
    }
    
    /**
     * 
     * @param driver This parameter will accept the WebDriver
     *  instance like FirefoxDriver, IEDriver or ChromeDriver, then set it to {@link TestInit#driver} and
     *  make it static.
     *  <p>Also, the implicitlyWait method will be called to wait a element appear for 60 seconds.
     */
    public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * Get a copy of {@link TestInit#driver} thread.
	 * If you want to run the test, you must use this method in order to use the methods in WebDriver.</p>
	 * @return Get a thread copy of WebDriver instance.
	 */
	public WebDriver getDriver() {
		return this.driver;
	}
	 
	/**
	 * Ignore the security certificates warnings of firefox.
	 * @return Get the firefox profile with security warnings off.
	 */
	public FirefoxProfile setCertificates() {
    	FirefoxProfile firefoxProfile = new FirefoxProfile();
    	firefoxProfile.setAcceptUntrustedCertificates(true);
    	firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
		return firefoxProfile;
    }
}