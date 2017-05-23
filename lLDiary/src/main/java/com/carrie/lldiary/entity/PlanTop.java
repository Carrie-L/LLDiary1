package com.carrie.lldiary.entity;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class PlanTop {

    public String date;
    public int sum;

    public PlanTop(String date, int total) {
        this.date = date;
        this.sum = total;
    }

    @Override
    public String toString() {
        return "PlanTop{" +
                "date='" + date + '\'' +
                ", total='" + sum + '\'' +
                '}';
    }
}
