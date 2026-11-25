package com.example.demo.reader;

import com.example.demo.config.DataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("prod")
@ConditionalOnExpression("'${app.datasource.format}'.equalsIgnoreCase('json')")
public class JsonDataReader implements DataReader {

    private final DataSourceProperties properties;
    private final ResourceLoader resourceLoader;
    private int loadedCount = 0;

    public JsonDataReader(DataSourceProperties properties, ResourceLoader resourceLoader) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<String> readData() {
        List<String> records = new ArrayList<>();
        try {
            String content = new String(
                    resourceLoader.getResource(properties.getFilePath()).getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (line.trim().startsWith("{") && records.size() < properties.getMaxRecords()) {
                    records.add("[JSON] " + line.trim());
                }
            }
            this.loadedCount = records.size();
        } catch (Exception e) {
            records.add("Eroare la citirea JSON: " + e.getMessage());
        }
        return records;
    }

    @Override
    public int getLoadedRecordsCount() {
        return loadedCount;
    }
}
