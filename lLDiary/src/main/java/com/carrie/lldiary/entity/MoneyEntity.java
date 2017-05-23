package com.carrie.lldiary.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MoneyEntity implements Parcelable{
	public int id;
	public int classifyIcon;
	public int accountIcon;
	public String content;//消费内容
	public String price;//金额
	public  String income;//收入
	public String expense;//支出
	public String classify;//类别
	public String account;//账户
	public int state;//状态为收入还是支出
	public String date;//发表的时间

	public MoneyEntity() {

	}

	@Override
	public String toString() {
		return "MoneyEntity{" +
				"account='" + account + '\'' +
				", id=" + id +
				", classifyIcon=" + classifyIcon +
				", accountIcon=" + accountIcon +
				", content='" + content + '\'' +
				", price='" + price + '\'' +
				", income='" + income + '\'' +
				", expense='" + expense + '\'' +
				", classify='" + classify + '\'' +
				", state=" + state +
				", date='" + date + '\'' +
				'}';
	}

	public MoneyEntity(int id, int classifyIcon,int accountIcon, String content, String price, String income, String expense, String classify, String account, int state, String date) {
		this.id=id;
		this.classifyIcon = classifyIcon;
		this.accountIcon=accountIcon;
		this.content = content;
		this.price = price;
		this.income = income;
		this.expense = expense;
		this.classify = classify;
		this.account = account;
		this.state = state;
		this.date = date;
	}

	protected MoneyEntity(Parcel in) {
		id=in.readInt();
		classifyIcon = in.readInt();
		accountIcon=in.readInt();
		content = in.readString();
		price = in.readString();
		income = in.readString();
		expense = in.readString();
		classify = in.readString();
		account = in.readString();
		state = in.readInt();
		date = in.readString();

	}

	public static final Creator<MoneyEntity> CREATOR = new Creator<MoneyEntity>() {
		@Override
		public MoneyEntity createFromParcel(Parcel in) {
			MoneyEntity moneyEntity=new MoneyEntity();
			moneyEntity.id=in.readInt();
			moneyEntity.classifyIcon=in.readInt();
			moneyEntity.accountIcon=in.readInt();
			moneyEntity.content=in.readString();
			moneyEntity.price=in.readString();
			moneyEntity.income=in.readString();
			moneyEntity.expense=in.readString();
			moneyEntity.classify=in.readString();
			moneyEntity.account=in.readString();
			moneyEntity.state=in.readInt();
			moneyEntity.date=in.readString();
			return moneyEntity;
		}

		@Override
		public MoneyEntity[] newArray(int size) {
			return new MoneyEntity[size];
		}
	};

	//String content,String date,int state,String classify,String account,String count,String price
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 该方法将类的数据写入外部提供的Parcel中。
	 * @param dest
	 * @param flags
     */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(classifyIcon);
		dest.writeInt(accountIcon);
		dest.writeString(content);
		dest.writeString(price);
		dest.writeString(income);
		dest.writeString(expense);
		dest.writeString(classify);
		dest.writeString(account);
		dest.writeInt(state);
		dest.writeString(date);



	}


}
