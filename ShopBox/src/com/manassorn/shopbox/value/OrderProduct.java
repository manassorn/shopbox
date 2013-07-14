package com.manassorn.shopbox.value;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderProduct implements Parcelable, Comparable<OrderProduct> {
	private Product product;
	private int amount;

	public OrderProduct() {
	
	}
	public OrderProduct(Product product) {
		this.product = product;
	}
	public OrderProduct(Product product, int amount) {
		this.product = product;
		this.amount = amount;
	}
	@Override
	public int compareTo(OrderProduct another) {
		return (int) (this.product.getId() - another.product.getId());
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
		dest.writeParcelable(product, flags);
		dest.writeInt(amount);
	}

	public static final Parcelable.Creator<OrderProduct> CREATOR = new Parcelable.Creator<OrderProduct>() {
		public OrderProduct createFromParcel(Parcel in) {
			return new OrderProduct(in);
		}

		public OrderProduct[] newArray(int size) {
			return new OrderProduct[size];
		}
	};

	private OrderProduct(Parcel in) {
		product = in.readParcelable(Product.class.getClassLoader());
		amount = in.readInt();
	}

}
