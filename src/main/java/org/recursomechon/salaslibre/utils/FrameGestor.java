package org.recursomechon.salaslibre.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FrameGestor {
    private final WebDriver driver;

    public FrameGestor(WebDriver driver) {
        this.driver = driver;
    }
    public void printAvailableFrames() {
        try {
            // Get a list of all frame elements on the page
            List<WebElement> frames = driver.findElements(By.tagName("frame"));

            // Print the names of the available frames
            System.out.println("Frames disponibles:");
            for (WebElement frame : frames) {
                String frameName = frame.getAttribute("name");
                if (frameName != null) {
                    System.out.println("- " + frameName);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los frames: " + e.getMessage());
        }
    }
}
