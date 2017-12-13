package org.se.lab;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class PageObject {

	WebDriver driver;
	boolean acceptNextAlert = true;
	StringBuffer verificationErrors = new StringBuffer();
	String baseUrl;

	private void setDefaults() {
		baseUrl = "http://localhost:8080/";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public PageObject() {
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");
		driver = new FirefoxDriver();
		setDefaults();
	}

	public PageObject(WebDriver driver) {
		this.driver = driver;
		setDefaults();
	}

	boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			throw new RuntimeException(verificationErrorString);
		}
	}
}