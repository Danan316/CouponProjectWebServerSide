package com.couponProject.main;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couponProject.controllers.AdminController;
import com.couponProject.controllers.CompanyController;
import com.couponProject.controllers.CustomerController;

@RestController
@RequestMapping("/login")
public class LoginManager {

	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private AdminController adminController;

	/* - CHECK IF WE CAN SKIP THE CONTROLLERS "LOGIN" FUNCTIONS AND USE THE SERVICES FUNCTIONS DIRECTLY!!
	 * Checking the email and password in the argument according to the case
	 * that matches the client type and returning the appropriate service.
	 */
	@PostMapping("")
	public ResponseEntity<?> login(@RequestBody Credentials cred) {
		boolean status = false;
		System.out.println(new Date()+": Got a new login: "+cred);
		switch (cred.getRole()) {
		case "admin":
			if(adminController.login(cred.getEmail(), cred.getPassword())) {
				status = true;
			}
			break;
		case "company":
			CompanyController compController = ctx.getBean(CompanyController.class);
			if(compController.login(cred.getEmail(), cred.getPassword())) {
				status = true;
			}
			break;
		case "customer":
			CustomerController custController = ctx.getBean(CustomerController.class);
			if(custController.login(cred.getEmail(), cred.getPassword())) {
				status = true;
			}
			break;
		}
		if(status) {
			String token = tokenManager.getNewToken(cred.getRole());
			return new ResponseEntity<String>(token, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Unsuccessful Login!", HttpStatus.BAD_REQUEST);
	}
}