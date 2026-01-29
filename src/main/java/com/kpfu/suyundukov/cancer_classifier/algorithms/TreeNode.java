package com.kpfu.suyundukov.cancer_classifier.algorithms;

import com.kpfu.suyundukov.cancer_classifier.model.CancerType;

/**
 * Узел дерева решений.
 * Может быть либо внутренним узлом (с правилом разделения), либо листом (с предсказанием).
 */
public class TreeNode {
    private boolean isLeaf;
    private CancerType predictedClass;

    private int featureIndex;
    private double threshold;
    private TreeNode left;
    private TreeNode right;

    /**
     * Конструктор для создания листа.
     */
    public TreeNode(CancerType predictedClass) {
        this.isLeaf = true;
        this.predictedClass = predictedClass;
    }

    /**
     * Конструктор для создания внутреннего узла.
     */
    public TreeNode(int featureIndex, double threshold, TreeNode left, TreeNode right) {
        this.isLeaf = false;
        this.featureIndex = featureIndex;
        this.threshold = threshold;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public CancerType getPredictedClass() {
        return predictedClass;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public double getThreshold() {
        return threshold;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }
}