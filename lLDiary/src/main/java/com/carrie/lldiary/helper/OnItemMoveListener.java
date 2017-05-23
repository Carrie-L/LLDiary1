package com.carrie.lldiary.helper;

/**
 * Created by Administrator on 2016/6/18 0018.
 * 列表拖曳排序监听
 */
public interface OnItemMoveListener {
    void move(boolean finished);

    void remove(boolean removed,String description);

}
