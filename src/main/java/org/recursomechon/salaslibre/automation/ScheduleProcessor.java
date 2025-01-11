package org.recursomechon.salaslibre.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.recursomechon.salaslibre.utils.Course;
import org.recursomechon.salaslibre.utils.CourseDAO;
import org.recursomechon.salaslibre.utils.FrameGestor;

import java.time.Duration;
import java.util.*;

import static org.recursomechon.salaslibre.utils.StringUtils.*;

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
        TabMethod(action, driver);
    }

    static void TabMethod(Actions action, WebDriver driver) throws InterruptedException {
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


//            Scanner scanner = new Scanner(System.in);

            System.out.println(".................");

//            scanner.nextLine();




        } catch (Exception e) {
            System.out.println("Error collecting information: " + e.getMessage());
        }
    }
    private void LocateOrange() {
        FrameGestor frameGestor = new FrameGestor(driver);
        List<String> local_list = new ArrayList<>();
        List<String> block_list = new ArrayList<>();

        ///////
        WebElement WebCampus = driver.findElement(By.xpath("//*[@id=\"msg\"]/center/table[1]/tbody/tr[1]/td[3]/font"));
        String CampusText = WebCampus.getText();

        WebElement WebAsignatura = driver.findElement(By.xpath("//*[@id=\"msg\"]/center/table[1]/tbody/tr[2]/td[3]/font"));
        String infoLocalText = WebAsignatura.getText();

        WebElement WebProfesor = driver.findElement(By.xpath("//*[@id=\"msg\"]/center/table[1]/tbody/tr[3]/td[3]/font"));
        String ProfesorText = WebProfesor.getText();

        WebElement WebParalelo = driver.findElement(By.xpath("//*[@id=\"msg\"]/center/table[1]/tbody/tr[2]/td[5]/font"));
        String auxParalelo = WebParalelo.getText();
        int ParaleloNumber = getNumber(auxParalelo);

        WebElement WebJornada = driver.findElement(By.xpath("//*[@id=\"msg\"]/center/table[1]/tbody/tr[1]/td[5]/font"));
        String JornadaText = WebJornada.getText();

        WebElement WebCreditos = driver.findElement(By.xpath("//*[@id=\"msg\"]/center/table[1]/tbody/tr[3]/td[5]/font"));
        String auxCreditos = WebCreditos.getText();
        int CreditosText = getNumber(auxCreditos);

        /////////
/*Test
        System.out.println(CampusText);
        System.out.println(ProfesorText);
        System.out.println("Paralelo: " + ParaleloNumber + "");
        System.out.println(JornadaText);
        System.out.println(CreditosText);
        System.out.println(infoLocalText);
*/
        //////
        Map<String, Map<String, String>> horario = new HashMap<>();
        Course course = new Course(CampusText, infoLocalText, ProfesorText, JornadaText, ParaleloNumber, CreditosText, horario);
        WebElement tablaPrincipal = driver.findElement(By.xpath("//*[@id='msg']/center/table[2]"));

        List<WebElement> filas = tablaPrincipal.findElements(By.tagName("tr"));
        List<String> listAux = new ArrayList<>();
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

                                String xpathFont = (String) ((JavascriptExecutor) driver).executeScript(
                                    "function getElementXPath(elt) {" +
                                    "  var path = '';" +
                                    "  for (; elt && elt.nodeType == 1; elt = elt.parentNode) {" +
                                    "    idx = Array.from(elt.parentNode.childNodes).filter(n => n.nodeName === elt.nodeName).indexOf(elt) + 1;" +
                                    "    xname = elt.nodeName.toLowerCase();" +
                                    "    path = '/' + xname + '[' + idx + ']' + path;" +
                                    "  }" +
                                    "  return path;" +
                                    "}" +
                                    "return getElementXPath(arguments[0]);", font);

                                String textoFF9900 = font.getText().trim();
                                String bloque = getBlock(textoRelacionado);
                                String sala = getSala(textoFF9900);

/*                                      TEST
                                System.out.println("día: " + getDay(extractTdNumber(xpathFont)));
                                System.out.println("bloque: " + bloque + " en la sala: " + sala);
*/
                                Map<String, String> LocalInfo = new HashMap<>();
                                LocalInfo.put(bloque, sala);
                                course.addHorario(getDay(extractTdNumber(xpathFont)), LocalInfo);


                            } catch (Exception e) {
                                continue;
                            }
                        }

                    }

                }

            }
        }
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.createTablesIfNotExist();
        courseDAO.saveCourse(course);
    }

}










