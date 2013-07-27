package com.manassorn.shopbox.value;

import java.text.DecimalFormat;

import com.manassorn.shopbox.db.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class Supplement implements Parcelable, Comparable<Supplement> {
	@DatabaseField
	private int id;
	@DatabaseField
	private SupplementType type;
	@DatabaseField
	private String name;
	@DatabaseField
	private double percent;
	@DatabaseField
	private double constant;
	@DatabaseField
	private int priority;

	public enum SupplementType {
		PERCENT, CONSTANT
	}

	public Supplement() {

	}
	
	public Supplement(BillSupplementItem supplementItem) {
		id = supplementItem.getSupplementId();
		type = supplementItem.getSupplementType();
		name = supplementItem.getSupplementName();
		percent = supplementItem.getSupplementPercent();
		constant = supplementItem.getSupplementConstant();
		priority = supplementItem.getSupplementPriority();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public SupplementType getType() {
		return type;
	}

	public void setType(SupplementType type) {
		this.type = type;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getConstant() {
		return constant;
	}

	public void setConstant(double constant) {
		this.constant = constant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double calcSupplementTotal(double subTotal) {
		switch (getType()) {
			case PERCENT:
				return subTotal * getPercent() / 100;
			case CONSTANT:
				return getConstant();
		}
		return 0;
	}

	public String formatValue() {
		return Supplement.formatValue(getType(), getPercent(), getConstant());
	}

	public static String formatValue(SupplementType type, double percent, double constant) {
		DecimalFormat df = new DecimalFormat("+0.##;-#");
		switch (type) {
			case PERCENT:
				return df.format(percent) + "%";
			case CONSTANT:
				return df.format(constant) + "ß";
		}
		return "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type.name());
		dest.writeString(name);
		dest.writeDouble(percent);
		dest.writeDouble(constant);
		dest.writeInt(priority);
	}

	public static final Parcelable.Creator<Supplement> CREATOR = new Parcelable.Creator<Supplement>() {
		public Supplement createFromParcel(Parcel in) {
			return new Supplement(in);
		}

		public Supplement[] newArray(int size) {
			return new Supplement[size];
		}
	};

	private Supplement(Parcel in) {
		type = SupplementType.valueOf(in.readString());
		name = in.readString();
		percent = in.readDouble();
		constant = in.readDouble();
		priority = in.readInt();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(constant);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		temp = Double.doubleToLongBits(percent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + priority;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Supplement other = (Supplement) obj;
		if (Double.doubleToLongBits(constant) != Double.doubleToLongBits(other.constant))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(percent) != Double.doubleToLongBits(other.percent))
			return false;
		if (priority != other.priority)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int compareTo(Supplement another) {
		return getPriority() - another.getPriority();
	}
}
