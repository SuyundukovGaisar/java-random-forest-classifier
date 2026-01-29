package com.kpfu.suyundukov.cancer_classifier.service.impl;

import com.kpfu.suyundukov.cancer_classifier.model.CancerDataset;
import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import com.kpfu.suyundukov.cancer_classifier.repository.CancerDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataProcessingServiceImplTest {

    @Mock
    private CancerDataRepository repository;

    private DataProcessingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DataProcessingServiceImpl(repository, 0.8);
    }

    @Test
    void testInitAndNormalize_Success() {

        List<CancerSample> mockSamples = new ArrayList<>();

        mockSamples.add(new CancerSample("s1", CancerType.LUAD, Arrays.asList(10.0)));
        mockSamples.add(new CancerSample("s2", CancerType.LUAD, Arrays.asList(20.0)));

        for (int i = 0; i < 8; i++) {
            mockSamples.add(new CancerSample("s" + (i + 3), CancerType.COAD, Arrays.asList(15.0)));
        }

        when(repository.findAll()).thenReturn(mockSamples);

        service.init();
        CancerDataset result = service.getPreparedDataset();

        assertNotNull(result);

        assertEquals(8, result.getTrainData().size(), "Обучающая выборка должна быть 80%");
        assertEquals(2, result.getTestData().size(), "Тестовая выборка должна быть 20%");

        boolean foundMin = false;
        boolean foundMax = false;

        List<CancerSample> allProcessed = new ArrayList<>(result.getTrainData());
        allProcessed.addAll(result.getTestData());

        for (CancerSample s : allProcessed) {
            double val = s.getGeneExpressions().get(0);
            if (val == 0.0) foundMin = true;
            if (val == 1.0) foundMax = true;
        }

        assertTrue(foundMin, "Минимальное значение (10.0) должно нормализоваться в 0.0");
        assertTrue(foundMax, "Максимальное значение (20.0) должно нормализоваться в 1.0");
    }

    @Test
    void testInit_EmptyRepository() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        service.init();
        CancerDataset result = service.getPreparedDataset();

        assertNotNull(result);
        assertTrue(result.getTrainData().isEmpty());
        assertTrue(result.getTestData().isEmpty());
    }
}