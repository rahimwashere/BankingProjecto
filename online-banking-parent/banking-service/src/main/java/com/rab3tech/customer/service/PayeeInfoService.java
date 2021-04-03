package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.PayeeInfoVO;

public interface PayeeInfoService {
		String savePayee(PayeeInfoVO payeeInfoVO);
		List<PayeeInfoVO> findByCustomerId(String username);
		List<PayeeInfoVO> findAll();
		PayeeInfoVO findPayeeById(int payeeId);
		void deletePayeeById(int payeeId);
		void editPayeeById(PayeeInfoVO payeInfoVo);
}
