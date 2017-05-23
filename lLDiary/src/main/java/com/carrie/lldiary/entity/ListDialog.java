package com.carrie.lldiary.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.carrie.lldiary.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/4 0004.
 */
public class ListDialog implements Parcelable {
    public String name;
    public String icon;
    public String balance;

    public ListDialog(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public ListDialog(String name) {
        this.name = name;
    }

    public ListDialog() {
    }

    public ListDialog(String name, String icon, String balance) {
        this.name = name;
        this.icon = icon;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "ListDialog{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    public static ArrayList<ListDialog> fillPlanIcons(Context context, ArrayList<ListDialog> lists) {
        ListDialog entity = new ListDialog("plan_priority_1", context.getString(R.string.edit_plan_import1));
        lists.add(entity);

        ListDialog entity2 = new ListDialog("plan_priority_2", context.getString(R.string.edit_plan_import2));
        lists.add(entity2);

        ListDialog entity3 = new ListDialog("plan_priority_3", context.getString(R.string.edit_plan_import3));
        lists.add(entity3);

        ListDialog entity4 = new ListDialog("plan_priority_4", context.getString(R.string.edit_plan_import4));
        lists.add(entity4);

        return lists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.balance);
    }

    protected ListDialog(Parcel in) {
        this.name = in.readString();
        this.icon = in.readString();
        this.balance = in.readString();
    }

    public static final Parcelable.Creator<ListDialog> CREATOR = new Parcelable.Creator<ListDialog>() {
        public ListDialog createFromParcel(Parcel source) {
            return new ListDialog(source);
        }

        public ListDialog[] newArray(int size) {
            return new ListDialog[size];
        }
    };
}
