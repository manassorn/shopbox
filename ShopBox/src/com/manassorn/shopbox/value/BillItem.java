package com.manassorn.shopbox.value;

import java.util.Comparator;

import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public abstract class BillItem implements Parcelable {
	@DatabaseField
	protected int billId;
	@DatabaseField
	protected int sequence;

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}
	
	public static Comparator<BillItem> comparator() {
		return new Comparator<BillItem>() {

			@Override
			public int compare(BillItem lhs, BillItem rhs) {
				return lhs.sequence - rhs.sequence;
			}
			
		};
	}
}
