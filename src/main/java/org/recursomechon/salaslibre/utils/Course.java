package org.recursomechon.salaslibre.utils;

import java.util.List;
import java.util.Map;

public class Course {

    private String campus;
    private String name;
    private String profesor;
    private String jornada;
    private int paralelo;
    private int creditos;
    private Map<String,String> horario;


    public Course(String campus,
                  String name,
                  String profesor,
                  String jornada,
                  int paralelo,
                  int creditos,
                  Map<String, String> horario) {
        this.campus = campus;
        this.name = name;
        this.profesor = profesor;
        this.jornada = jornada;
        this.paralelo = paralelo;
        this.creditos = creditos;
        this.horario = horario;
    }


    public void addHorario(String dia, String bloque) {
        horario.put(dia, bloque );
    }

    public void printInfo(){
        System.out.println(String.format("Campus: %s", campus != null ? campus : "No definido"));
        System.out.println(String.format("Asignatura: %s", name != null ? name : "No definido"));
        System.out.println(String.format("Profesor: %s", profesor != null ? profesor : "No definido"));
        System.out.println(String.format("Jornada: %s", jornada != null ? jornada : "No definido"));
        System.out.println(String.format("Paralelo: %s", paralelo));
        System.out.println(String.format("Creditos: %d", creditos));

        System.out.println("Horario: ");
        if (horario != null && !horario.isEmpty()) {
            printHorario();
        } else {
            System.out.println("  No definido");
        }
    }

    private void printHorario() {
        for (Map.Entry<String, String> entry : horario.entrySet()) {
            System.out.println(String.format("  %s: %s", entry.getKey(), entry.getValue()));
        }
    }
}
