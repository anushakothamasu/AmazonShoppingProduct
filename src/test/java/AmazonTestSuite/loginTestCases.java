package AmazonTestSuite;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import AndroidSetUp.base;
import PageObjects.checkOutPage;
import PageObjects.itemDeliveryPage;
import PageObjects.loginPage;
import PageObjects.paymentPage;
import PageObjects.searchPage;
import PageObjects.dataProviderObjects;
import Resources.utilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import java.util.Properties;

public class loginTestCases extends base {
	private static final int AndroidDriver = 0;

	@BeforeTest
	public void killAllNodes() throws IOException, InterruptedException {
		Runtime.getRuntime().exec("taskkill /F /IM node.exe");
		Thread.sleep(3000);
	}

	Properties prop = new Properties();

	@Test(dataProvider = "InputData", dataProviderClass = dataProviderObjects.class)
	public void Amazonlogin(String emailText, String passwordText, String searchItemName, String altProduct,String objAddToCart, 
			String nameOnCard, String cardNumber, String actual, String expected ) throws IOException, InterruptedException

	{
		service = startServer();

		AndroidDriver<AndroidElement> driver = capabilities("apkName");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		utilities utils = new utilities(driver);
		// Login page
		loginPage loginPage = new loginPage(driver);
		searchPage srPage = new searchPage(driver);
		checkOutPage objChkOut = new checkOutPage(driver);
		itemDeliveryPage objDelivery = new itemDeliveryPage(driver);
		paymentPage objPayment = new paymentPage(driver);
		String objScrollText = prop.getProperty("Continue");
		
		// Check sign in user
		loginPage.clickcheckSignin();
		loginPage.enterEmail(emailText);
		loginPage.clickcontinue();
		loginPage.clickdisableShowPwd();
		loginPage.enterpassword(passwordText);

		// Click Signin
		loginPage.clickSignin();
		srPage.searchProduct(searchItemName);
		utils.ScrollItem(altProduct,driver);
		Thread.sleep(2000);
		srPage.clickTVSelection();
		String addedItem=altProduct;
		objChkOut.clickOnBuyingOptions(objAddToCart);
		objChkOut.clickOnCheckOutCart();
		String itemInCart=altProduct;
		objChkOut.clickOnProceddToCheckOut();
		objDelivery.clickOnDeliverToAddress();
		objDelivery.clickOnDeliveryContinue();

		objPayment.clickOnPlaceOrder();
		Assert.assertEquals(addedItem,itemInCart);

		service.stop();

	}
	
}
