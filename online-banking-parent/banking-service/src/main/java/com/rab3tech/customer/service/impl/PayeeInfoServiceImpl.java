package com.rab3tech.customer.service.impl;


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
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;
@Service
@Transactional
public class PayeeInfoServiceImpl implements PayeeInfoService{
	@Autowired
	private PayeeInfoRepository payeeRepository;
	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
@Autowired
private MagicCustomerRepository magicCustomerRepository;
	@Override
	public String savePayee(PayeeInfoVO payeeInfoVO) {
		//logged in user has account or not 
		String message = null;
		Optional<CustomerAccountInfo> customerAccount=customerAccountInfoRepository.findByCustomerId(payeeInfoVO.getCustomerId());
		if(customerAccount.isPresent()) {
			// is the account a saving account?
			if(!customerAccount.get().getAccountType().equals("SAVING")) {
				message = "You don't have a valid saving account brother you need to figure things out much better but I believe in you";
				return message;
				}
		}	else {
		message = "You don't have a valid account brother you need to figure things out much better";
			return message;
			
		}
		// Payee account number is valid
		Optional<CustomerAccountInfo> payeeAccount = customerAccountInfoRepository.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());
if(!payeeAccount.isPresent()) {		
	message = "Payee account number is not valid so you cant add the payee";
				return message;
			
		}
//user account should not be the same as the payee account number

if(customerAccount.get().getAccountNumber().equals(payeeAccount.get().getAccountNumber())) {
	message = "Payee account number cannot be the same as the user account number";
	return message;
}
// Check payee name from database
Optional<Customer> customer = magicCustomerRepository.findByEmail(payeeAccount.get().getCustomerId());

if(!customer.get().getName().equalsIgnoreCase(payeeInfoVO.getPayeeName())) {
	message="Payee Name Is not correct";
	return message;
	
}
//cannot add same payee again
Optional<PayeeInfo> payeeAcc = payeeRepository.findByCustomerIdAndPayeeAccountNo(payeeInfoVO.getCustomerId(), payeeInfoVO.getPayeeAccountNo());
if(payeeAcc.isPresent()) {
	message = "Same payee cannot be added again";
	return message;
}



		PayeeInfo payee = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payee);
		payee.setDoe(new Timestamp(new Date().getTime()));
		payee.setDom(new Timestamp(new Date().getTime()));
		payee.setUrn(1);
		
		System.out.println(payee);
		payeeRepository.save(payee);
		message = "Payee has been successfully added";
		return message;
	}
	@Override
	public List<PayeeInfoVO> findByCustomerId(String username) {
		List<PayeeInfoVO> payees = new ArrayList<PayeeInfoVO>();
		List<PayeeInfo> payeeInfo = payeeRepository.findByCustomerId(username);
		for(PayeeInfo payeelist:payeeInfo) {
			PayeeInfoVO payeeInfoVO = new PayeeInfoVO();
			BeanUtils.copyProperties(payeelist, payeeInfoVO);
			payees.add(payeeInfoVO);
			
		}
		return payees;
	}
	@Override
	public List<PayeeInfoVO> findAll() {
		List <PayeeInfoVO> payees= new ArrayList <PayeeInfoVO> ();
		List <PayeeInfo> payeeInfo= payeeRepository.findAll();
		for(PayeeInfo payeelist:payeeInfo) {
		PayeeInfoVO payeeinfoVo= new PayeeInfoVO();
		BeanUtils.copyProperties(payeelist, payeeinfoVo);
		payees.add( payeeinfoVo);
		}
		return payees;
	}
	@Override
	public PayeeInfoVO findPayeeById(int payeeId) {
		PayeeInfoVO payeeInfoVO = new PayeeInfoVO();
		Optional<PayeeInfo> payee = payeeRepository.findById(payeeId);
		if(payee.isPresent()) {
			BeanUtils.copyProperties(payee.get(), payeeInfoVO);
		}
		return payeeInfoVO;
	}
	@Override
	public void deletePayeeById(int payeeId) {
		payeeRepository.deleteById(payeeId);		
	}
	@Override
	public void editPayeeById(PayeeInfoVO payeeInfoVo) {
		PayeeInfoVO payeeInDB = findPayeeById(payeeInfoVo.getId());
		payeeInDB.setPayeeAccountNo(payeeInfoVo.getPayeeAccountNo());
		payeeInDB.setPayeeName(payeeInfoVo.getPayeeName());
		payeeInDB.setPayeeNickName(payeeInfoVo.getPayeeNickName());
		payeeInDB.setRemarks(payeeInfoVo.getRemarks());
		payeeInDB.setDom(new Timestamp(new Date().getTime()));
		PayeeInfo payee = new PayeeInfo();
		BeanUtils.copyProperties(payeeInDB, payee);
		payeeRepository.save(payee);
		
	}







		

}
