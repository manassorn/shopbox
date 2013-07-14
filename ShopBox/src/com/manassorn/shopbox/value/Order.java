package com.manassorn.shopbox.value;

import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
	private LinkedList<OrderProduct> productItems;
	private LinkedList<Supplement> supplements;

	public Order() {
		productItems = new LinkedList<OrderProduct>();
		supplements = new LinkedList<Supplement>();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(productItems);
		dest.writeList(supplements);
	}

	public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
		public Order createFromParcel(Parcel in) {
			return new Order(in);
		}

		public Order[] newArray(int size) {
			return new Order[size];
		}
	};

	private Order(Parcel in) {
		productItems = new LinkedList<OrderProduct>();
		in.readList(productItems, BillProductItem.class.getClassLoader());
		supplements = new LinkedList<Supplement>();
		in.readList(supplements, Supplement.class.getClassLoader());
	}

	public List<Supplement> getSupplements() {
		return supplements;
	}

	public List<OrderProduct> getOrderProducts() {
		return productItems;
	}
	
	public int getOrderProductCount() {
		return getOrderProducts().size();
	}
}
