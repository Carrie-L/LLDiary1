package com.carrie.lldiary.entity;


/**
 * 
 ******************************************
 * @author 廖乃波
 * @文件名称	:  Emoji.java
 * @创建时间	: 2013-1-27 下午02:33:43
 * @文件描述	: 表情对象实体
 ******************************************
 */
public class Emoji {

    /** 表情资源图片对应的ID */
    private int id;

    /** 表情资源对应的文字描述 */
    private String character;

    /** 表情资源的文件名，对于下载的表情，为 */
    private String faceName;

    /** 表情资源图片对应的ID */
    public int getId() {
        return id;
    }

    /** 表情资源图片对应的ID */
    public void setId(int id) {
        this.id=id;
    }

    /** 表情资源对应的文字描述 */
    public String getCharacter() {
        return character;
    }

    /** 表情资源对应的文字描述 */
    public void setCharacter(String character) {
        this.character=character;
    }

    /** 表情资源的文件名 */
    public String getFaceName() {
        return faceName;
    }

    /** 表情资源的文件名 */
    public void setFaceName(String faceName) {
        this.faceName=faceName;
    }

    @Override
    public String toString() {
        return "Emoji{" +
                "id=" + id +
                ", character='" + character + '\'' +
                ", faceName='" + faceName + '\'' +
                '}';
    }
}
