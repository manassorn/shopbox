package com.manassorn.shopbox.value;

import com.manassorn.shopbox.db.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	
	@DatabaseField
	private int amount;
	
	@DatabaseField(id=true)
	private int id;
	
	@DatabaseField
	private int categoryId;
	
	@DatabaseField
	private double price;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String barcode;
	
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
