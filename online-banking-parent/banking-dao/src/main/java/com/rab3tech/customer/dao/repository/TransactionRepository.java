package com.rab3tech.customer.dao.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Integer>{

		@Query("Select t from Transaction t where t.credit_account_number=:pcredit_account_number or t.debit_account_number=:pdebit_account_number")
		List<Transaction> getAccountStatementData(@Param("pcredit_account_number") String credit_account_number, @Param("pdebit_account_number") String debit_account_number);

		
}
