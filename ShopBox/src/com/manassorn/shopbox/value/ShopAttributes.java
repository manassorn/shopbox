package com.manassorn.shopbox.value;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public class ShopAttributes implements Parcelable {

	@DatabaseField(id=true, generatedId=true)
	private int id;
	@DatabaseField
	private String shopName;
	@DatabaseField
	private String branchName;
	@DatabaseField
	private String taxId;
	
	public ShopAttributes() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(shopName);
		dest.writeString(branchName);
		dest.writeString(taxId);
	}

	public static final Parcelable.Creator<ShopAttributes> CREATOR = new Parcelable.Creator<ShopAttributes>() {
		public ShopAttributes createFromParcel(Parcel in) {
			return new ShopAttributes(in);
		}

		public ShopAttributes[] newArray(int size) {
			return new ShopAttributes[size];
		}
	};

	protected ShopAttributes(Parcel in) {
		id = in.readInt();
		shopName = in.readString();
		branchName = in.readString();
		taxId = in.readString();
	}
}
