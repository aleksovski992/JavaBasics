package AutomationPractice;

import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class AutomationTests {
    //declare WebDriver
    public static WebDriver driver;

    //declare Wait WebDriver
    WebDriverWait wait;


    public String getSaltString() {
        String salchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * salchars.length());
            salt.append(salchars.charAt(index));
        }
        return salt.toString();
    }


    @BeforeTest
    public void navigateToPage() {
        BasicConfigurator.configure();

        //locate WebDriver from local machine - set system property
        System.setProperty("webdriver.chrome.driver", "C:\\Chrome Drivers\\ChromeDriver 96.0.4664.45\\chromedriver.exe");

        //initialize objects
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        //Resize current window to the set dimension
        driver.manage().window().maximize();

        //navigate WebDriver to the web page
        driver.get("http://automationpractice.com/index.php");



        //optional console output messages
        System.out.println("*****BeforeTest*****");
        System.out.println("Browser is successfully opened!");
        System.out.println("Navigation to the page was successful!");
        System.out.println("----------------------------------");
    }


    @Test(priority = 1)
    public void createAccount() {
        //click on the login button
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("login"))).click();
        //input email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_create"))).sendKeys(getSaltString() + "@scalefocus.com");
        //click on "Create account"
        driver.findElement(By.id("SubmitCreate")).click();
    }

    @Test(priority = 2)
    public void fillInfo() {
        //click on the Gender radio button
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uniform-id_gender1"))).click();
        //input name of the user
        driver.findElement(By.id("customer_firstname")).sendKeys("Aleksandar");
        //input surname of the user
        driver.findElement(By.id("customer_lastname")).sendKeys("Aleksovski");
        //input the password
        driver.findElement(By.id("passwd")).sendKeys("AceAce!1234");
        //select the day of the birth
        Select birthDay = new Select(driver.findElement(By.id("days")));
        birthDay.selectByValue("6");
        //select the month of the birth
        Select birthMonth = new Select(driver.findElement(By.id("months")));
        birthMonth.selectByValue("6");
        //select the year of the birth
        Select birthYear = new Select(driver.findElement(By.id("years")));
        birthYear.selectByValue("1992");
        //input Company name
        driver.findElement(By.id("company")).sendKeys("Scalefocus");
        //input Company address
        driver.findElement(By.id("address1")).sendKeys("Максим Горки 13, Скопје 1000");
        //input City
        driver.findElement(By.id("city")).sendKeys("Skopje");
        //select the state
        Select state = new Select(driver.findElement(By.id("id_state")));
        state.selectByValue("1");
        //input postcode
        driver.findElement(By.id("postcode")).sendKeys("10000");
        //input mobile phone
        driver.findElement(By.id("phone_mobile")).sendKeys("0038970123456");
        //click on "Register"
        driver.findElement(By.id("submitAccount")).click();
        //verify the user is logged
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("page-heading"))).getText();
        Assert.assertEquals("MY ACCOUNT", text);
    }

    @Test(priority = 3)
    public void checkCategories() {
        //navigate to home page
        driver.get("http://automationpractice.com/index.php");

        //verify the number of categories popular elements
        List<WebElement> categories = driver.findElements(By.cssSelector("ul#homefeatured li"));
        Assert.assertEquals(categories.size(), 7, "The number of the elements is incorrect");

        //click on the bestsellers tab
        driver.findElement(By.className("blockbestsellers")).click();

        //verify the number of bestsellers elements
        List<WebElement> bestSellers = driver.findElements(By.cssSelector("ul#blockbestsellers li"));
        Assert.assertEquals(bestSellers.size(), 7, "The number of the elements is incorrect");

        for(int i=0;i<3;i++) {
            //click on the i-th bestsellers element
            bestSellers.get(i).click();
            //click on the ADD button
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Add to cart"))).click();
            if(i<2)
                //click on Continue shopping
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[title='Continue shopping']"))).click();
            else
                //click on Proceed to checkout
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='http://automationpractice.com/index.php?controller=order']"))).click();
        }
    }

    @Test(priority = 4)
    public void checkout() {
        //click on Proceed to checkout
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='http://automationpractice.com/index.php?controller=order&step=1']"))).click();

        //click on Proceed to checkout on the Address tab
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[name='processAddress']"))).click();

        //click on the checkbox
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uniform-cgv"))).click();

        //click on Proceed to checkout on the Shipping tab
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[name='processCarrier']"))).click();

        //click on Pay by bank wire
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='Pay by bank wire']"))).click();

        //click on I confirm my order
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[3]/div/form/p/button"))).click();

        //check the displayed text for the finished order
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Your order on My Store is complete.']"))).isDisplayed();

        //close browser
        driver.quit();
    }
}