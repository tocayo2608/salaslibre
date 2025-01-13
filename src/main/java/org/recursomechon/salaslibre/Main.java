package org.recursomechon.salaslibre;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.recursomechon.salaslibre.automation.ExtractInfo;
import org.recursomechon.salaslibre.automation.ScheduleProcessor;

import java.io.IOException;
import java.util.Scanner;

import static org.recursomechon.salaslibre.processdb.ScheduleManager.RunManager;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("""
                'Bienvenido a Salaslibre USM!
                Selecciona una opción:
                1. Extraer datos.
                2. Obtener info (Si es que ya hay datos extraídos.)
                """);
        Scanner sc = new Scanner(System.in);
        int election = sc.nextInt();
        switch (election) {
            case 1-> {
                try{
                    WebDriver driver = new ChromeDriver();
                    ExtractInfo extract = new ExtractInfo(driver);
                    ScheduleProcessor processor = new ScheduleProcessor(driver);
                    extract.LoginSiga();  //Login Siga
                    extract.navigateSiga();
                    extract.selectOptions();
                    processor.runProcessor();
                    driver.quit();
                    System.out.println("Información extraída exitosamente.");
                } catch (Exception e) {
                    System.out.println("No se logró iniciar sesión en Siga. Volviendo al menú principal...");
                }

            }
            case 2-> {
                RunManager();
            }
        }
    }
}