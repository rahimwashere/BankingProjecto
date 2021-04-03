package com.rab3tech.vo;

import java.security.Timestamp;

public class TransactionVO {
	private int id;
	private int amount;
	private String credit_account_number;
	private String debit_account_number;
	private String remark;
	private Timestamp transactionDate;
	private String customerID;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCredit_account_number() {
		return credit_account_number;
	}
	public void setCredit_account_number(String credit_account_number) {
		this.credit_account_number = credit_account_number;
	}
	public String getDebit_account_number() {
		return debit_account_number;
	}
	public void setDebit_account_number(String debit_account_number) {
		this.debit_account_number = debit_account_number;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
}