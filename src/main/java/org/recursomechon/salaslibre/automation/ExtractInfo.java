package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.recursomechon.salaslibre.utils.CredentialsReader;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class ExtractInfo {
    private static WebDriver driver;
    private String username;
    private String password;

    public ExtractInfo(WebDriver driver) throws IOException {
        this.driver = driver;
        Properties credentials = CredentialsReader.readCredentials("credentials.txt");
        this.username = credentials.getProperty("username");
        this.password = credentials.getProperty("password");
    }

    public void LoginSiga(){
        driver.get("https://siga.usm.cl/pag/home.jsp");

        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='login'and @type='TEXT']")));
            usernameField.sendKeys(username);
            WebElement passwordField = driver.findElement(By.xpath("//input[@name='passwd' and @type='password']"));
            passwordField.sendKeys(password);

            WebElement loginbutton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='Ingresar']")));
            loginbutton.click();
        }
        catch(Exception e){
            System.out.println("Error durante el Login: " + e.getMessage());
        }


    }
}
