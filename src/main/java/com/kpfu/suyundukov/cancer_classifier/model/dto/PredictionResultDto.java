package com.kpfu.suyundukov.cancer_classifier.model.dto;

import com.kpfu.suyundukov.cancer_classifier.model.CancerType;

/**
 * Объект передачи данных (DTO), содержащий результат предсказания.
 * Этот объект будет автоматически сериализован в JSON для отправки на фронтенд.
 */
public class PredictionResultDto {

    /** Предсказанный тип рака.*/
    private CancerType cancerType;

    /** Вероятность предсказания.*/
    private double probability;

    /** Сообщение или статус (например, "Success").*/
    private String message;

    public PredictionResultDto(CancerType cancerType, double probability, String message) {
        this.cancerType = cancerType;
        this.probability = probability;
        this.message = message;
    }

    public CancerType getCancerType() {
        return cancerType;
    }

    public void setCancerType(CancerType cancerType) {
        this.cancerType = cancerType;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}