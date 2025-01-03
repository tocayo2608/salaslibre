package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.recursomechon.salaslibre.utils.FrameGestor;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class ScheduleProcessorRespaldo {
    private final WebDriver driver;

    public ScheduleProcessorRespaldo(WebDriver driver) {

        this.driver = driver;
    }

    public void runProcessor(){
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame3")) ;
        String originalWindow = driver.getWindowHandle();
        Actions action = new Actions(driver);

        boolean loop= true;
        while(loop){
            try{
                TabPerform(action, driver);
                for(String windowHandle : driver.getWindowHandles()){
                    if (!windowHandle.equals(originalWindow)){
                        driver.switchTo().window(windowHandle);
                        collectData();
                        driver.close();
                        break;
                    }
                }

                Thread.sleep(100);
                driver.switchTo().window(originalWindow);
                driver.switchTo().frame("frame3");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                loop = false;
            }
        }
    }

    static void TabPerform(Actions action, WebDriver driver) throws InterruptedException {
        action.sendKeys(Keys.TAB).perform();
        Thread.sleep(200);
        action.sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);
        driver.switchTo().defaultContent();

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.numberOfWindowsToBe(2));
    }

    private void collectData(){
        try{
            FrameGestor frameGestor = new FrameGestor(driver);
            frameGestor.switchTo("cuerpo");
            LocateOrange();


            Scanner scanner = new Scanner(System.in);
            System.out.println("Continuar...");
            scanner.nextLine();


        } catch (Exception e) {
            System.out.println("Error collecting information: " + e.getMessage());
        }
    }
    private void LocateOrange() {
        FrameGestor frameGestor = new FrameGestor(driver);
        WebElement tablaPrincipal = driver.findElement(By.xpath("//*[@id='msg']/center/table[2]"));
        System.out.println("");
        List<WebElement> filas = tablaPrincipal.findElements(By.tagName("tr"));
        for (WebElement fila : filas) {
            List<WebElement> celdas = fila.findElements(By.tagName("td"));

            for (WebElement celda : celdas) {
                WebElement tablaInterna;
                try {
                    tablaInterna = celda.findElement(By.className("letra7"));
                } catch (Exception e) {
                    continue;
                }

                List<WebElement> filasInternas = tablaInterna.findElements(By.tagName("tr"));
                for (WebElement filaInterna : filasInternas) {
                    List<WebElement> tdsInternos = filaInterna.findElements(By.tagName("td"));
                    for (WebElement tdInterno : tdsInternos) {
                        String colorFondo = tdInterno.getAttribute("bgcolor");

                        if ("#FF9900".equalsIgnoreCase(colorFondo)) {
                            try {
                                WebElement font = tdInterno.findElement(By.tagName("font"));
                                String texto = font.getText().trim();
                                System.out.println("Celda encontrada: " + texto);
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
}