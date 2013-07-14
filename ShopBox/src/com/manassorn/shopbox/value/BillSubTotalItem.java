package com.manassorn.shopbox.value;

import android.os.Parcel;
import android.os.Parcelable;

public class BillSubTotalItem extends BillItem {
	private double subTotal;
	
	public BillSubTotalItem() {
		
	}
	
	public BillSubTotalItem(double subTotal) {
		this.subTotal = subTotal;
	}
	
	public BillSubTotalItem(int sequence, double subTotal) {
		this.sequence = sequence;
		this.subTotal = subTotal;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(billId);
		dest.writeInt(sequence);
		dest.writeDouble(subTotal);
	}

	public static final Parcelable.Creator<BillSubTotalItem> CREATOR = new Parcelable.Creator<BillSubTotalItem>() {
		public BillSubTotalItem createFromParcel(Parcel in) {
			return new BillSubTotalItem(in);
		}

		public BillSubTotalItem[] newArray(int size) {
			return new BillSubTotalItem[size];
		}
	};

	private BillSubTotalItem(Parcel in) {
		billId = in.readInt();
		sequence = in.readInt();
		subTotal = in.readDouble();
	}
}
