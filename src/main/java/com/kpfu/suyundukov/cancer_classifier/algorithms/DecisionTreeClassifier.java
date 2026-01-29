package com.kpfu.suyundukov.cancer_classifier.algorithms;

import com.kpfu.suyundukov.cancer_classifier.model.CancerSample;
import com.kpfu.suyundukov.cancer_classifier.model.CancerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация классификатора на основе Дерева Решений.
 * Использует критерий Джини для построения дерева.
 */
public class DecisionTreeClassifier {

    private int maxDepth;
    private int minSamplesSplit;

    private TreeNode root;

    public DecisionTreeClassifier(int maxDepth, int minSamplesSplit) {
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
    }

    /**
     * Вычисляет Индекс Джини (Gini Impurity) для набора данных.
     * Формула: G = 1 - sum(p_i^2)
     *
     * @param samples Список образцов в узле.
     * @return Значение от 0.0 (идеально чистый узел) до 1.0 (максимальный хаос).
     */
    public double calculateGini(List<CancerSample> samples) {
        if (samples == null || samples.isEmpty()) {
            return 0.0;
        }

        int totalSamples = samples.size();
        Map<CancerType, Integer> classCounts = new HashMap<>();

        for (CancerSample sample : samples){
            CancerType type = sample.getCancerType();
            classCounts.put(type, classCounts.getOrDefault(type, 0) + 1);
        }

        double sumSquaredProbs = 0.0;
        for (Integer count : classCounts.values()) {
            double prob = (double) count / totalSamples;
            sumSquaredProbs += (prob * prob);
        }

        return 1.0 - sumSquaredProbs;
    }

    public TreeNode getRoot() {
        return root;
    }

    /**
     * Внутренний класс для хранения информации о наилучшем найденном разделении.
     */
    private static class BestSplit {
        int featureIndex;
        double threshold;
        double gini;
        List<CancerSample> leftSubset;
        List<CancerSample> rightSubset;

        BestSplit() {
            this.gini = Double.MAX_VALUE;
        }
    }

    /**
     * Определяет класс большинства в списке образцов.
     * Используется для создания Листа.
     *
     * @param samples Текущий набор образцов.
     * @return Класс рака, представляющий большинство.
     */
    private CancerType getMajorityClass(List<CancerSample> samples) {
        if (samples.isEmpty()) return null;

        Map<CancerType, Integer> counts = new HashMap<>();
        for (CancerSample sample : samples){
            counts.put(sample.getCancerType(), counts.getOrDefault(sample.getCancerType(), 0) + 1);
        }

        CancerType majority = null;
        int maxValue = -1;

        for (CancerType type : counts.keySet()){
            int value = counts.get(type);
            if (value > maxValue){
                maxValue = value;
                majority = type;
            }
        }
        return majority;
    }

    /**
     * Находит наилучшее разделение данных, перебирая признаки и пороги.
     * Реализует формулу прироста информации (через минимизацию Gini).
     *
     * @param samples Текущий набор образцов.
     * @return Объект BestSplit с лучшими параметрами.
     */
    private BestSplit findBestSplit(List<CancerSample> samples) {
        BestSplit bestSplit = new BestSplit();

        int numFeatures = samples.get(0).getGeneExpressions().size();
        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++){
            for (CancerSample sample : samples){
                double threshold = sample.getGeneExpressions().get(featureIndex);

                List<CancerSample> leftList = new ArrayList<>();
                List<CancerSample> rightList = new ArrayList<>();

                for (CancerSample s : samples){
                    if (s.getGeneExpressions().get(featureIndex) < threshold){
                        leftList.add(s);
                    }else{
                        rightList.add(s);
                    }
                }

                if (leftList.isEmpty() || rightList.isEmpty()) continue;

                double giniLeft = calculateGini(leftList);
                double giniRight = calculateGini(rightList);

                double weightedGini = ((double) leftList.size() / samples.size()) * giniLeft +
                        ((double) rightList.size() / samples.size()) * giniRight;
                if (weightedGini < bestSplit.gini) {
                    bestSplit.gini = weightedGini;
                    bestSplit.featureIndex = featureIndex;
                    bestSplit.threshold = threshold;
                    bestSplit.leftSubset = leftList;
                    bestSplit.rightSubset = rightList;
                }
            }
        }
        return bestSplit;
    }

    /**
     * Публичный метод для запуска обучения.
     * @param data Обучающая выборка.
     */
    public void train(List<CancerSample> data) {
        this.root = buildTree(data, 0);
    }

    /**
     * Рекурсивный метод построения дерева.
     *
     * @param samples Текущий набор данных.
     * @param depth Текущая глубина дерева.
     * @return Построенный узел (TreeNode).
     */
    private TreeNode buildTree(List<CancerSample> samples, int depth) {
        CancerType majorityClass = getMajorityClass(samples);

        if (samples.isEmpty()) {
            return null;
        }

        if (isPure(samples)) {
            return new TreeNode(majorityClass);
        }

        if (depth >= maxDepth) {
            return new TreeNode(majorityClass);
        }

        if (samples.size() < minSamplesSplit) {
            return new TreeNode(majorityClass);
        }

        BestSplit bestSplit = findBestSplit(samples);

        if (bestSplit.gini == Double.MAX_VALUE) {
            return new TreeNode(majorityClass);
        }

        TreeNode leftChild = buildTree(bestSplit.leftSubset, depth + 1);
        TreeNode rightChild = buildTree(bestSplit.rightSubset, depth + 1);

        return new TreeNode(bestSplit.featureIndex, bestSplit.threshold, leftChild, rightChild);
    }

    /**
     * Проверяет, состоит ли узел только из одного класса.
     */
    private boolean isPure(List<CancerSample> samples) {
        if (samples.isEmpty()) return true;
        CancerType firstType = samples.get(0).getCancerType();
        for (CancerSample s : samples) {
            if (s.getCancerType() != firstType) {
                return false;
            }
        }
        return true;
    }

    /**
     * Предсказывает класс для одного образца.
     * @param geneExpressions Вектор экспрессии генов пациента.
     * @return Предсказанный тип рака.
     */
    public CancerType predict(List<Double> geneExpressions) {
        return predictRecursive(this.root, geneExpressions);
    }

    private CancerType predictRecursive(TreeNode node, List<Double> genes){
        if (node.isLeaf()){
            return node.getPredictedClass();
        }

        double val = genes.get(node.getFeatureIndex());
        if (val < node.getThreshold()) {
            return predictRecursive(node.getLeft(), genes);
        } else {
            return predictRecursive(node.getRight(), genes);
        }
    }
}
