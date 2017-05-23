package com.carrie.lldiary.entity;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class PlanContent {
    public Long id;
    public String importantDegree;
    public Integer icon;
    /** Not-null value. */
    public String content;
    public boolean finish;
    public boolean remind;
    /** Not-null value. */
    public String startTime;
    public String endTime;

    public PlanContent(Long id, String importantDegree,Integer icon,String content,  boolean finish,   boolean remind, String startTime,String endTime) {
        this.content = content;
        this.endTime = endTime;
        this.finish = finish;
        this.icon = icon;
        this.id = id;
        this.importantDegree = importantDegree;
        this.remind = remind;
        this.startTime = startTime;
    }
}
