package com.kpfu.suyundukov.cancer_classifier.service.impl;

import com.kpfu.suyundukov.cancer_classifier.exception.DataLoadingException;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import com.kpfu.suyundukov.cancer_classifier.model.dto.PredictionResultDto;
import com.kpfu.suyundukov.cancer_classifier.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {

    private static final Logger logger = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Override
    public PredictionResultDto predictFromFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new DataLoadingException("Загруженный файл пуст.");
        }

        logger.info("Получен файл для анализа: {}", file.getOriginalFilename());

        try {
            List<Double> geneExpressions = parseCsvFile(file);
            logger.info("Файл успешно прочитан. Найдено {} значений генов.", geneExpressions.size());

            // String predictedType = randomForest.predict(geneExpressions);

            return new PredictionResultDto(CancerType.BRCA, 0.95,
                    "Анализ выполнен успешно");

        } catch (IOException | NumberFormatException e) {
            logger.error("Ошибка обработки файла", e);
            throw new DataLoadingException("Ошибка формата файла. Ожидается CSV с числами.", e);
        }
    }

    /**
     * Парсит CSV файл и извлекает значения экспрессии генов.
     */
    private List<Double> parseCsvFile(MultipartFile file) throws IOException {
        List<Double> values = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line = reader.readLine();
            if (line == null) return values;

            String[] parts = line.split(",");
            int startIndex = 1;

            for (int i = startIndex; i < parts.length; i++) {
                values.add(Double.parseDouble(parts[i].trim()));
            }
        }
        return values;
    }
}