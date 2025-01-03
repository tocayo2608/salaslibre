package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.recursomechon.salaslibre.utils.FrameGestor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScheduleProcessor {
    private final WebDriver driver;

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

        // Localizar la tabla principal
        WebElement tablaPrincipal = driver.findElement(By.xpath("//*[@id='msg']/center/table[2]"));

        // Listas para días y bloques horarios
        List<String> dias = List.of("pass", "pass1", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        List<String> bloques = List.of("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"); // Personaliza según los bloques reales

        // Contadores
        int contFila = 0;

        // Obtener todas las filas de la tabla principal
        List<WebElement> filas = tablaPrincipal.findElements(By.tagName("tr"));

        // Iterar por las filas de la tabla principal
        for (WebElement fila : filas) {
            contFila++; // Incrementar el contador de filas

            // Obtener todas las celdas (columnas) de la fila actual
            List<WebElement> celdas = fila.findElements(By.tagName("td"));
            int contColumna = 0; // Reiniciar contador de columnas por fila

            for (WebElement celda : celdas) {
                contColumna++; // Incrementar el contador de columnas

                // Verificar si la celda tiene una tabla interna con clase "letra7"
                WebElement tablaInterna;
                try {
                    tablaInterna = celda.findElement(By.className("letra7"));
                } catch (Exception e) {
                    continue; // Si no tiene tabla interna, pasar a la siguiente celda
                }

                // Obtener filas internas de la tabla dentro de la celda
                List<WebElement> filasInternas = tablaInterna.findElements(By.tagName("tr"));
                for (WebElement filaInterna : filasInternas) {
                    // Obtener las celdas internas (td) de la fila interna
                    List<WebElement> tdsInternos = filaInterna.findElements(By.tagName("td"));
                    for (WebElement tdInterno : tdsInternos) {
                        // Verificar el color de fondo de la celda interna
                        String colorFondo = tdInterno.getAttribute("bgcolor");
                        if ("#FF9900".equalsIgnoreCase(colorFondo)) {
                            try {
                                // Extraer el texto de la celda interna
                                WebElement font = tdInterno.findElement(By.tagName("font"));
                                String texto = font.getText().trim();

                                // Determinar el día (basado en el contador de columnas)
                                String dia = contColumna <= dias.size() ? dias.get(contColumna) : "Desconocido";

                                // Determinar el bloque horario (basado en el contador de filas)
                                String bloque = contFila <= bloques.size() ? bloques.get(contFila) : "Bloque desconocido";

                                // Imprimir información recolectada
                                System.out.println("Celda encontrada: " + texto);
                                System.out.println("  Día: " + dia);
                                System.out.println("  Bloque horario: " + bloque);
                                System.out.println("--------------------------------");
                            } catch (Exception e) {
                                continue; // Si ocurre un error, pasar a la siguiente celda
                            }
                        }
                    }
                }
            }
        }
    }

}