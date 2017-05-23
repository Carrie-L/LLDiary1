package com.carrie.lldiary.helper;

/**
 * Created by Administrator on 2016/5/7 0007.
 */
/**
 * 排序抽象基础类
 * @author Andy.Chen
 *
 * @param <T>
 */
public abstract class Sorter<T extends Comparable<T>> {

    public abstract void sort(T[] array,int from,int len);

    public final void sort(T[] array){
        sort(array,0,array.length);
    }

    protected final void swap(T[] array,int from,int to){
        T tmp = array[from];
        array[from] = array[to];
        array[to] = tmp;
    }

}
