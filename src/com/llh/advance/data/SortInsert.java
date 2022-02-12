package com.llh.advance.data;
/**
 * @author: lviter
 * @desc:
 * @date:2022/1/25
 */
public class SortInsert {

    public static void main(String[] args) {
        // 需要排序的数组
        int arr[] = { 49, 20, 36, 51, 18, 94, 61, 31, 50 };
        int res[] = sortArrays(arr);
        if(res.length > 0){
            for (int i = 0; i < res.length; i++) {
                System.out.println(arr[i] + "\t");
            }
        }
    }

    private static int[] sortArrays(int[] arr) {

        int res[] = arr;

        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            int j = i - 1;
            temp = arr[i];
            for (;j >= 0;j--){

            }
        }


        return new int[5];
    }
}
