package org.recursomechon.salaslibre.processdb;

import java.sql.*;
import java.util.*;
import java.util.*;

public class ScheduleManager {

    public static void RunManager() {
        getAvailableRooms();
    }

    public static void getAvailableRooms() {
        String url = "jdbc:sqlite:Courses_Final.db";

        //////////////////
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el día: ");
        String day = scanner.nextLine().trim();
        System.out.print("Ingrese el bloque: ");
        String block = scanner.nextLine().trim();
        /// //////////////////
        String query =
                "SELECT DISTINCT room FROM Schedule " +
                "WHERE room NOT IN ( " +
                "    SELECT room FROM Schedule " +
                "    WHERE day = ? AND block = ? " +
                ")";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, day);
            statement.setString(2, block);


            ResultSet resultSet = statement.executeQuery();
            List<String> availableRooms = new ArrayList<>();

            while (resultSet.next()) {
                availableRooms.add(resultSet.getString("room"));
            }

            if (availableRooms.isEmpty()) {
                System.out.println("No hay salas desocupadas para el día y bloque especificados.");
            } else {
                System.out.println("Salas desocupadas en " + day + ", bloque " + block + ":");
                for (String room : availableRooms) {
                    System.out.println(room);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
