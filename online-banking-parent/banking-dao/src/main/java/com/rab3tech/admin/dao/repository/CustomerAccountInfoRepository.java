package com.rab3tech.admin.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rab3tech.dao.entity.CustomerAccountInfo;
@Repository
public interface CustomerAccountInfoRepository extends JpaRepository<CustomerAccountInfo, Integer>{
	public Optional<CustomerAccountInfo>findByCustomerId(String emailId);
	public Optional<CustomerAccountInfo>findByAccountNumber(String accountNumber);
	
}
