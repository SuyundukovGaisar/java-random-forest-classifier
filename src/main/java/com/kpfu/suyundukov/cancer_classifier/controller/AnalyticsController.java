package com.kpfu.suyundukov.cancer_classifier.controller;

import com.kpfu.suyundukov.cancer_classifier.model.CancerDataset;
import com.kpfu.suyundukov.cancer_classifier.service.DataProcessingService;
import com.kpfu.suyundukov.cancer_classifier.model.dto.PredictionResultDto;
import com.kpfu.suyundukov.cancer_classifier.service.PredictionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST-контроллер для получения аналитической информации о загруженных данных.
 * Предоставляет API для внешних клиентов.
 */
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final DataProcessingService dataProcessingService;
    private final PredictionService predictionService;

    /**
     * Конструктор контроллера.
     *
     * @param dataProcessingService Сервис обработки данных.
     */
    public AnalyticsController(DataProcessingService dataProcessingService,
                               PredictionService predictionService) {
        this.dataProcessingService = dataProcessingService;
        this.predictionService = predictionService;
    }

    /**
     * Получение сводной статистики по загруженному датасету.
     * <p>
     * URL: GET /api/v1/analytics/status
     *
     * @return JSON-объект со статистикой (количество образцов, размеры выборок).
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDatasetStatus() {
        CancerDataset dataset = dataProcessingService.getPreparedDataset();

        Map<String, Object> response = new HashMap<>();

        if (dataset == null || dataset.getTrainData().isEmpty()) {
            response.put("status", "EMPTY");
            response.put("message", "Данные не загружены или отсутствуют.");
            return ResponseEntity.ok(response);
        }

        int trainSize = dataset.getTrainData().size();
        int testSize = dataset.getTestData().size();
        int total = trainSize + testSize;

        response.put("status", "OK");
        response.put("totalSamples", total);
        response.put("trainSize", trainSize);
        response.put("testSize", testSize);

        if (trainSize > 0 && !dataset.getTrainData().get(0).getGeneExpressions().isEmpty()) {
            response.put("sampleNormalizationCheck",
                    dataset.getTrainData().get(0).getGeneExpressions().get(4));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Эндпоинт для загрузки файла пациента и получения прогноза.
     * Принимает файл (CSV), возвращает JSON с диагнозом.
     *
     * URL: POST /api/v1/analytics/predict
     *
     * @param file Файл с данными экспрессии генов.
     * @return Объект PredictionResultDto в формате JSON.
     */
    @PostMapping("/predict")
    public ResponseEntity<PredictionResultDto> predictCancerType(@RequestParam("file") MultipartFile file) {
        PredictionResultDto result = predictionService.predictFromFile(file);
        return ResponseEntity.ok(result);
    }
}