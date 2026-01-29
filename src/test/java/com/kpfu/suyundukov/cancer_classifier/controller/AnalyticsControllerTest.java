package com.kpfu.suyundukov.cancer_classifier.controller;

import com.kpfu.suyundukov.cancer_classifier.model.CancerDataset;
import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import com.kpfu.suyundukov.cancer_classifier.model.dto.PredictionResultDto;
import com.kpfu.suyundukov.cancer_classifier.service.DataProcessingService;
import com.kpfu.suyundukov.cancer_classifier.service.PredictionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataProcessingService dataProcessingService;

    @MockBean
    private PredictionService predictionService;

    @Test
    void testGetDatasetStatus_Success() throws Exception {
        List<Double> mockGenes = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.555);

        List<CancerSample> trainSamples = Collections.singletonList(
                new CancerSample("s1", CancerType.BRCA, mockGenes)
        );
        CancerDataset mockDataset = new CancerDataset(trainSamples, Collections.emptyList());

        when(dataProcessingService.getPreparedDataset()).thenReturn(mockDataset);

        mockMvc.perform(get("/api/v1/analytics/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем код 200
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.trainSize").value(1))
                .andExpect(jsonPath("$.totalSamples").value(1))
                .andExpect(jsonPath("$.sampleNormalizationCheck").value(0.555));
    }

    @Test
    void testPredictCancerType_Success() throws Exception {
        PredictionResultDto mockResult = new PredictionResultDto(CancerType.BRCA, 0.99,
                "OK");
        when(predictionService.predictFromFile(any())).thenReturn(mockResult);

        MockMultipartFile file = new MockMultipartFile(
                "file", "data.csv", "text/csv", "content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/analytics/predict").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancerType").value(CancerType.BRCA.toString()))
                .andExpect(jsonPath("$.probability").value(0.99));
    }
}