    package org.recursomechon.salaslibre.automation;

    import org.openqa.selenium.*;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.time.Duration;
    import java.util.List;

    public class ScheduleProcessor_respaldo {
        private final WebDriver driver;

        public ScheduleProcessor_respaldo(WebDriver driver) {

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
                            collectData();
                            break;
                        }

                    }
                    Thread.sleep(100);
                    driver.close();
                    driver.switchTo().window(originalWindow);
                    driver.switchTo().frame("frame3");

                }
                catch(Exception e){
                    System.out.println("Error: " + e.getMessage());
                    loop = false;
                }
            }


        }



        private void collectData(){
            try {
                new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("cuerpo"));

                WebElement table = new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@class='letra8']")));

                List<WebElement> tableRows = table.findElements(By.tagName("tr"));
                System.out.println("Number of rows found: " + tableRows.size());

                for (int i = 1; i < tableRows.size(); i++) {
                    WebElement row = tableRows.get(i);
                    List<WebElement> cells = row.findElements(By.tagName("td"));

                    String block1 = cells.get(0).findElement(By.xpath(".//table/tbody/tr[1]/td")).getText().trim();
                    String block2 = cells.get(0).findElement(By.xpath(".//table/tbody/tr[2]/td")).getText().trim();

                    for (int j = 2; j < cells.size()-2; j++) {
                        WebElement dayCell = cells.get(j);
                        String dayOfWeek = getDayOfWeek(j);

                        List<WebElement> innerTables = dayCell.findElements(By.tagName("table"));
                        if (!innerTables.isEmpty()) {
                            WebElement innerTable = innerTables.get(0);
                            List<WebElement> innerRows = innerTable.findElements(By.tagName("tr"));

                            // Check if there are two inner rows (indicating a class)
                            if (innerRows.size() == 2) {
                                // Extract information from the inner cells, handling potential missing elements
                                String info1 = "";
                                String info2 = "";
                                try {
                                    info1 = innerRows.get(0).findElement(By.tagName("td")).getText().trim();
                                    info2 = innerRows.get(1).findElement(By.tagName("td")).getText().trim();
                                } catch (NoSuchElementException e) {
                                    System.out.println("Inner cell not found for block " + block1 + "/" + block2 + " on " + getDayOfWeek(j));
                                }

                                // Check if the cell has relevant information (not empty and not &nbsp;)
                                if (!info1.isEmpty() && !info1.equals("&nbsp;")) {
                                    System.out.println("Bloque: " + block1 + "/" + block2 +
                                            ", Día: " + dayOfWeek +
                                            ", Info1: " + info1 +
                                            ", Info2: " + info2);

                                    // Store the class information in the database
                                    // ... your database insertion logic here ...
                                }
                            }
                        } else {
                            // No class during this block on this day
                            System.out.println("Bloque: " + block1 + "/" + block2 +
                                    ", Día: " + dayOfWeek + " -> Libre");

                            // Store the free block information in the database
                            // ... your database insertion logic here ...
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error collecting information: " + e.getMessage());
            }
        }
        private String getDayOfWeek(int j){
            String[] dayOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
            return dayOfWeek[j-2];
        }
    }
//        private void collectData(){
//            System.out.println("oo me estoy depurando \n oo me estoy depurando");
//            new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("cuerpo")) ;
//            System.out.println(driver.getPageSource());
//            WebElement table = new WebDriverWait(driver, Duration.ofSeconds(10))
//                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@class='letra8']")));
//
//            System.out.println(table.getAttribute("outerHTML"));
//            List<WebElement> tableRows = table.findElements(By.tagName("tr"));
//            System.out.println("Number of rows found: " + tableRows.size());
//            for (int i = 1; i < tableRows.size(); i++){
//                System.out.println("depuraooo loco estoy entero depuraoo");
//                WebElement row = tableRows.get(i);
//                List<WebElement> cells = row.findElements(By.tagName("td"));
//                WebElement blockCell = cells.get(0);
//                WebElement blockTable = blockCell.findElement(By.tagName("table"));
//                List<WebElement> blockRows = blockTable.findElements(By.tagName("tr"));
//                String block1 = blockRows.get(0).findElement(By.tagName("td")).getText().trim();
//                String block2 = blockRows.get(1).findElement(By.tagName("td")).getText().trim();
//                for (int j = 2; j < cells.size(); j++){
//                    WebElement dayCell= cells.get(j);
//                    WebElement dayTable = dayCell.findElement(By.tagName("table"));
//                    List<WebElement> dayRows = dayTable.findElements(By.tagName("tr"));
//
//                    WebElement firstRowCell = dayRows.get(0).findElement(By.tagName("td"));
//                    String cellText = firstRowCell.getText().trim();
//                    if (!cellText.isEmpty() && !cellText.equals("&nbsp;")){
//                        String info1 = dayRows.get(0).findElement(By.tagName("td")).getText().trim();
//                        String info2 = dayRows.get(1).findElement(By.tagName("td")).getText().trim();
//                        String dayOfWeek = getDayOfWeek(j);
//                        System.out.println("Bloque: " + block1 + "/" + block2 +
//                                ", Día: " + dayOfWeek +
//                                ", Info1: " + info1 +
//                                ", Info2: " + info2);
//                    }
//                }
//
//            }
//        }
