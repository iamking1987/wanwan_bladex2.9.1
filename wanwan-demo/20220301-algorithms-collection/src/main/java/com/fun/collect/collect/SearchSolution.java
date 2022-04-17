package com.fun.collect.collect;

/**
 * 查找算法集合
 * @author wanwan 2022/4/16
 */
public class SearchSolution {

	public static void main(String[] args) {
		int []arr = {0, 2, 5, 6, 8, 10};
		int index = binarySearch(arr, 10);
		System.out.println(index);
	}

	/**
	 * 折半查找
	 */
	public static int binarySearch(int []arr, int target) {
		int right = arr.length -1, left = 0 , mid;
		while (right >= left) {
			mid = (right + left) / 2;
			if (arr[mid] < target) {
				left = mid + 1;
			}
			else if (arr[mid] > target) {
				right = mid - 1;
			}
			else {
				return mid;
			}
		}
		return -1;
	}

	/**
	 * 斐波那契查找
	 */
	public static int fibonacciSearch(int []arr, int target) {

		return -1;
	}

}
