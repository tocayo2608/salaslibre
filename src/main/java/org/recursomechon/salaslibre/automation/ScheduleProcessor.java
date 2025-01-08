package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.recursomechon.salaslibre.utils.FrameGestor;

import java.time.Duration;
import java.util.*;

public class ScheduleProcessor {
    private final WebDriver driver;
    Map<String, List<String>> information;
    public ScheduleProcessor(WebDriver driver) {

        this.driver = driver;
        this.information = new HashMap<>();
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
        List<String> local_list = new ArrayList<>();
        List<String> block_list = new ArrayList<>();

        ///////

        //////

        WebElement tablaPrincipal = driver.findElement(By.xpath("//*[@id='msg']/center/table[2]"));
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
                                WebElement trExterno = tdInterno.findElement(By.xpath("./ancestor::tr"));
                                WebElement primerTd = trExterno.findElement(By.xpath("./td[1]"));
                                String textoRelacionado = primerTd.getText().trim();
                                WebElement font = tdInterno.findElement(By.tagName("font"));
                                String textoFF9900 = font.getText().trim();
                                local_list.add(textoFF9900);
                                block_list.add(textoRelacionado);

                            } catch (Exception e) {
                                continue;
                            }
                        }

                    }
                }

            }
        }
    }
    private String getBlock(String number1, String number2){
        Integer local_number = Integer.valueOf(number1);
        if (local_number % 2 == 0){
            return String.format("{}-{}",number2, number1);
        }
        else{
            return String.format("{} -{}", number1, number2);
        }
    }

    private void getInformationAsignatura(){
        WebElement tablaPrincipal = driver.findElement(By.xpath("/center/table[1]"));

    }
}