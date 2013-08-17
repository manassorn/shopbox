package com.manassorn.shopbox.value;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public class Bill extends Report {
	@DatabaseField
	private double total;
	@DatabaseField
	private int shopAttributesId;
	
	private ArrayList<BillItem> billItems;

	public Bill() {
		this(new ArrayList<BillItem>());
	}
	
	public Bill(ArrayList<BillItem> billItems) {
		this.billItems = billItems;
		if(billItems.size() > 0) {
			total = ((BillSubTotalItem)billItems.get(billItems.size() - 1)).getSubTotal();
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeDouble(total);
		dest.writeInt(shopAttributesId);
		dest.writeList(billItems);
	}

	public static final Parcelable.Creator<Bill> CREATOR = new Parcelable.Creator<Bill>() {
		public Bill createFromParcel(Parcel in) {
			return new Bill(in);
		}

		public Bill[] newArray(int size) {
			return new Bill[size];
		}
	};

	protected Bill(Parcel in) {
		super(in);
		total = in.readDouble();
		shopAttributesId = in.readInt();
		billItems = new ArrayList<BillItem>();
		in.readList(billItems, BillItem.class.getClassLoader());
	}

	public ArrayList<BillItem> getBillItems() {
		return billItems;
	}

	public void setBillItems(ArrayList<BillItem> billItems) {
		this.billItems = billItems;
	}
	
	public double getTotal() {
//		if(total != 0) return total;
//		if(billItems == null) return 0;
//		total = ((BillSubTotalItem)billItems.get(billItems.size() - 1)).getSubTotal();
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getShopAttributesId() {
		return shopAttributesId;
	}

	public void setShopAttributesId(int shopAttributesId) {
		this.shopAttributesId = shopAttributesId;
	}
}
