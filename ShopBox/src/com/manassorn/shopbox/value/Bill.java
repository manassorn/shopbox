package com.manassorn.shopbox.value;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public class Bill implements Parcelable {
	@DatabaseField(id=true, generatedId=true)
	private int id;
	@DatabaseField
	private Date createdTime;
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
		this.createdTime = new Date();
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
		billItems = new ArrayList<BillItem>();
		in.readList(billItems, BillItem.class.getClassLoader());
	}

	public int getId() {
		return id;
	}

	public void setId(int billId) {
		this.id = billId;
	}
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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
