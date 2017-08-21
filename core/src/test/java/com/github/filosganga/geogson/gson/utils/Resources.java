package com.github.filosganga.geogson.gson.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public final class Resources {

    public static String readJson(String resource) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(resource)))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    private Resources(){

    }
}
