package com.example.demo.reader;

import com.example.demo.config.DataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@ConditionalOnExpression("'${app.datasource.format}'.equalsIgnoreCase('csv')")
public class CsvDataReader implements DataReader {

    private final DataSourceProperties properties;
    private final ResourceLoader resourceLoader;
    private int loadedCount = 0;

    public CsvDataReader(DataSourceProperties properties, ResourceLoader resourceLoader) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<String> readData() {
        List<String> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resourceLoader.getResource(properties.getFilePath()).getInputStream()))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < properties.getMaxRecords()) {
                records.add("[CSV] " + line);
                count++;
            }
            this.loadedCount = records.size();
        } catch (Exception e) {
            records.add("Eroare la citirea CSV: " + e.getMessage());
        }
        return records;
    }

    @Override
    public int getLoadedRecordsCount() {
        return loadedCount;
    }
}
