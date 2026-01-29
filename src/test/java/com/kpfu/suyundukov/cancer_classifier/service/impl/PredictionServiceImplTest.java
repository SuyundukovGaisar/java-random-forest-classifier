package com.kpfu.suyundukov.cancer_classifier.service.impl;

import com.kpfu.suyundukov.cancer_classifier.exception.DataLoadingException;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import com.kpfu.suyundukov.cancer_classifier.model.dto.PredictionResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PredictionServiceImplTest {

    private PredictionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PredictionServiceImpl();
    }

    @Test
    void testPredictFromFile_Success() {
        String csvContent = "sample_test, 0.5, 1.2, 3.4";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        PredictionResultDto result = service.predictFromFile(file);

        assertNotNull(result);
        assertEquals(CancerType.BRCA, result.getCancerType());
        assertEquals("Анализ выполнен успешно", result.getMessage());
    }

    @Test
    void testPredictFromFile_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "", "text/csv", new byte[0]
        );

        assertThrows(DataLoadingException.class, () -> {
            service.predictFromFile(emptyFile);
        });
    }

    @Test
    void testPredictFromFile_InvalidFormat() {
        String invalidContent = "sample_id, not_a_number, 1.0";
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "test.csv", "text/csv", invalidContent.getBytes()
        );

        Exception exception = assertThrows(DataLoadingException.class, () -> {
            service.predictFromFile(invalidFile);
        });

        assertTrue(exception.getMessage().contains("Ошибка формата файла"));
    }
}