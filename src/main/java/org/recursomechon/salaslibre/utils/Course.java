package org.recursomechon.salaslibre.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Course {

    private final String campus;
    private final String name;
    private final String profesor;
    private final String jornada;
    private final int paralelo;
    private final int creditos;
    private final Map<String, Map<String, String>> horario;

    // Constructor con validaciones
    public Course(String campus,
                  String name,
                  String profesor,
                  String jornada,
                  int paralelo,
                  int creditos,
                  Map<String, Map<String, String>> horario) {

        if (campus == null || campus.isEmpty()) throw new IllegalArgumentException("El campus no puede ser nulo o vacío.");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        if (profesor == null || profesor.isEmpty()) throw new IllegalArgumentException("El profesor no puede ser nulo o vacío.");
        if (jornada == null || jornada.isEmpty()) throw new IllegalArgumentException("La jornada no puede ser nula o vacía.");
        if (paralelo < 1) throw new IllegalArgumentException("El paralelo debe ser mayor o igual que 1.");
        if (creditos < 1) throw new IllegalArgumentException("Los créditos deben ser mayor o igual que 1.");

        this.campus = campus;
        this.name = name;
        this.profesor = profesor;
        this.jornada = jornada;
        this.paralelo = paralelo;
        this.creditos = creditos;
        this.horario = horario != null ? new HashMap<>(horario) : new HashMap<>();
    }

    // Método para agregar un horario, utiliza computeIfAbsent para simplificar lógica
    public void addHorario(String dia, Map<String, String> info) {
        if (dia == null || dia.isEmpty()) {
            throw new IllegalArgumentException("El día no puede ser nulo o vacío.");
        }
        if (info == null) {
            throw new IllegalArgumentException("La información del horario no puede ser nula.");
        }

        horario.computeIfAbsent(dia, k -> new HashMap<>()).putAll(info);
    }

    // Método para imprimir información completa del curso
    public void printInfo() {
        System.out.println("Información del Curso:");
        System.out.println("Campus: " + campus);
        System.out.println("Nombre: " + name);
        System.out.println("Profesor: " + profesor);
        System.out.println("Jornada: " + jornada);
        System.out.println("Paralelo: " + paralelo);
        System.out.println("Créditos: " + creditos);
        System.out.println("Horario:");
        printHorario();
    }

    // Método privado para imprimir horarios
    private void printHorario() {
        for (Map.Entry<String, Map<String, String>> dia : horario.entrySet()) {
            System.out.println("  Día: " + dia.getKey());
            for (Map.Entry<String, String> hora : dia.getValue().entrySet()) {
                System.out.println("    " + hora.getKey() + ": " + hora.getValue());
            }
        }
    }



    //getters


    public String getCampus() {
        return campus;
    }

    public String getName() {
        return name;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getJornada() {
        return jornada;
    }

    public int getParalelo() {
        return paralelo;
    }

    public int getCreditos() {
        return creditos;
    }

    public Map<String, Map<String, String>> getHorario() {
        return horario;
    }
}