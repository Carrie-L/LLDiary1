package com.carrie.lldiary.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "ANN".
 */
public class Ann implements Parcelable {

    private Long id;
    private String content;
    private String person;
    private String relationship;
    private String icon;
    private String date;
    private String remind;
    private String remark;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Ann() {
    }

    public Ann(Long id) {
        this.id = id;
    }

    public Ann(Long id, String content, String person, String relationship, String icon, String date, String remind, String remark) {
        this.id = id;
        this.content = content;
        this.person = person;
        this.relationship = relationship;
        this.icon = icon;
        this.date = date;
        this.remind = remind;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // KEEP METHODS - put your custom methods here
    public String durTime;

    @Override
    public String toString() {
        return "Ann{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", person='" + person + '\'' +
                ", relationship='" + relationship + '\'' +
                ", icon='" + icon + '\'' +
                ", date='" + date + '\'' +
                ", remind='" + remind + '\'' +
                ", remark='" + remark + '\'' +
                ", durTime='" + durTime + '\'' +
                '}';
    }
    // KEEP METHODS END

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.content);
        dest.writeString(this.person);
        dest.writeString(this.relationship);
        dest.writeString(this.icon);
        dest.writeString(this.date);
        dest.writeString(this.remind);
        dest.writeString(this.remark);
        dest.writeString(this.durTime);
    }

    protected Ann(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.content = in.readString();
        this.person = in.readString();
        this.relationship = in.readString();
        this.icon = in.readString();
        this.date = in.readString();
        this.remind = in.readString();
        this.remark = in.readString();
        this.durTime = in.readString();
    }

    public static final Parcelable.Creator<Ann> CREATOR = new Parcelable.Creator<Ann>() {
        public Ann createFromParcel(Parcel source) {
            return new Ann(source);
        }

        public Ann[] newArray(int size) {
            return new Ann[size];
        }
    };
}
