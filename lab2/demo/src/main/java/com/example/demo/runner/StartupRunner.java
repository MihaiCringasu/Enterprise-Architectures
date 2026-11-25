package com.example.demo.runner;

import com.example.demo.config.DataSourceProperties;
import com.example.demo.reader.DataReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    private final DataReader dataReader;
    private final DataSourceProperties properties;

    public StartupRunner(DataReader dataReader, DataSourceProperties properties) {
        this.dataReader = dataReader;
        this.properties = properties;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== PORNIRE APLICATIE ===");
        System.out.println("Fisier configurat: " + properties.getFilePath() + " | Format: " + properties.getFormat());
        List<String> data = dataReader.readData();
        data.forEach(System.out::println);
        System.out.println("=========================");
    }
}
