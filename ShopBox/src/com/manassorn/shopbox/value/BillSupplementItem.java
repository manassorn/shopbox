package com.manassorn.shopbox.value;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;
import com.manassorn.shopbox.value.Supplement.SupplementType;

public class BillSupplementItem extends BillItem {
	@DatabaseField
	private int supplementId;
	@DatabaseField
	private String supplementName;
	@DatabaseField
	private SupplementType supplementType;
	@DatabaseField
	private double supplementPercent;
	@DatabaseField
	private double supplementConstant;
	@DatabaseField
	private int supplementPriority;
	@DatabaseField
	private double total;
	
	public BillSupplementItem() {
		
	}
	
	public BillSupplementItem(Supplement supplement) {
		this(0, supplement);
	}
	
	public BillSupplementItem(int sequence, Supplement supplement) {
		this.sequence = sequence;
		this.supplementId = supplement.getId();
		this.supplementName = supplement.getName();
		this.supplementType = supplement.getType();
		this.supplementPercent = supplement.getPercent();
		this.supplementConstant = supplement.getConstant();
		this.supplementPriority = supplement.getPriority();
	}

	public int getSupplementId() {
		return supplementId;
	}

	public void setSupplementId(int supplementId) {
		this.supplementId = supplementId;
	}

	public String getSupplementName() {
		return supplementName;
	}

	public void setSupplementName(String supplementName) {
		this.supplementName = supplementName;
	}

	public SupplementType getSupplementType() {
		return supplementType;
	}

	public void setSupplementType(SupplementType supplementType) {
		this.supplementType = supplementType;
	}

	public double getSupplementPercent() {
		return supplementPercent;
	}

	public void setSupplementPercent(double supplementPercent) {
		this.supplementPercent = supplementPercent;
	}

	public double getSupplementConstant() {
		return supplementConstant;
	}

	public void setSupplementConstant(double supplementConstant) {
		this.supplementConstant = supplementConstant;
	}

	public int getSupplementPriority() {
		return supplementPriority;
	}

	public void setSupplementPriority(int supplementPriority) {
		this.supplementPriority = supplementPriority;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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
		dest.writeInt(supplementId);
		dest.writeString(supplementName);
		dest.writeString(supplementType.name());
		dest.writeDouble(supplementPercent);
		dest.writeDouble(supplementConstant);
		dest.writeInt(supplementPriority);
		dest.writeDouble(total);
	}

	public static final Parcelable.Creator<BillSupplementItem> CREATOR = new Parcelable.Creator<BillSupplementItem>() {
		public BillSupplementItem createFromParcel(Parcel in) {
			return new BillSupplementItem(in);
		}

		public BillSupplementItem[] newArray(int size) {
			return new BillSupplementItem[size];
		}
	};

	private BillSupplementItem(Parcel in) {
		billId = in.readInt();
		sequence = in.readInt();
		supplementId = in.readInt();
		supplementName = in.readString();
		supplementType = SupplementType.valueOf(in.readString());
		supplementPercent = in.readDouble();
		supplementConstant = in.readDouble();
		supplementPriority = in.readInt();
		total = in.readDouble();
	}
}
