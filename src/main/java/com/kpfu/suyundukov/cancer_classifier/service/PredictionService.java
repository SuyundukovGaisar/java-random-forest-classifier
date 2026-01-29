package com.kpfu.suyundukov.cancer_classifier.service;

import com.kpfu.suyundukov.cancer_classifier.model.dto.PredictionResultDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Интерфейс сервиса для выполнения предсказаний.
 * Обрабатывает загруженные файлы пользователей.
 */
public interface PredictionService {

    /**
     * Обрабатывает файл пациента и возвращает результат прогноза.
     *
     * @param file Загруженный пользователем файл (CSV или JSON).
     * @return DTO с результатом предсказания.
     */
    PredictionResultDto predictFromFile(MultipartFile file);
}