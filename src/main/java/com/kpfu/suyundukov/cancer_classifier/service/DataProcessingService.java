package com.kpfu.suyundukov.cancer_classifier.service;

import com.kpfu.suyundukov.cancer_classifier.model.CancerDataset;

/**
 * Интерфейс сервиса обработки данных.
 * Отвечает за подготовку датасета (загрузка, разделение, нормализация).
 */
public interface DataProcessingService {

    /**
     * Возвращает подготовленный и нормализованный набор данных.
     * Если данные еще не загружены, метод должен инициировать загрузку или вернуть кешированную версию.
     *
     * @return Объект {@link CancerDataset} с обучающей и тестовой выборками.
     */
    CancerDataset getPreparedDataset();
}