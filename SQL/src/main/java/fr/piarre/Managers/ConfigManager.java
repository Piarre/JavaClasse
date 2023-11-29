package fr.piarre.Managers;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class ConfigManager {
    public static void main(String[] args) {
        String yamlPath = "config.yml";

        try {
            Yaml yaml = new Yaml();

            try (FileInputStream fichierInputStream = new FileInputStream(yamlPath)) {
                Map<String, Object> donnees = yaml.load(fichierInputStream);

                System.out.println(donnees);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
