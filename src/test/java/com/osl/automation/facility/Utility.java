package com.osl.automation.facility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedCondition;  

public class Utility {
	//generate a txt file in C:\temp folder
	public static String generateTxtFile() throws IOException {
		String filePath = null;
		File path = new File("C:\\temp\\");
		if(path.exists()) {
			filePath = "C:\\temp\\" + getTimeNow() + ".txt";
			FileWriter fw = new FileWriter(filePath);
			fw.write(getTimeNow());
			fw.flush();
			fw.close();
		}else {
			path.mkdir();
			filePath = "C:\\temp\\" + getTimeNow() + ".txt";
			FileWriter fw = new FileWriter(filePath);
			fw.write(getTimeNow());
			fw.flush();
			fw.close();
		}
		return filePath;
	}
	
	//delete a file
	public static void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}
	
	//find an element inside an element
	//if it doesn't exist, return false
	//else return true
	public static boolean elementExist(WebElement element, By by) {
		try {
			element.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * generate a string, the length of this string depends on the param charCount.
	 * 
	 * @param charCount The length of this string.
	 * @return A random string.
	 */
	public static String generateRandomChar(int charCount) {
		int randomNumber;
		char ch;
		String str = "";
		Random rand = new Random();
		for (int i = 0; i < charCount; i++) {
			randomNumber = rand.nextInt(26) + 97;
			ch = (char) randomNumber;
			str = str + Character.toString(ch);
		}
		return str;
	}

	/**
	 * Generate a random number, the length of this number depends on the param digitCount.
	 * @param The length of this number.
	 * @return A random number.
	 */
	public static Long generateRandomNumber(int digitCount) {
		long randomNumber;

		Random rand = new Random();
		randomNumber = (long) (rand.nextDouble() * Math.pow(10, digitCount));
		return randomNumber;
	}

	/**
	 * Generate a random number between param fromDigit and param toDigit.
	 * @param fromDigit The minium number.
	 * @param toDigit The maxium number.
	 * @return A random number between two params.
	 */
	public static int generateRandomNumber(int fromDigit, int toDigit) {
		int randomNumber;

		Random rand = new Random();
		randomNumber = rand.nextInt(toDigit - fromDigit + 1);
		List<Integer> list = new ArrayList<Integer>();
		for (int i = fromDigit; i <= toDigit; i++) {
			list.add(i);
		}
		return list.get(randomNumber);
	}

	/**
	 * Get the current time.
	 * @return The time format would be yyyyMMddHHmmss.
	 */
	public static String getTimeNow() {
		SimpleDateFormat timePattern = new SimpleDateFormat("yyyyMMddHHmmss");
		return timePattern.format(new Date());
	}
	
	/**
	 * Get the date.
	 * @param afterDays The param controls which date should be return.
	 * <p>For example, if the input is 1, that means you will get the tomorrow date.
	 * <p>If you input -1, that means you will get the yesterday date.
	 * @return The time format would be MM/dd/yyyy.
	 */
	public static String getDate(int afterDays) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, afterDays);
		date = calendar.getTime();
		SimpleDateFormat timePattern = new SimpleDateFormat("MM/dd/yyyy");
		return timePattern.format(date);
	}
	
	public static void waitElement(WebDriver driver, final WebElement element){
		 WebDriverWait wait = new WebDriverWait(driver,10);  
	        wait.until(new ExpectedCondition<WebElement>(){  
	            @Override
				public WebElement apply(WebDriver arg0) {
					// TODO Auto-generated method stub
					return null;
				}}).click();  
	}
	
	public void switchToWindow(WebDriver driver,String windowTitle) throws Exception{  
		
	    try {  
	        //String currentHandle = driver.getWindowHandle();  
	        Set<String> handles = driver.getWindowHandles();  
	        for (String s : handles) {  
	           /* if (s.equals(currentHandle))  
	                continue;  
	        else {  */
	                driver.switchTo().window(s);  
	                if (driver.getTitle().contains(windowTitle)) {  
	                    
	                    System.out.println("Switch to window: "  
	                            + windowTitle + " successfully!");  
	                    break;  
	                } else  
	                    continue;  
	           // }  
	        }  
	    } catch (NoSuchWindowException e) {  
	    	System.out.println(e.getMessage());
	        throw new Exception("Window: " + windowTitle  
	                + " cound not found!");   
	    }  
	}  
}
