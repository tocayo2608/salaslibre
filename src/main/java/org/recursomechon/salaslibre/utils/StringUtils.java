package org.recursomechon.salaslibre.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class StringUtils {

    public static int getNumber(String input) {
        // Expresión regular para encontrar un número en el String
        String numeroEncontrado = input.replaceAll("\\D+", "");
        return Integer.parseInt(numeroEncontrado);
    }

    public static String getBlock(String input) {
        return input.replace("\n", "-");
    }

    public static String getSala(String input) {
        // Busca la posición de la palabra "Sala"
        int indice = input.indexOf("Sala");
        if (indice != -1) {
            return input.substring(indice);
        }
        throw new IllegalArgumentException("Formato incorrecto");
    }

    public static String obtenerXPathAbsoluto(WebElement elemento) {
        StringBuilder xpath = new StringBuilder();

        // Recorremos desde el elemento actual hacia el nodo raíz
        while (elemento != null) {
            String tagName = elemento.getTagName();

            // Verifica si el elemento tiene un atributo "id"
            String id = elemento.getAttribute("id");
            if (id != null && !id.isEmpty()) {
                xpath.insert(0, "//*[@id='" + id + "']");
                break;
            }

            // Calcula el índice del elemento entre sus hermanos del mismo tipo
            int indice = obtenerIndiceEntreHermanos(elemento, tagName);

            // Construye el segmento del XPath para este nodo
            String segmento = tagName;
            if (indice > 1) {
                segmento += "[" + indice + "]";
            }

            // Inserta este segmento al inicio del XPath
            xpath.insert(0, "/" + segmento);

            // Subimos al elemento padre
            elemento = elemento.findElement(By.xpath(".."));
        }

        return xpath.toString();
    }

    private static int obtenerIndiceEntreHermanos(WebElement elemento, String tagName) {
        int indice = 1;

        try {
            // Recorre los hermanos anteriores para determinar el índice
            WebElement hermano = elemento.findElement(By.xpath("preceding-sibling::" + tagName));
            while (hermano != null) {
                indice++;
                hermano = hermano.findElement(By.xpath("preceding-sibling::" + tagName));
            }
        } catch (Exception e) {
            // No hay más hermanos anteriores
        }

        return indice;
    }
    public static int extractTdNumber(String xpath) {
        // Encuentra la posición de "td[" y extrae el número siguiente
        return Integer.parseInt(xpath.substring(xpath.indexOf("td[") + 3, xpath.indexOf("]", xpath.indexOf("td["))));
    }

    public static String getDay(int index){
        switch(index){
            case 3: return "Lunes";
            case 4: return "Martes";
            case 5: return "Miercoles";
            case 6: return "Jueves";
            case 7: return "Viernes";
            case 8: return "Sabado";
            case 9: return "Domingo";
            default: return "No definido";
        }
    }
}
