package com.psychologicalcounseling.login.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.psychologicalcounseling.login.dao.AddPhoneByAlipayIdDaoImpl;

@Service
public class AddPhoneByAlipayIdServiceImpl {
    @Resource
    private AddPhoneByAlipayIdDaoImpl apbaidi;
    
	public void addPhone(String userPhone,String alipayUserId) {
		apbaidi.updatePhone(userPhone,alipayUserId);
	}
	

}
