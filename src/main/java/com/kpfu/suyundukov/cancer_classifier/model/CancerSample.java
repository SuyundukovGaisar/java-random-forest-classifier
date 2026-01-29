package com.kpfu.suyundukov.cancer_classifier.model;

import java.util.List;
import java.util.Objects;

/**
 * Модель данных, представляющая один биологический образец (пациента).
 * Содержит идентификатор, диагноз (тип рака) и список уровней экспрессии генов.
 */
public class CancerSample {

    /** Уникальный идентификатор образца (например, "sample_1"). */
    private String cancerId;

    /** Тип рака (метка класса). Может быть null, если тип еще не предсказан. */
    private CancerType cancerType;

    /** Список вещественных чисел, отражающих экспрессию генов. */
    private List<Double> geneExpressions;

    /**
     * Конструктор для создания нового образца.
     *
     * @param cancerId        Идентификатор образца.
     * @param cancerType      Тип рака (метка).
     * @param geneExpressions Данные экспрессии генов.
     */
    public CancerSample(String cancerId, CancerType cancerType, List<Double> geneExpressions) {
        this.cancerId = cancerId;
        this.cancerType = cancerType;
        this.geneExpressions = geneExpressions;
    }

    /**
     * Возвращает идентификатор образца.
     * @return строка с ID.
     */
    public String getCancerId() {
        return cancerId;
    }

    public void setCancerId(String cancerId) {
        this.cancerId = cancerId;
    }

    /**
     * Возвращает тип рака.
     * @return строка с типом рака.
     */
    public CancerType getCancerType() {
        return cancerType;
    }

    public void setCancerType(CancerType cancerType) {
        this.cancerType = cancerType;
    }

    /**
     * Возвращает список значений экспрессии генов.
     * @return список чисел Double.
     */
    public List<Double> getGeneExpressions() {
        return geneExpressions;
    }

    public void setGeneExpressions(List<Double> geneExpressions) {
        this.geneExpressions = geneExpressions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancerSample that = (CancerSample) o;
        return Objects.equals(cancerId, that.cancerId) &&
                cancerType == that.cancerType &&
                Objects.equals(geneExpressions, that.geneExpressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cancerId, cancerType, geneExpressions);
    }

    @Override
    public String toString() {
        return "CancerSample{" +
                "id='" + cancerId + '\'' +
                ", type='" + cancerType + '\'' +
                ", genesCount=" + (geneExpressions != null ? geneExpressions.size() : 0) +
                '}';
    }
}