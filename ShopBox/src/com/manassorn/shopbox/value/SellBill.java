package com.manassorn.shopbox.value;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public class SellBill extends Bill {
	@DatabaseField(id=true)
	private double receiveMoney;

	public SellBill() {
		super();
	}
	
	public SellBill(ArrayList<BillItem> billItems) {
		super(billItems);
	}
	
	public double getReceiveMoney() {
		return receiveMoney;
	}
	
	public void setReceiveMoney(double receiveMoney) {
		this.receiveMoney = receiveMoney;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeDouble(receiveMoney);
	}

	public static final Parcelable.Creator<SellBill> CREATOR = new Parcelable.Creator<SellBill>() {
		public SellBill createFromParcel(Parcel in) {
			return new SellBill(in);
		}

		public SellBill[] newArray(int size) {
			return new SellBill[size];
		}
	};

	SellBill(Parcel in) {
		super(in);
		receiveMoney = in.readDouble();
	}
}
