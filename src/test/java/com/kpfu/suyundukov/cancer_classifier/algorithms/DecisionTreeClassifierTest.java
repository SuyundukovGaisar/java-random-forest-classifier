package com.kpfu.suyundukov.cancer_classifier.algorithms;

import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecisionTreeClassifierTest {

    private DecisionTreeClassifier classifier;

    @BeforeEach
    void setUp() {
        classifier = new DecisionTreeClassifier(5, 2);
    }

    @Test
    void testCalculateGini_OneClass() {
        List<CancerSample> samples = new ArrayList<>();
        samples.add(new CancerSample("1", CancerType.BRCA, Collections.emptyList()));
        samples.add(new CancerSample("2", CancerType.BRCA, Collections.emptyList()));
        samples.add(new CancerSample("3", CancerType.BRCA, Collections.emptyList()));

        double gini = classifier.calculateGini(samples);

        assertEquals(0.0, gini, 0.0001, "Для чистого узла Gini должен быть 0");
    }

    @Test
    void testCalculateGini_TwoClasses() {
        List<CancerSample> samples = new ArrayList<>();
        samples.add(new CancerSample("1", CancerType.BRCA, Collections.emptyList()));
        samples.add(new CancerSample("2", CancerType.BRCA, Collections.emptyList()));
        samples.add(new CancerSample("3", CancerType.LUAD, Collections.emptyList()));
        samples.add(new CancerSample("4", CancerType.LUAD, Collections.emptyList()));

        double gini = classifier.calculateGini(samples);

        assertEquals(0.5, gini, 0.0001, "Для разделения 50/50 Gini должен быть 0.5");
    }

    @Test
    void testCalculateGini_ComplexMix() {
        List<CancerSample> samples = new ArrayList<>();
        samples.add(new CancerSample("1", CancerType.BRCA, Collections.emptyList()));
        samples.add(new CancerSample("2", CancerType.BRCA, Collections.emptyList()));
        samples.add(new CancerSample("3", CancerType.LUAD, Collections.emptyList()));
        samples.add(new CancerSample("4", CancerType.KIRC, Collections.emptyList()));

        double gini = classifier.calculateGini(samples);

        assertEquals(0.625, gini, 0.0001);
    }

    @Test
    void testMajorityClassLogic() {
        DecisionTreeClassifier tree = new DecisionTreeClassifier(0, 2);

        List<CancerSample> samples = new ArrayList<>();

        samples.add(new CancerSample("1", CancerType.BRCA, Arrays.asList(1.0)));
        samples.add(new CancerSample("2", CancerType.BRCA, Arrays.asList(1.0)));
        samples.add(new CancerSample("3", CancerType.BRCA, Arrays.asList(1.0)));
        samples.add(new CancerSample("4", CancerType.LUAD, Arrays.asList(1.0)));

        tree.train(samples);

        TreeNode root = tree.getRoot();
        assertTrue(root.isLeaf());
        assertEquals(CancerType.BRCA, root.getPredictedClass(), "Должен победить класс " +
                "большинства (BRCA)");
    }

    @Test
    void testTrainAndPredict_SimpleSeparation() {
        List<CancerSample> trainData = new ArrayList<>();
        trainData.add(new CancerSample("1", CancerType.BRCA, Arrays.asList(10.0)));
        trainData.add(new CancerSample("2", CancerType.BRCA, Arrays.asList(20.0)));
        trainData.add(new CancerSample("3", CancerType.LUAD, Arrays.asList(80.0)));
        trainData.add(new CancerSample("4", CancerType.LUAD, Arrays.asList(90.0)));

        classifier.train(trainData);

        TreeNode root = classifier.getRoot();
        assertFalse(root.isLeaf(), "Корень должен разделиться");
        assertEquals(0, root.getFeatureIndex(), "Дерево должно выбрать Ген №0");

        CancerType result1 = classifier.predict(Arrays.asList(15.0));
        assertEquals(CancerType.BRCA, result1);

        CancerType result2 = classifier.predict(Arrays.asList(85.0));
        assertEquals(CancerType.LUAD, result2);
    }
}