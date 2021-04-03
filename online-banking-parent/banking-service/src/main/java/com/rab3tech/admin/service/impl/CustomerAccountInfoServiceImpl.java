package com.rab3tech.admin.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.admin.service.CustomerAccountInfoService;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.vo.AccountStatementVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.TransactionVO;
@Service
@Transactional
public class CustomerAccountInfoServiceImpl implements CustomerAccountInfoService{

	
	@Autowired
	private MagicCustomerRepository customerRepository;
	
	@Autowired
	CustomerAccountInfoRepository customerAccountInfoRepository;
	
	@Autowired
	PayeeInfoRepository payeeInfoRepository;
	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public List<CustomerVO> findCustomers() {		
	List<CustomerVO> customerVos = new ArrayList<CustomerVO>();
		List<Customer> customers = customerRepository.findAll();//find all the customers present in the database
		for (Customer customer:customers) {//iterate through each customer
			///first check if this customer is present in the account table
	Optional<CustomerAccountInfo> accountInfo = customerAccountInfoRepository.findByCustomerId(customer.getEmail());
		if(!accountInfo.isPresent()) { ///check if you get a row for accountInfo
			CustomerVO customerVO = new CustomerVO();
			BeanUtils.copyProperties(customer, customerVO);
			customerVos.add(customerVO);
		}
		
		
	
	}
		return customerVos;

	}


	@Override
	public void createAccount(int customerId) {
CustomerAccountInfo accountInfo = new CustomerAccountInfo();
	String accountNumber = "1000" + customerId;
	accountInfo.setAccountNumber(accountNumber);
	accountInfo.setAccountType("SAVING");
	accountInfo.setAvBalance(5000);
	accountInfo.setBranch("Fremont");
	accountInfo.setCurrency("USD");
	/////customer's email through customerRepository with ID
	Optional<Customer> customer = customerRepository.findById(customerId);
	accountInfo.setCustomerId(customer.get().getEmail());
	accountInfo.setStatusAsOf(new Date());
	accountInfo.setTavBalance(1000);
		System.out.println("Account info ------" + accountInfo);
	customerAccountInfoRepository.save(accountInfo);		
	}


	@Override
	public CustomerAccountInfoVO findCustomer(String customerId) {
		Optional<CustomerAccountInfo> account = customerAccountInfoRepository.findByCustomerId(customerId);
		CustomerAccountInfoVO accountInfoVO = new CustomerAccountInfoVO();
		if(account.isPresent()) {
			BeanUtils.copyProperties(account.get(), accountInfoVO);
		}
		return accountInfoVO;
	}


	

	@Override
	public String saveTransaction(TransactionVO transactionVO) {
		String message = null;
		///check if debitor has a valid account
		Optional<CustomerAccountInfo> accountDebitor = customerAccountInfoRepository.findByCustomerId(transactionVO.getCustomerID());
		if (accountDebitor.isPresent()) {
			//debitor has enough balance
			if (transactionVO.getAmount() > accountDebitor.get().getAvBalance()) {
				message = "debitor does not have a sufficient balance";
				return message;
			}
		}else {
			message = "creditor does have a valid account";
			return message;
		}
		// Check creditor account is valid  or not
		Optional<CustomerAccountInfo> accountCreditor = customerAccountInfoRepository.findByAccountNumber(transactionVO.getCredit_account_number());
		if (!accountCreditor.isPresent()) {
			message = "debitor doesn't have a valid account";
			return message;
			
			}
		
		Optional<PayeeInfo> payee = payeeInfoRepository.findByCustomerIdAndPayeeAccountNo(transactionVO.getCustomerID(),transactionVO.getCredit_account_number());
		if (!payee.isPresent()) {
			message = "Debiotrs is not valid for the creditor";
			return message;
			
		}
		
		float debitorAmount =  accountDebitor.get().getAvBalance() - transactionVO.getAmount();
		accountDebitor.get().setAvBalance(debitorAmount);
		accountDebitor.get().setTavBalance(debitorAmount);
		accountDebitor.get().setStatusAsOf(new Date());
		customerAccountInfoRepository.save(accountDebitor.get());
		
	
		float creditorAmount =  accountCreditor.get().getAvBalance() + transactionVO.getAmount();
		accountCreditor.get().setAvBalance(creditorAmount);
		accountCreditor.get().setTavBalance(creditorAmount);
		accountCreditor.get().setStatusAsOf(new Date());
	customerAccountInfoRepository.save(accountCreditor.get());
	// check debitor has a valid account
	// update transaction table
	Transaction transaction = new Transaction();
	BeanUtils.copyProperties(transactionVO, transaction);
	transaction.setDebit_account_number(accountCreditor.get().getAccountNumber());
	transaction.setTransactionDate(new Timestamp(new Date().getTime()));
		transactionRepository.save(transaction);
		message = "amount transfer has been done successfully!!!";
		return message;
	}


	@Override
	public List<AccountStatementVO> getStatement(String email) {
		String message= null; 
		List <AccountStatementVO> accountStatementVO = new ArrayList <AccountStatementVO>();
		Optional<CustomerAccountInfo> customerAccountInfo= customerAccountInfoRepository.findByCustomerId(email);
		List<Transaction> customerAccountTransaction=transactionRepository.getAccountStatementData(customerAccountInfo.get().getAccountNumber(), customerAccountInfo.get().getAccountNumber());
		for (Transaction customerAccounts:customerAccountTransaction) {
			AccountStatementVO statement= new AccountStatementVO();
			statement.setTransactionId(customerAccounts.getId());
			statement.setRemark(customerAccounts.getRemark());
			statement.setAmount(customerAccounts.getAmount());
			statement.setBalance(customerAccountInfo.get().getAvBalance());
//			statement.setBalance(customerAccounts.get);
			
			if (customerAccountInfo.get().getAccountNumber().equals(customerAccounts.getCredit_account_number())) {
			statement.setDebitCredit("credit");	
			statement.setAccountNumber(customerAccounts.getDebit_account_number());
			} else {
				statement.setDebitCredit("debit");
				statement.setAccountNumber(customerAccounts.getCredit_account_number());
				
			}
			accountStatementVO.add(statement);
			System.out.println(accountStatementVO+ "-----________-------");
		}
		return accountStatementVO;
	}
	
}
