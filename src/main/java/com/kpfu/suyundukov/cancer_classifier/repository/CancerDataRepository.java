package com.kpfu.suyundukov.cancer_classifier.repository;

import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import java.util.List;

/**
 * Интерфейс репозитория для доступа к данным образцов рака.
 * Определяет контракт для получения данных, независимо от источника.
 */
public interface CancerDataRepository {

    /**
     * Загружает все доступные образцы данных.
     *
     * @return Список объектов {@link CancerSample}.
     */
    List<CancerSample> findAll();
}