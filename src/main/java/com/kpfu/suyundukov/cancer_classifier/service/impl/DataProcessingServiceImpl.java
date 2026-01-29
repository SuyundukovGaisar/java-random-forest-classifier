package com.kpfu.suyundukov.cancer_classifier.service.impl;

import com.kpfu.suyundukov.cancer_classifier.model.CancerDataset;
import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.repository.CancerDataRepository;
import com.kpfu.suyundukov.cancer_classifier.service.DataProcessingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Реализация сервиса обработки данных.
 * Выполняет загрузку данных из репозитория, разделение на выборки и нормализацию.
 * Данные загружаются и обрабатываются один раз при старте приложения.
 */
@Service
public class DataProcessingServiceImpl implements DataProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessingServiceImpl.class);

    private final CancerDataRepository repository;
    private final double trainRatio;

    /** Кешированный датасет, готовый к использованию. */
    private CancerDataset cachedDataset;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param repository Репозиторий доступа к данным.
     * @param trainRatio Доля обучающей выборки (из конфигурации).
     */
    public DataProcessingServiceImpl(CancerDataRepository repository,
                                     @Value("${app.training.train-ratio:0.8}") double trainRatio) {
        this.repository = repository;
        this.trainRatio = trainRatio;
    }

    /**
     * Метод инициализации. Вызывается Spring-контейнером после создания бина.
     * Загружает, делит и нормализует данные.
     */
    @PostConstruct
    public void init() {
        logger.info("Запуск инициализации данных...");
        long startTime = System.currentTimeMillis();

        List<CancerSample> allSamples = repository.findAll();
        if (allSamples.isEmpty()) {
            logger.warn("Репозиторий вернул пустой список образцов!");
            this.cachedDataset = new CancerDataset(Collections.emptyList(), Collections.emptyList());
            return;
        }
        logger.info("Загружено {} образцов.", allSamples.size());

        CancerDataset splitData = splitData(allSamples, trainRatio);
        logger.info("Данные разделены. Train: {}, Test: {}",
                splitData.getTrainData().size(), splitData.getTestData().size());

        normalizeData(splitData);
        logger.info("Нормализация завершена.");

        this.cachedDataset = splitData;

        long duration = System.currentTimeMillis() - startTime;
        logger.info("Инициализация данных завершена успешно за {} мс.", duration);
    }

    @Override
    public CancerDataset getPreparedDataset() {
        return cachedDataset;
    }

    /**
     * Разделяет список образцов на обучающую и тестовую выборки.
     */
    private CancerDataset splitData(List<CancerSample> allSamples, double ratio) {
        List<CancerSample> shuffled = new ArrayList<>(allSamples);
        Collections.shuffle(shuffled, new Random(42));

        int splitIndex = (int) (shuffled.size() * ratio);

        List<CancerSample> trainData = new ArrayList<>(shuffled.subList(0, splitIndex));
        List<CancerSample> testData = new ArrayList<>(shuffled.subList(splitIndex, shuffled.size()));

        return new CancerDataset(trainData, testData);
    }

    /**
     * Вычисляет параметры нормализации (min/max) на основе обучающей выборки
     * и применяет их ко всему датасету.
     */
    private void normalizeData(CancerDataset dataset) {
        List<CancerSample> trainData = dataset.getTrainData();
        List<CancerSample> testData = dataset.getTestData();

        if (trainData.isEmpty()) return;

        int numGenes = trainData.get(0).getGeneExpressions().size();
        double[] minValues = new double[numGenes];
        double[] maxValues = new double[numGenes];

        Arrays.fill(minValues, Double.MAX_VALUE);
        Arrays.fill(maxValues, Double.MIN_VALUE);

        for (CancerSample sample : trainData) {
            List<Double> genes = sample.getGeneExpressions();
            for (int i = 0; i < numGenes; i++) {
                double val = genes.get(i);
                if (val < minValues[i]) minValues[i] = val;
                if (val > maxValues[i]) maxValues[i] = val;
            }
        }

        applyNormalization(trainData, minValues, maxValues);
        applyNormalization(testData, minValues, maxValues);
    }

    private void applyNormalization(List<CancerSample> samples, double[] min, double[] max) {
        for (CancerSample sample : samples) {
            List<Double> original = sample.getGeneExpressions();
            List<Double> normalized = new ArrayList<>(original.size());

            for (int i = 0; i < original.size(); i++) {
                double val = original.get(i);
                double denominator = max[i] - min[i];
                double normVal = (denominator != 0) ? (val - min[i]) / denominator : 0.0;
                normalized.add(normVal);
            }
            sample.setGeneExpressions(normalized);
        }
    }
}