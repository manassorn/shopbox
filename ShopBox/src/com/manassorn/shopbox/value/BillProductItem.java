package com.manassorn.shopbox.value;

import com.manassorn.shopbox.db.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class BillProductItem extends BillItem implements Comparable<BillProductItem> {
	@DatabaseField
	private int productId;
	@DatabaseField
	private String productName;
	@DatabaseField
	private double productPrice;
	@DatabaseField
	private int amount;
	//TODO-remove total
	@DatabaseField
	private double total;

	public BillProductItem() {

	}

	public BillProductItem(Product product) {
		this(product, 0);
	}

	public BillProductItem(Product product, int amount) {
		this(0, product, amount);
	}

	public BillProductItem(int sequence, Product product, int amount) {
		this.sequence = sequence;
		this.productId = product.getId();
		this.productName = product.getName();
		this.productPrice = product.getPrice();
		this.amount = amount;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getTotal() {
		return getProductPrice() * getAmount();
	}

	@Override
	public int compareTo(BillProductItem another) {
		return this.productId - another.productId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(billId);
		dest.writeInt(sequence);
		dest.writeInt(productId);
		dest.writeString(productName);
		dest.writeDouble(productPrice);
		dest.writeInt(amount);
		dest.writeDouble(total);
	}

	public static final Parcelable.Creator<BillProductItem> CREATOR = new Parcelable.Creator<BillProductItem>() {
		public BillProductItem createFromParcel(Parcel in) {
			return new BillProductItem(in);
		}

		public BillProductItem[] newArray(int size) {
			return new BillProductItem[size];
		}
	};

	private BillProductItem(Parcel in) {
		billId = in.readInt();
		sequence = in.readInt();
		productId = in.readInt();
		productName = in.readString();
		productPrice = in.readDouble();
		amount = in.readInt();
		total = in.readDouble();
	}

}
