package org.recursomechon.salaslibre.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CredentialsReader {
    public static Properties readCredentials(String file_path) throws IOException {
        Properties credentials = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
                credentials.load(reader);
            }
            return credentials;
        }
}
