package com.fun.collect.collect;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanwan 2022/4/17
 */
public class BSTreeSolution {

	public static void main(String[] args) {
		BSTree bsTree = new BSTree(20, null, null);
		buildTree(bsTree, 21);
		buildTree(bsTree, 10);
		buildTree(bsTree, 36);
		buildTree(bsTree, 18);
		System.out.println(JSONUtil.toJsonStr(bsTree));
	}

	/**
	 * 前序遍历
	 */
	public static void preOrder(BSTree tree) {
		if (tree != null) {
			System.out.println(tree.data);
			preOrder(tree.lchild);
			preOrder(tree.rchild);
		}
	}

	/**
	 * 中序遍历
	 */
	public static void inOrder(BSTree tree) {
		if (tree != null) {
			inOrder(tree.lchild);
			System.out.println(tree.data);
			inOrder(tree.rchild);
		}
	}

	/**
	 * 后序遍历
	 */
	public static void postOrder(BSTree tree) {
		if (tree != null) {
			postOrder(tree.lchild);
			postOrder(tree.rchild);
			System.out.println(tree.data);
		}
	}

	/**
	 * 判断是否存在节点
	 */
	public static boolean hasKey(BSTree tree, int key) {
		if (tree == null) {
			return false;
		}
		if (tree.data == key) {
			return true;
		}
		if (key > tree.data) {
			return hasKey(tree.getRchild(), key);
		}
		else {
			return hasKey(tree.getLchild(), key);
		}
	}

	/**
	 * 构建树
	 */
	public static BSTree buildTree(BSTree tree, int key) {
		if (tree == null) {
			BSTree bsTree = new BSTree();
			bsTree.setData(key);
			return bsTree;
		}
		if (key > tree.getData()) {
			tree.setRchild(buildTree(tree.getRchild(), key));
		}
		else {
			tree.setLchild(buildTree(tree.getLchild(), key));
		}
		return tree;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class BSTree {
		private int data;
		private BSTree lchild;
		private BSTree rchild;
	}
}
