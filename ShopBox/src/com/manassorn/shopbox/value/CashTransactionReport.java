package com.manassorn.shopbox.value;

import com.manassorn.shopbox.db.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class CashTransactionReport extends Report {

	@DatabaseField
	private TransactionType transactionType;
	@DatabaseField
	private double amount;
	@DatabaseField
	private double cashRemain;
	
	public enum TransactionType {
		TRANSFER, MISSING
	}
	
	public CashTransactionReport() {
		super();
	}
	
	public CashTransactionReport(TransactionType transactionType, double cashAmount) {
		super();
		this.transactionType = transactionType;
		this.amount = cashAmount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double mount) {
		this.amount = mount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setCashTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public double getCashRemain() {
		return cashRemain;
	}

	public void setCashRemain(double cashRemain) {
		this.cashRemain = cashRemain;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(transactionType.name());
		dest.writeDouble(amount);
	}

	public static final Parcelable.Creator<CashTransactionReport> CREATOR = new Parcelable.Creator<CashTransactionReport>() {
		public CashTransactionReport createFromParcel(Parcel in) {
			return new CashTransactionReport(in);
		}

		public CashTransactionReport[] newArray(int size) {
			return new CashTransactionReport[size];
		}
	};

	protected CashTransactionReport(Parcel in) {
		super(in);
		transactionType = TransactionType.valueOf(in.readString());
		amount = in.readDouble();
	}
}
