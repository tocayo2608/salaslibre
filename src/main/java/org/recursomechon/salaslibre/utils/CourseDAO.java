package org.recursomechon.salaslibre.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CourseDAO {

    // Dirección de la base de datos SQLite
    private final String dbUrl = "jdbc:sqlite:courses.db";

    // Crear conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    // Método para guardar un curso en la base de datos
    public void saveCourse(Course course) {
        String insertCourseSQL = "INSERT INTO Course (name, professor, jornada, paralelo, credits) VALUES (?, ?, ?, ?, ?)";
        String insertScheduleSQL = "INSERT INTO Schedule (course_id, day, block, room) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement courseStmt = connection.prepareStatement(insertCourseSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insertar curso
            courseStmt.setString(1, course.getName());
            courseStmt.setString(2, course.getProfesor());
            courseStmt.setString(3, course.getJornada());
            courseStmt.setInt(4, course.getParalelo());
            courseStmt.setInt(5, course.getCreditos());

            int affectedRows = courseStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el curso.");
            }

            // Obtener ID del curso
            int courseId;
            try (ResultSet generatedKeys = courseStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    courseId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del curso.");
                }
            }

            // Insertar horarios
            try (PreparedStatement scheduleStmt = connection.prepareStatement(insertScheduleSQL)) {
                for (Map.Entry<String, Map<String, String>> entry : course.getHorario().entrySet()) {
                    String dia = entry.getKey();
                    for (Map.Entry<String, String> scheduleEntry : entry.getValue().entrySet()) {
                        scheduleStmt.setInt(1, courseId);
                        scheduleStmt.setString(2, dia);
                        scheduleStmt.setString(3, scheduleEntry.getKey());
                        scheduleStmt.setString(4, scheduleEntry.getValue());
                        scheduleStmt.executeUpdate();
                    }
                }
            }

            System.out.println("Curso y horarios guardados correctamente en la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al guardar el curso en la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para crear tablas si no existen (puede llamarse en la inicialización)
    public void createTablesIfNotExist() {
        String createCourseTableSQL = "CREATE TABLE IF NOT EXISTS Course (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "professor TEXT NOT NULL," +
                "jornada TEXT NOT NULL," +
                "paralelo INTEGER NOT NULL," +
                "credits INTEGER NOT NULL" +
                ");";

        String createScheduleTableSQL = "CREATE TABLE IF NOT EXISTS Schedule (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "course_id INTEGER NOT NULL," +
                "day TEXT NOT NULL," +
                "block TEXT NOT NULL," +
                "room TEXT NOT NULL," +
                "FOREIGN KEY (course_id) REFERENCES Course (id)" +
                ");";

        try (Connection connection = getConnection()) {
            connection.createStatement().execute(createCourseTableSQL);
            connection.createStatement().execute(createScheduleTableSQL);
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas en la base de datos: " + e.getMessage());
        }
    }
}