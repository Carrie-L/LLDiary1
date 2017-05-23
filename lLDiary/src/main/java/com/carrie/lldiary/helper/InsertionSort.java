package com.carrie.lldiary.helper;

/**
 * Created by Administrator on 2016/5/7 0007.
 */
/**
 * 插入排序法
 * @author Andy.Chen
 * @param <T>
 *
 */
public class InsertionSort<T extends Comparable<T>> extends Sorter<T> {

    @Override
    public void sort(T[] array, int from, int len) {
        T tmp = null;
        for (int i = from + 1; i < from + len; i++) {
            tmp = array[i];
            int j = i;
            for (; j > from; j--) {
                if (tmp.compareTo(array[j - 1]) < 0) {
                    array[j] = array[j - 1];
                } else {
                    break;
                }
            }
            array[j] = tmp;
        }
    }
}
