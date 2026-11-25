package com.example.demo.actuator;

import com.example.demo.config.DataSourceProperties;
import com.example.demo.reader.DataReader;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceInfoContributor implements InfoContributor {

    private final DataSourceProperties properties;
    private final DataReader dataReader;

    public DataSourceInfoContributor(DataSourceProperties properties, DataReader dataReader) {
        this.properties = properties;
        this.dataReader = dataReader;
    }

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = new HashMap<>();
        details.put("filePath", properties.getFilePath());
        details.put("format", properties.getFormat());
        details.put("recordsLoaded", dataReader.getLoadedRecordsCount());
        details.put("activeReaderBean", dataReader.getClass().getSimpleName());

        builder.withDetail("customDataSource", details);
    }
}
