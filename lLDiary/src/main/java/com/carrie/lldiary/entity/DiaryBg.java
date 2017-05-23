package com.carrie.lldiary.entity;

/**
 * Created by new on 2017/4/15.
 */

public class DiaryBg {
    public String bgName;
    /**文字颜色：0为黑色，1为白色**/
    public Integer textColor;

    public DiaryBg(String bgName, Integer textColor) {
        this.bgName = bgName;
        this.textColor = textColor;
    }
}
