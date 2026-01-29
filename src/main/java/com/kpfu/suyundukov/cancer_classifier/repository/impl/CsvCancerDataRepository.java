package com.kpfu.suyundukov.cancer_classifier.repository.impl;

import com.kpfu.suyundukov.cancer_classifier.exception.DataLoadingException;
import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import com.kpfu.suyundukov.cancer_classifier.repository.CancerDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация репозитория, загружающая данные из CSV-файлов,
 * расположенных в ресурсах приложения.
 */
@Repository
public class CsvCancerDataRepository implements CancerDataRepository {

    private final String labelsPath;
    private final String dataPath;

    /**
     * Конструктор с внедрением путей к файлам из конфигурации.
     *
     * @param labelsPath Путь к файлу меток (из application.properties).
     * @param dataPath   Путь к файлу данных (из application.properties).
     */
    public CsvCancerDataRepository(@Value("${app.data.labels-path}") String labelsPath,
                                   @Value("${app.data.samples-path}") String dataPath) {
        this.labelsPath = labelsPath;
        this.dataPath = dataPath;
    }

    @Override
    public List<CancerSample> findAll() {
        Map<String, String> labelsMap = loadLabels();

        return loadSamples(labelsMap);
    }

    private Map<String, String> loadLabels() {
        Map<String, String> labelsMap = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream(labelsPath)) {
            if (is == null) throw new DataLoadingException("Файл меток не найден: " + labelsPath);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                    StandardCharsets.UTF_8))) {
                reader.readLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        labelsMap.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            throw new DataLoadingException("Ошибка чтения файла меток", e);
        }
        return labelsMap;
    }

    private List<CancerSample> loadSamples(Map<String, String> labelsMap) {
        List<CancerSample> samples = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(dataPath)) {
            if (is == null) throw new DataLoadingException("Файл данных не найден: " + dataPath);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                    StandardCharsets.UTF_8))) {
                reader.readLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String sampleId = parts[0].trim();

                    String cancerTypeStr = labelsMap.get(sampleId);
                    if (cancerTypeStr == null) {
                        continue;
                    }

                    CancerType cancerType;
                    try {
                        cancerType = CancerType.valueOf(cancerTypeStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Неизвестный тип рака: " + cancerTypeStr);
                        continue;
                    }

                    List<Double> geneExpressions = new ArrayList<>();
                    for (int i = 1; i < parts.length; i++) {
                        geneExpressions.add(Double.parseDouble(parts[i].trim()));
                    }

                    samples.add(new CancerSample(sampleId, cancerType, geneExpressions));
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new DataLoadingException("Ошибка чтения файла данных или парсинга чисел", e);
        }
        return samples;
    }
}