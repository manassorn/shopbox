package com.manassorn.shopbox.value;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	private int amount;
	private int id;
	private int categoryId;
	private double price;
	private String name;
	
	public Product() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(amount);
		dest.writeInt(id);
		dest.writeInt(categoryId);
		dest.writeDouble(price);
		dest.writeString(name);
	}
    public static final Parcelable.Creator<Product> CREATOR
		    = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
		    return new Product(in);
		}
		
		public Product[] newArray(int size) {
		    return new Product[size];
		}
	};
	private Product(Parcel in) {
		amount = in.readInt();
		id = in.readInt();
		categoryId = in.readInt();
		price = in.readDouble();
		name = in.readString();
	}
	
}
