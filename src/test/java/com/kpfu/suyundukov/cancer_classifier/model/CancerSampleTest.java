package com.kpfu.suyundukov.cancer_classifier.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CancerSampleTest {

    @Test
    void testConstructorAndGetters() {
        String id = "sample_01";
        CancerType type = CancerType.BRCA;
        List<Double> genes = Arrays.asList(0.5, 0.1, 0.9);

        CancerSample sample = new CancerSample(id, type, genes);

        assertEquals(id, sample.getCancerId());
        assertEquals(type, sample.getCancerType());
        assertEquals(genes, sample.getGeneExpressions());
        assertEquals(3, sample.getGeneExpressions().size());
    }

    @Test
    void testSetters() {
        CancerSample sample = new CancerSample("id", CancerType.LUAD, null);

        sample.setCancerId("new_id");
        sample.setCancerType(CancerType.LUAD);
        List<Double> newGenes = Arrays.asList(1.0, 2.0);
        sample.setGeneExpressions(newGenes);

        assertEquals("new_id", sample.getCancerId());
        assertEquals(CancerType.LUAD, sample.getCancerType());
        assertEquals(newGenes, sample.getGeneExpressions());
    }

    @Test
    void testEqualsAndHashCode() {
        List<Double> genes = Arrays.asList(0.1, 0.2);
        CancerSample sample1 = new CancerSample("s1", CancerType.BRCA, genes);
        CancerSample sample2 = new CancerSample("s1", CancerType.BRCA, genes);
        CancerSample sample3 = new CancerSample("s2", CancerType.KIRC, genes);

        assertEquals(sample1, sample2, "Объекты с одинаковыми полями должны быть равны");
        assertNotEquals(sample1, sample3, "Объекты с разными ID должны отличаться");

        assertEquals(sample1.hashCode(), sample2.hashCode(), "Хэш-коды равных объектов должны совпадать");
        assertNotEquals(sample1.hashCode(), sample3.hashCode());
    }

    @Test
    void testToString() {
        CancerSample sample = new CancerSample("test_id", CancerType.COAD, Arrays.asList(0.1));
        String result = sample.toString();

        assertTrue(result.contains("test_id"));
    }
}