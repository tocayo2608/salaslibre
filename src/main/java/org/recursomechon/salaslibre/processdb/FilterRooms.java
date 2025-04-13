package org.recursomechon.salaslibre.processdb;

import java.sql.*;

public class FilterRooms {
    public static void main(String[] args) {
        // Cambiar estos valores según tu configuración
        String url = "jdbc:sqlite:courses.db";
        String querySelect = "SELECT id, room FROM Schedule";
        String queryUpdate = "UPDATE Schedule SET room = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement selectStmt = connection.prepareStatement(querySelect);
             PreparedStatement updateStmt = connection.prepareStatement(queryUpdate);
             ResultSet resultSet = selectStmt.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String room = resultSet.getString("room");

                if (room != null) {
                    // Solo tomar el contenido antes del primer salto de línea
                    String firstLine = room.split("\\R", 2)[0]; // \\R captura cualquier salto de línea
                    System.out.println("Updating ID: " + id + ", Room: " + firstLine);

                    // Actualizar el valor en la base de datos
                    updateStmt.setString(1, firstLine);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                }
            }

            System.out.println("Actualización completada.");
        } catch (SQLException e) {
            System.err.println("Error al acceder o actualizar la base de datos: " + e.getMessage());
        }
    }
}

