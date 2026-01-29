package com.kpfu.suyundukov.cancer_classifier.model.dto;

import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PredictionResultDtoTest {

    @Test
    void testConstructorAndGetters() {
        CancerType type = CancerType.LUAD;
        double probability = 0.85;
        String message = "Success";

        PredictionResultDto dto = new PredictionResultDto(type, probability, message);

        assertEquals(type, dto.getCancerType());
        assertEquals(probability, dto.getProbability());
        assertEquals(message, dto.getMessage());
    }

    @Test
    void testSetters() {
        PredictionResultDto dto = new PredictionResultDto(CancerType.BRCA, 0.0, "Init");

        dto.setCancerType(CancerType.KIRC);
        dto.setProbability(0.99);
        dto.setMessage("Updated");

        assertEquals(CancerType.KIRC, dto.getCancerType());
        assertEquals(0.99, dto.getProbability());
        assertEquals("Updated", dto.getMessage());
    }
}