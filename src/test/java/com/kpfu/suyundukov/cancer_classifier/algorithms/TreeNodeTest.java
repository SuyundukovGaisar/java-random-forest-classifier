package com.kpfu.suyundukov.cancer_classifier.algorithms;

import com.kpfu.suyundukov.cancer_classifier.model.CancerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {

    @Test
    void testLeafNodeCreation() {
        TreeNode leaf = new TreeNode(CancerType.BRCA);

        assertTrue(leaf.isLeaf(), "Узел должен быть листом");
        assertEquals(CancerType.BRCA, leaf.getPredictedClass(), "Предсказание должно совпадать");
    }

    @Test
    void testInternalNodeCreation() {
        TreeNode leftChild = new TreeNode(CancerType.BRCA);
        TreeNode rightChild = new TreeNode(CancerType.LUAD);

        TreeNode node = new TreeNode(5, 10.5, leftChild, rightChild);

        assertFalse(node.isLeaf(), "Узел НЕ должен быть листом");
        assertEquals(5, node.getFeatureIndex());
        assertEquals(10.5, node.getThreshold());
        assertEquals(leftChild, node.getLeft());
        assertEquals(rightChild, node.getRight());
        assertNull(node.getPredictedClass(), "У внутреннего узла нет предсказания класса");
    }
}