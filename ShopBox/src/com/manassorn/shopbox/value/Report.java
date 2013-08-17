package com.manassorn.shopbox.value;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.manassorn.shopbox.db.DatabaseField;

public class Report implements Parcelable {
	@DatabaseField(id=true, generatedId=true)
	private int id;
	@DatabaseField
	private Date createdTime;
	
	public Report() {
		createdTime = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(createdTime.getTime());
	}

	public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
		public Report createFromParcel(Parcel in) {
			return new Report(in);
		}

		public Report[] newArray(int size) {
			return new Report[size];
		}
	};

	protected Report(Parcel in) {
		id = in.readInt();
		createdTime = new Date(in.readLong());
	}
}
