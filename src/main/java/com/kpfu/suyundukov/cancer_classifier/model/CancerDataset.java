package com.kpfu.suyundukov.cancer_classifier.model;

import java.util.List;

/**
 * Класс-контейнер, предназначенный для хранения
 * разделенного набора данных.
 * Используется для передачи обучающей и тестовой выборок из сервиса в контроллер
 * или другие компоненты системы.
 */
public class CancerDataset {
    /**
     * Обучающая выборка.
     * Используется для тренировки модели.
     */
    private List<CancerSample> trainData;

    /**
     * Тестовая выборка.
     * Используется для оценки качества модели.
     */
    private List<CancerSample> testData;

    public CancerDataset(List<CancerSample> trainData, List<CancerSample> testData) {
        this.trainData = trainData;
        this.testData = testData;
    }

    /**
     * Возвращает обучающую выборку.
     * @return список пациентов в обучающей выборке.
     */
    public List<CancerSample> getTrainData() {
        return trainData;
    }

    /**
     * Возвращает тестовую выборку.
     * @return список пациентов в тестовой выборке.
     */
    public List<CancerSample> getTestData() {
        return testData;
    }
}