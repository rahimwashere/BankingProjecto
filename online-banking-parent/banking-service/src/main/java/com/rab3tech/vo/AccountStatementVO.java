package com.rab3tech.vo;

import java.sql.Timestamp;

public class AccountStatementVO {
private int transactionId;
private String accountNumber;
private String debitCredit;
private int amount;
private Timestamp date;
private String remark;
public int getTransactionId() {
	return transactionId;
}
public void setTransactionId(int transactionId) {
	this.transactionId = transactionId;
}
public String getAccountNumber() {
	return accountNumber;
}
public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
}
public String getDebitCredit() {
	return debitCredit;
}
public void setDebitCredit(String debitCredit) {
	this.debitCredit = debitCredit;
}
public float getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
public Timestamp getDate() {
	return date;
}
public void setDate(Timestamp date) {
	this.date = date;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
@Override
public String toString() {
	return "AccountStatementVO [transactionId=" + transactionId + ", accountNumber=" + accountNumber + ", debitCredit="
			+ debitCredit + ", amount=" + amount + ", date=" + date + ", remark=" + remark + "]";
}
public void setBalance(float avBalance) {
	// TODO Auto-generated method stub
	
}


}
