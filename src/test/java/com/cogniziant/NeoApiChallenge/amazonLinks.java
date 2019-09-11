package com.cogniziant.NeoApiChallenge;

import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class amazonLinks {
	
	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Evgeny\\Selenium\\1\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.get("http:\\www.google.com");

		WebElement searchBar = driver.findElement(By.xpath("//*/input[@title='Search']"));
		searchBar.sendKeys("amazon");
		searchBar.sendKeys(Keys.ENTER);

		List<WebElement> links = driver.findElements(By.xpath("//table[@class='nrgt']/tbody/tr/td"));

		HashMap<Integer, String> map = new HashMap<>();
		int count = 1;
		for (WebElement we : links) {
			String url = we.findElement(By.tagName("a")).getAttribute("href");
			map.put(count, url);
			count++;
		}

		System.out.println(map);

		driver.close();
	}
}