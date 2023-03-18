package com.example.reteadesocializare.utils;

import com.example.reteadesocializare.exceptions.MyException;

import java.time.format.DateTimeFormatter;

public class Utils {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static void tryExecute(Runnable runnable) {
        try {
            runnable.run();
            System.out.println("Success!");
        } catch (MyException e) {
            System.err.println(e.getMessage());
        }
    }
}
