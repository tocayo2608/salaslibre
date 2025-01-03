package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.recursomechon.salaslibre.utils.CredentialsReader;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Scanner;

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

    public boolean LoginSiga(){
        driver.get("https://siga.usm.cl/pag/home.jsp");

        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='login'and @type='TEXT']")));
            usernameField.sendKeys(username);
            WebElement passwordField = driver.findElement(By.xpath("//input[@name='passwd' and @type='password']"));
            passwordField.sendKeys(password);

            WebElement loginbutton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='Ingresar']")));
            loginbutton.click();
            System.out.println("Login exitoso.");
            return true;
        }
        catch(Exception e){
            System.out.println("Error durante el Login: " + e.getMessage());
            return false;
        }
    }

    public void navigateSiga() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("dos")) ;
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("a[title='Consulte el horario de las asignaturas programadas en el semestre']")));
            element.click();
            System.out.println(driver.getPageSource());
        } catch (Exception e) {
            System.out.println("Error durante el navigation: " + e.getMessage());
        }
    }
    public void selectOptions(){
        try{
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame5")) ;
            WebElement options = new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("op")));
            options.click();
            Select select = new Select(options);

            select.selectByVisibleText("Todas las asignaturas");
            driver.switchTo().defaultContent();
        }
        catch (Exception e) {
            System.out.println("Error durante el navigation: " + e.getMessage());
        }
    }
}
