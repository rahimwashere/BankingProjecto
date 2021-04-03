package com.rab3tech.admin.service;

import java.util.List;

import com.rab3tech.vo.AccountStatementVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.TransactionVO;

public interface CustomerAccountInfoService {

	public List<CustomerVO> findCustomers();
	void createAccount(int customerId);
	public CustomerAccountInfoVO findCustomer(String customerId);
	public String saveTransaction(TransactionVO transactionVO);
	public List<AccountStatementVO> getStatement(String email);
}