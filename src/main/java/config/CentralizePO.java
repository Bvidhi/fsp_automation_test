package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CentralizePO extends BaseTest{

    public CentralizePO(WebDriver driver) {
        super(driver);
        PageFactory.initElements(getDriver(), this);
    }


    public void clickOnWebElement(WebElement element){
        element.click();
    }

    public void elementIsDisplayed(WebElement element){
        element.isDisplayed();
    }

    public void verifyPageIsOpened(String pageName){
        getDriver().getCurrentUrl().contains(pageName);
    }

    public void enterValue(WebElement element, String value){
        element.sendKeys(value);
    }

    public String getValueFromFile(String value) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(System.getProperty("user.dir") + "/src/main/resources/loginDetails.properties"));
        return properties.getProperty(value);
    }
}
