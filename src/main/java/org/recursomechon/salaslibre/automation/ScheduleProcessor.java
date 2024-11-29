package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ScheduleProcessor {
    private static WebDriver driver;

    public ScheduleProcessor(WebDriver driver) {
        this.driver = driver;
    }

    public void runProcessor(){
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame3")) ;
        String originalWindow = driver.getWindowHandle();
        Actions action = new Actions(driver);

        boolean loop= true;
        while(loop){
            try{
                action.sendKeys(Keys.TAB).perform();
                Thread.sleep(200);
                action.sendKeys(Keys.ENTER).perform();
                Thread.sleep(1000);
                driver.switchTo().defaultContent();

                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.numberOfWindowsToBe(2));
                for(String windowHandle : driver.getWindowHandles()){
                    if (!windowHandle.equals(originalWindow)){
                        driver.switchTo().window(windowHandle);
                        break;
                    }
                    Thread.sleep(100);
                    driver.close();
                    driver.switchTo().window(originalWindow);
                    driver.switchTo().frame("frame3");
                }


            }
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());
                loop = false;
            }
        }


    }


}
