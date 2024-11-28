package org.recursomechon.salaslibre;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.recursomechon.salaslibre.automation.ExtractInfo;

import java.io.IOException;
import java.util.Scanner;

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
                WebDriver driver = new ChromeDriver();
                ExtractInfo extract = new ExtractInfo(driver);
                extract.LoginSiga();



                driver.quit();
            }
            case 2-> {
                System.out.println("Pendiente xd");
            }
        }
    }
}