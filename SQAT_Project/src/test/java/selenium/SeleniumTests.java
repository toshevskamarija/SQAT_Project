package selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.testng.Assert.*;

public class SeleniumTests {
    private WebDriver driver;

    @BeforeTest
    public void setup() {
        driver = getDriver();
    }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Marija\\Downloads\\SQAT_Project\\src\\main\\resources\\drivers\\chromedriver.exe");
        return new ChromeDriver();
    }


    //check loading pages
    //1
    @Test
    public void testLoadNew() throws InterruptedException {
        FluentWait wait = new FluentWait(driver);
        driver.get("https://cityfashion.mk/novo/");
        wait.until(ExpectedConditions.titleIs("Ново"));

    }
    //2
    @Test
    public void testLoadHomePage() throws InterruptedException {
        FluentWait wait = new FluentWait(driver);
        driver.get("https://cityfashion.mk/");
        wait.until(ExpectedConditions.titleIs("City Fashion"));

    }
    //3
    @Test
    public void testLoadCategory() throws InterruptedException {
        FluentWait wait = new FluentWait(driver);
        driver.get("https://cityfashion.mk/");
        sleep(1000);
        final WebElement brand = driver.findElement(By.xpath("//li[@id='mega-menu-item-8153']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(brand).build().perform();
        sleep(1000);
        final WebElement category = driver.findElement(By.xpath("//li[@id='mega-menu-item-12672']"));
        category.click();
        assertEquals("https://cityfashion.mk/?product_cat=mazi&brend=karl-lagerfeld", driver.getCurrentUrl());

    }
    //4
    @Test
    public void testLoadLogin() throws InterruptedException {
        FluentWait wait = new FluentWait(driver);
        driver.get("https://cityfashion.mk/");
        sleep(2000);
        final WebElement signIn = driver.findElement(By.id("loginShowModal"));
        signIn.click();
        sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("login")));

    }

    //check authentication
    //5
    @Test
    public void LoginSucessfulTest() throws InterruptedException {
        testLoadLogin();
        final WebElement emailField = driver.findElement(By.id("username"));
        final WebElement passwordField = driver.findElement(By.name("password"));
        final WebElement loginButton = driver.findElement(By.name("login"));
        emailField.sendKeys("marijatosevska@yahoo.com");
        passwordField.sendKeys("12345678");
        loginButton.click();
        sleep(1000);
        //new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.recaptcha-checkbox-checkmark"))).click();
        //final WebElement submit = driver.findElement(By.linkText("SUBMIT"));
        //submit.click();
        assertEquals("https://cityfashion.mk/", driver.getCurrentUrl());

    }
    //6
    @Test
    public void InvalidCredentialsTest() throws InterruptedException {
        testLoadLogin();
        final WebElement emailField = driver.findElement(By.id("username"));
        final WebElement passwordField = driver.findElement(By.name("password"));
        final WebElement loginButton = driver.findElement(By.name("login"));
        emailField.sendKeys("marijatosevska@yahoo.com");
        passwordField.sendKeys("12345");
        loginButton.click();
        sleep(2000);
        String message = "Грешка: Внесената лозинка за marijatosevska@yahoo.com не е точна. Ја изгубивте лозинката?";
        assertEquals(message, driver.findElement(By.xpath("//ul[@class='woocommerce-error']")).getText());
    }
    //7
    @Test
    public void EmptyInputTest() throws InterruptedException {
        testLoadLogin();
        final WebElement loginButton= driver.findElement(By.name("login"));
        loginButton.click();
        sleep(1000);
        String message = "Error: Потребно е корисничко име.";
        assertEquals(message, driver.findElement(By.xpath("//ul[@class='woocommerce-error']")).getText());
    }
    //8
    @Test
    public void EmptyPasswordTest() throws InterruptedException {
        testLoadLogin();
        final WebElement emailField = driver.findElement(By.id("username"));
        final WebElement loginButton= driver.findElement(By.name("login"));
        emailField.sendKeys("marijatosevska@yahoo.com");
        loginButton.click();
        sleep(1000);
        String message = "Грешка: Полето за лозинка е празно.";
        assertEquals(message, driver.findElement(By.xpath("//ul[@class='woocommerce-error']")).getText());
    }
    //9
    @Test
    public void InvalidEmailAddress() throws InterruptedException {
        testLoadLogin();
        final WebElement emailField = driver.findElement(By.id("username"));
        final WebElement passwordField = driver.findElement(By.name("password"));
        final WebElement loginButton = driver.findElement(By.name("login"));
        emailField.sendKeys("marijatosevska@");
        passwordField.sendKeys("12345678");
        loginButton.click();
        sleep(1000);
        String message = "Error: The username marijatosevska@ is not registered on this site. If you are unsure of your username, try your email address instead.";
        assertEquals(message, driver.findElement(By.xpath("//ul[@class='woocommerce-error']")).getText());
    }

    //check search field
    //10
    @Test
    public void Search() throws InterruptedException {
        driver.get("https://cityfashion.mk/");
        final WebElement searchIcon= driver.findElement(By.id("button-search"));
        searchIcon.click();
        final WebElement searchField = driver.findElement(By.className("search-field"));
        searchField.sendKeys("Ж капи");
        searchField.sendKeys(Keys.ENTER);
        sleep(1000);
        assertEquals("https://cityfashion.mk/product/karl-lagerfeld-kapi-20/", driver.getCurrentUrl());

    }
    //11 this test case contains a bug because the page displays 'critical error'.
    @Test
    public void EmptySearch() throws InterruptedException {
        driver.get("https://cityfashion.mk/");
        final WebElement searchIcon= driver.findElement(By.id("button-search"));
        searchIcon.click();
        final WebElement searchField = driver.findElement(By.className("search-field"));
        searchField.sendKeys(Keys.ENTER);
        sleep(1000);
        assertEquals("https://cityfashion.mk/?s=&post_type=product", driver.getCurrentUrl());
    }
    //12
    @Test
    public void NoResultsSearch() throws InterruptedException {
        driver.get("http://automationpractice.com/index.php");
        final WebElement searchField = driver.findElement(By.className("search_query"));
        final WebElement searchButton= driver.findElement(By.name("submit_search"));
        String searchInput="abc";
        searchField.sendKeys(searchInput);
        searchButton.click();
        sleep(2000);
        String textMessage=("No results were found for your search \"" +searchInput+'\"');
        WebElement t=driver.findElement(By.xpath("//p[@class='alert alert-warning']"));
        assertEquals(t.getText(), textMessage);
        String s = t.getCssValue("background-color");
        String c = Color.fromString(s).asHex();
        assertEquals(c,"#fe9126");
    }

    //check Cart

    //add item to cart
    //13
    @Test
    public void AddToCart() throws InterruptedException {
        LoginSucessfulTest();
        sleep(2000);
        final WebElement zatvori = driver.findElement(By.linkText("ПРОДОЛЖИ СО КУПУВАЊЕ"));
        zatvori.click();
        sleep(1000);
        final WebElement women = driver.findElement(By.id("mega-menu-item-8252"));
        Actions actions = new Actions(driver);
        actions.moveToElement(women).build().perform();
        sleep(1000);
        final WebElement skirts = driver.findElement(By.linkText("Здолништа"));
        skirts.click();
        //sleep(1000);
        final WebElement selectItem = driver.findElement(By.id("product-image68135"));
        Actions action = new Actions(driver);
        action.moveToElement(selectItem).build().perform();
        sleep(1000);
        final WebElement addToFavorites = driver.findElement(By.linkText("ВИДИ ПОВЕЌЕ"));
        addToFavorites.click();
        //sleep(1000);
        final WebElement size=driver.findElement(By.xpath("//li[@title='IT40']"));
        size.click();
        //sleep(1000);
        final WebElement addToCart = driver.findElement(By.xpath("//button[@class='single_add_to_cart_button button alt']"));
        addToCart.click();
        WebElement t=driver.findElement(By.xpath("//div[@class='woocommerce-message']"));
        String textMessage=("Види кошничка\n" + "Производот “VERSACE JEANS COUTURE – Ж Здолништа” е додаден во Вашата кошничка.");
        assertEquals(t.getText(), textMessage);
        String s = t.getCssValue("background-color");
        String c = Color.fromString(s).asHex();
        assertEquals(c,"#0f834d");
    }

    //add same item to cart again which has 1 item in stock
    //14
    @Test
    public void AddToCartAgain() throws InterruptedException {
        LoginSucessfulTest();
        sleep(2000);
        final WebElement zatvori = driver.findElement(By.linkText("ПРОДОЛЖИ СО КУПУВАЊЕ"));
        zatvori.click();
        sleep(1000);
        final WebElement women = driver.findElement(By.id("mega-menu-item-8252"));
        Actions actions = new Actions(driver);
        actions.moveToElement(women).build().perform();
        sleep(1000);
        final WebElement skirts = driver.findElement(By.linkText("Здолништа"));
        skirts.click();
        final WebElement selectItem = driver.findElement(By.id("product-image68135"));
        Actions action = new Actions(driver);
        action.moveToElement(selectItem).build().perform();
        sleep(1000);
        final WebElement addToFavorites = driver.findElement(By.linkText("ВИДИ ПОВЕЌЕ"));
        addToFavorites.click();
        driver.get("https://cityfashion.mk/product/versace-jeans-couture-zh-zdolnishta-2/");
        final WebElement size=driver.findElement(By.xpath("//li[@title='IT40']"));
        size.click();
        //sleep(1000);
        final WebElement addToCart = driver.findElement(By.xpath("//button[@class='single_add_to_cart_button button alt']"));
        addToCart.click();
        WebElement t=driver.findElement(By.xpath("//ul[@class='woocommerce-error']"));
        String textMessage=("Види кошничка\n" + "You cannot add that amount to the cart — we have 1 in stock and you already have 1 in your cart.");
        assertEquals(t.getText(), textMessage);
        String s = t.getCssValue("background-color");
        String c = Color.fromString(s).asHex();
        assertEquals(c,"#e2401c");
    }

    //delete element from shopping cart
    //15
    @Test
    public void DeleteItemFromCart() throws InterruptedException {
        LoginSucessfulTest();
        sleep(2000);
        final WebElement zatvori = driver.findElement(By.linkText("ПРОДОЛЖИ СО КУПУВАЊЕ"));
        zatvori.click();
        sleep(1000);
        driver.get("https://cityfashion.mk/cart/");
        List<WebElement> rows = driver.findElements(By.className("product-name"));
        Optional<WebElement> name= rows.stream().findFirst();
        List<WebElement> deleteIcons = driver.findElements(By.className("remove"));
        final WebElement deleteIcon= deleteIcons.get(0);
        deleteIcon.click();
        sleep(3000);
        for(int i=0; i<rows.size(); i++){
            if(rows.contains(name)){
                fail();
            }
        }
    }
    //16
    @Test
    public void FilterItems() throws InterruptedException {
        LoginSucessfulTest();
        sleep(2000);
        final WebElement zatvori = driver.findElement(By.linkText("ПРОДОЛЖИ СО КУПУВАЊЕ"));
        zatvori.click();
        sleep(1000);
        final WebElement women = driver.findElement(By.id("mega-menu-item-8252"));
        Actions actions = new Actions(driver);
        actions.moveToElement(women).build().perform();
        sleep(1000);
        final WebElement kaputi = driver.findElement(By.linkText("Капути"));
        kaputi.click();
        final WebElement boja = driver.findElement(By.xpath("(//span[@class='anchor'])[4]"));
        boja.click();
        sleep(1000);
        final WebElement crna = driver.findElement(By.xpath("//label[contains(text(), 'Црна')]"));
        crna.click();
        assertEquals("https://cityfashion.mk/?product_cat=kaputi&pa_boja=crna", driver.getCurrentUrl());

    }

    @AfterTest
    public void teardown(){
        driver.quit();
    }

}

