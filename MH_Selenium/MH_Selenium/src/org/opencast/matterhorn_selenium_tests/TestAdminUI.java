package org.opencast.matterhorn_selenium_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestAdminUI {

	private WebDriver driver;
	private String baseUrl = "http://testallinone.usask.ca:8080";// "http://demo.opencastproject.org";
	
	private String recordingName = "Aufzeichnung ä ö ü ß";
	private String authorName = "Helmut Müller";
	private String contributor = "Test Contributor";
	private String subject = "Künstliche Intellegenz";
	private String language = "Deutsch";
	private String description = "Beschreibung";
	private String startDate = "2033-01-01";
	private int durationHours = 1;
	private int durationMins = 30;
	private String startTimeHours = "08";
	private String startTimeMins = "30";
	private String agent = "demo_capture_agent";
	
	private String notFound = "0 found";
		
//	@Test
	public void testAdminLogIn() {
		
		driver = new HtmlUnitDriver();
		
		driver.get(baseUrl);
		
		WebElement element = driver.findElement(By.tagName("h2"));
		
		String titelString = "Welcome to Matterhorn";
		
		assertEquals(titelString, element.getText());
		
		WebElement username = driver.findElement(By.name("j_username"));
		WebElement password = driver.findElement(By.name("j_password"));
		
		username.sendKeys("admin");
		password.sendKeys("opencast");
		
		WebElement logInLink = driver.findElement(By.name("submit"));
		
		logInLink.click();
		
		element = driver.findElement(By.id("col1m")).findElement(By.tagName("h2"));
		
		String adminString = "Administrative Tools";
		
		assertEquals(adminString, element.getText());
		
		WebElement adminLink = driver.findElement(By.id("adminlink"));
		
		adminLink.click();
		
		element = driver.findElement(By.tagName("h1"));
		
		String adminTools = "Admin Tools";
		
		assertEquals(adminTools, element.getText());
		
		driver.quit();
		
	}
	
//	@Test
	public void recordingsTabInspectToolTest() {
		
		driver = login();
		
		// recordings table
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recordingsTable")));
		
		List<WebElement> elements = driver.findElements(By.cssSelector(".status-column-cell.ui-state-active"));
		
		boolean checkFinishedJob = false;
		
		for (WebElement el : elements) {
			
			// find a finished job
			if (el.getText().equals("Finished") && !checkFinishedJob) {
				
				checkFinishedJob = true;
				
				WebElement jobLink = el.findElement(By.tagName("a"));
				jobLink.click();
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inspectContainer")));
				
				WebElement element = driver.findElement(By.cssSelector(".td-value"));		
				String succeeded = "SUCCEEDED";
		
				assertEquals(succeeded, element.getText());
				
				// mediapackage tab
				WebElement mediapackageLink = driver.findElement(By.linkText("Mediapackage"));
				mediapackageLink.click();
				
				WebElement mediapackage = driver.findElement(By.id("mediapackage"));
				
				// TODO ausfuerlicher testen?
				
				// find start cell
				WebElement key = mediapackage.findElement(By.cssSelector(".td-key"));
				String start = "start";
				
				assertEquals(start, key.getText());
				
				// operations tab
				WebElement operationsLink = driver.findElement(By.linkText("Operations"));
				operationsLink.click();
				
				WebElement operations = driver.findElement(By.id("operations"));
				
				// find start cell
				key = operations.findElement(By.cssSelector(".td-key"));
				String ingest_download = "ingest-download";
				
				assertEquals(ingest_download, key.getText());
			}	
		}
		
		driver.quit();	
	}
	
//	@Test
	// TODO
	public void recordingsTabSortingPaging() {
		
		driver = login();
		
		// recordings table
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recordingsTable")));
		
		List<WebElement> elements = driver.findElements(By.cssSelector(".status-column-cell.ui-state-active"));
		
		WebElement sortTitle = driver.findElement(By.id("sortTitle"));
		sortTitle.click();
		
	}
	
	
	@Test
	public void recordingsTabAddAndDeleteSingleRecording() {
		
		driver = login();
		
		// add a single recording
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("scheduleButton")));
		
		WebElement scheduleBtn = driver.findElement(By.id("scheduleButton"));
		scheduleBtn.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("common-data")));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i18n_dist_label")));
		
		driver.findElement(By.id("title")).sendKeys(this.recordingName);
		driver.findElement(By.id("creator")).sendKeys(this.authorName);
		
		driver.findElement(By.id("i18n_additional")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contributor")));
		
		driver.findElement(By.id("contributor")).sendKeys(this.contributor);
		driver.findElement(By.id("subject")).sendKeys(this.subject);
		driver.findElement(By.id("language")).sendKeys(this.language);
		driver.findElement(By.id("description")).sendKeys(this.description);
		driver.findElement(By.id("copyright")).sendKeys(this.authorName);
		driver.findElement(By.id("durationHour")).sendKeys(this.durationHours + "");
		driver.findElement(By.id("durationMin")).sendKeys(this.durationMins + "");
		driver.findElement(By.id("startTimeHour")).sendKeys(this.startTimeHours);
		driver.findElement(By.id("startTimeMin")).sendKeys(this.startTimeMins);
		driver.findElement(By.id("agent")).sendKeys(this.agent);
		
		WebElement startDateFiled = driver.findElement(By.id("startDate"));
		startDateFiled.clear();
		startDateFiled.sendKeys(this.startDate);
		
		driver.findElement(By.id("submitButton")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content-metadata")));
		
		// test for presence
		String title = driver.findElement(By.id("field-title")).findElement(By.className("fieldValue")).getText();
		assertEquals(title, this.recordingName);
		
		String fieldCreator = driver.findElement(By.id("field-creator")).findElement(By.className("fieldValue")).getText();
		assertEquals(fieldCreator, this.authorName);
		
		String fieldContributor = driver.findElement(By.id("field-contributor")).findElement(By.className("fieldValue")).getText();
		assertEquals(fieldContributor, this.contributor);
		
		String fieldSubject = driver.findElement(By.id("field-subject")).findElement(By.className("fieldValue")).getText();
		assertEquals(fieldSubject, this.subject);
		
		String fieldLanguage = driver.findElement(By.id("field-language")).findElement(By.className("fieldValue")).getText();
		assertEquals(fieldLanguage, this.language);
		
		String fieldDescripton = driver.findElement(By.id("field-description")).findElement(By.className("fieldValue")).getText();
		assertEquals(fieldDescripton, this.description);

		driver.findElement(By.id("back_to_recordings")).click();
		
		// find the recording
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBox")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recordingsTable")));
		
		driver.findElement(By.cssSelector(".searchbox-text-input.ui-corner-all")).sendKeys(this.recordingName);
		driver.findElement(By.cssSelector(".searchbox-search-icon.ui-icon.ui-icon-search")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recordingsTable")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filterRecordingCount")));
		
		int foundRecs = Integer.parseInt(driver.findElement(By.id("filterRecordingCount")).getText().split(" ")[0]);
		
		assertTrue(foundRecs > 0);
		
		// delete the recording
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Delete")));
		WebElement deleteLink = driver.findElement(By.linkText("Delete"));
		
		assertNotNull(deleteLink);
		
		deleteLink.click();
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
		
		deleteLink = driver.findElement(By.linkText("Delete"));
	
		deleteLink.click();
		alert = driver.switchTo().alert();
		alert.accept();
				
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBox")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recordingsTable")));
		
		WebElement searchInput = driver.findElement(By.cssSelector(".searchbox-text-input.ui-corner-all"));
		searchInput.clear();
		searchInput.sendKeys(this.recordingName);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".searchbox-search-icon.ui-icon.ui-icon-search")));
		driver.findElement(By.cssSelector(".searchbox-search-icon.ui-icon.ui-icon-search")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recordingsTable")));
		
		String found = driver.findElement(By.id("filterRecordingCount")).getText();
		
		assertEquals(found, this.notFound);
		
		driver.quit();
	}
	
	
	private WebDriver login() {
		
		WebDriver driver = new FirefoxDriver();
		
		driver.get(baseUrl);
		
		WebElement username = driver.findElement(By.name("j_username"));
		WebElement password = driver.findElement(By.name("j_password"));
		
		username.sendKeys("admin");
		password.sendKeys("opencast");
		
		WebElement logInLink = driver.findElement(By.name("submit"));
		
		logInLink.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("adminlink")));
		
		WebElement adminLink = driver.findElement(By.id("adminlink"));
		
		adminLink.click();
		
		return driver;
		
	}
}
