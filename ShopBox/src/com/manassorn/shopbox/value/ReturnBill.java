package com.manassorn.shopbox.value;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public class ReturnBill extends Bill {
	@DatabaseField
	private int sellBillId;

	public ReturnBill() {
		super();
	}
	
	public ReturnBill(ArrayList<BillItem> billItems) {
		super(billItems);
	}

	public int getSellBillId() {
		return sellBillId;
	}

	public void setSellBillId(int sellBillId) {
		this.sellBillId = sellBillId;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(sellBillId);
	}

	public static final Parcelable.Creator<ReturnBill> CREATOR = new Parcelable.Creator<ReturnBill>() {
		public ReturnBill createFromParcel(Parcel in) {
			return new ReturnBill(in);
		}

		public ReturnBill[] newArray(int size) {
			return new ReturnBill[size];
		}
	};

	ReturnBill(Parcel in) {
		super(in);
		sellBillId = in.readInt();
	}
}
