package org.recursomechon.salaslibre.processdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CleanDatabase {
    public static void main(String[] args) throws Exception {
        trimDatabase();
}

private static void cleanDatabase(Connection connection) throws Exception {
    String url = "jdbc:sqlite:courses.db";

    String query = "UPDATE Schedule " +
            "SET room = TRIM(SUBSTR(room, 1, INSTR(room || ',', ',') - 1)) " +
            "WHERE room LIKE 'Sala%';";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement()) {

        int rowsAffected = stmt.executeUpdate(query);
        System.out.println("Registros actualizados: " + rowsAffected);

    } catch (Exception e) {
        e.printStackTrace();
        }
    }
private static void trimDatabase() throws Exception {
    String url = "jdbc:sqlite:courses.db";

    // Consulta SQL para eliminar espacios en blanco finales
    String query = "UPDATE Schedule " +
            "SET room = RTRIM(room);";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement()) {

        // Ejecutar la actualización
        int rowsAffected = stmt.executeUpdate(query);
        System.out.println("Registros actualizados: " + rowsAffected);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}


