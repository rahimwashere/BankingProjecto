package com.rab3tech.customer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerController {
	@Autowired
	PayeeInfoService payeeInfoService;
	@Autowired
	private EmailService emailService;
	@GetMapping(value="/customer/findPayee/{payeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public PayeeInfoVO managePayee(@PathVariable int payeeId ) {
	 PayeeInfoVO payeeInfoVO = payeeInfoService.findPayeeById(payeeId);
			return payeeInfoVO;
	}
	
	@GetMapping(value="/customer/managePayee", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<PayeeInfoVO> findAllPayee() {
			List<PayeeInfoVO> payeeInfoVO = payeeInfoService.findAll();
			return payeeInfoVO;
	}
	@GetMapping(value="/customer/deletePayee/{payeeId}") //http://localhost:999/v3/customer/deletePayee
	public ApplicationResponseVO deletePayee(@PathVariable int payeeId) {
		ApplicationResponseVO payeeVO = new ApplicationResponseVO();
		payeeInfoService.deletePayeeById(payeeId);
		payeeVO.setCode(200);
		payeeVO.setMessage("Payee has been deleted successfully");
		payeeVO.setStatus("Success");
		return payeeVO;
	}
	@PostMapping(value="/customer/editPayee", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ApplicationResponseVO editPayee(@RequestBody PayeeInfoVO payeeInfoVo) {
		ApplicationResponseVO payeeVO = new ApplicationResponseVO();
		payeeInfoService.editPayeeById(payeeInfoVo);
		payeeVO.setCode(200);
		payeeVO.setMessage("Payee has been updated successfully");
		payeeVO.setStatus("Success");
		
		
		///Send email after success
		PayeeInfoVO payee = managePayee(payeeInfoVo.getId());
		EmailVO email = new EmailVO(payee.getCustomerId(), "shahzadladiwala123@gmail.com", "Edit Payee Email", payee.getPayeeAccountNo(), payee.getPayeeName());
		emailService.sendEditPayeeEmail(email);
		
		
		return payeeVO;
	}

	
	
}