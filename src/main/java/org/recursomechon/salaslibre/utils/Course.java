package org.recursomechon.salaslibre.utils;

public class Course {

    private String campus;
    private String name;
    private String profesor;
    private String jornada;
    private int paralelo;
    private int creditos;

    public Course(String campus,
                  String name,
                  String profesor,
                  String jornada,
                  int paralelo,
                  int creditos) {
        this.campus = campus;
        this.name = name;
        this.profesor = profesor;
        this.jornada = jornada;
        this.paralelo = paralelo;
        this.creditos = creditos;
    }
}
