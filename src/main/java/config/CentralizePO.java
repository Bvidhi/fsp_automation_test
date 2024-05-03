package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CentralizePO extends BaseTest{

    public CentralizePO(WebDriver driver) {
        super(driver);
        PageFactory.initElements(getDriver(), this);
    }


    public boolean clickOnWebElement(WebElement webElement){
        WebElement element = new WebDriverWait(getDriver(),20L).until(ExpectedConditions.visibilityOf(webElement));
        element.click();
        return true;
    }

    public boolean elementIsDisplayed(WebElement element){
        element.isDisplayed();
        return true;
    }

    public boolean enterValue(WebElement element, String value){
        element.clear();
        element.sendKeys(value);
        return true;
    }

    public String getValueFromFile(String value) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(System.getProperty("user.dir") + "/src/main/resources/loginDetails.properties"));
        return properties.getProperty(value);
    }
}
