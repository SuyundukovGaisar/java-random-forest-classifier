package com.kpfu.suyundukov.cancer_classifier.repository.impl;

import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvCancerDataRepositoryTest {

    @Test
    void testFindAll_LoadsDataCorrectly() {
        String labelsPath = "/test_labels.csv";
        String dataPath = "/test_data.csv";

        CsvCancerDataRepository repository = new CsvCancerDataRepository(labelsPath, dataPath);

        List<CancerSample> samples = repository.findAll();

        assertEquals(2, samples.size(), "Должно быть загружено 2 образца " +
                "(пересечение меток и данных)");

        CancerSample s1 = samples.stream()
                .filter(s -> s.getCancerId().equals("sample_1"))
                .findFirst()
                .orElseThrow();

        assertEquals(CancerType.BRCA, s1.getCancerType());
        assertEquals(2, s1.getGeneExpressions().size());
        assertEquals(10.0, s1.getGeneExpressions().get(0));
    }
}