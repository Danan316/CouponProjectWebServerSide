package com.couponProject.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couponProject.main.TokenManager;
import com.couponProject.repositories.CompanyRepository;
import com.couponProject.repositories.CouponRepository;
import com.couponProject.repositories.CustomerRepository;

@Service
@Transactional
public abstract class ClientController {
	
	@Autowired
	protected CouponRepository couponRepository;
	@Autowired
	protected CompanyRepository companyRepository;
	@Autowired
	protected CustomerRepository customerRepository;
	@Autowired
	protected TokenManager tokenManager;
	
	public ClientController() {}
	
	public abstract boolean login(String email, String password);
	
}
