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

	public static BSTree buildTree(BSTree tree, int key) {
		if (tree == null) {
			BSTree bsTree = new BSTree();
			bsTree.setData(key);
		}
		else if (key > tree.getData()) {
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
