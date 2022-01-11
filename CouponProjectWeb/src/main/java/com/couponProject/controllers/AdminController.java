package com.couponProject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couponProject.entities.Company;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CompanyAlreadyExistsException;
import com.couponProject.exceptions.CompanyNotFoundException;
import com.couponProject.exceptions.CustomerAlreadyExistsException;
import com.couponProject.exceptions.CustomerNotFoundException;
import com.couponProject.services.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController extends ClientController {

	@Autowired
	private AdminService adminService;

	public AdminController() {
	}

	@Override
	public boolean login(String email, String password) {
		if (adminService.login(email, password)) {
			return true;
		}
		return false;
	}

	@PostMapping("/addCompany")
	public ResponseEntity<?> addCompany(@RequestBody Company company, @RequestHeader("token") String token) throws CompanyAlreadyExistsException {
		System.out.println("Got a new company: "+company+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				Company savedCompany = adminService.addCompany(company);
				return new ResponseEntity<Integer>(savedCompany.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateCompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company, @RequestHeader("token") String token) throws CompanyNotFoundException, CompanyAlreadyExistsException {
		System.out.println("Got a company to update: "+company+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				Company updatedCompany = adminService.updateCompany(company);
				return new ResponseEntity<Integer>(updatedCompany.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
//	@DeleteMapping - Ask Rami
	@PostMapping("/deleteCompany")
	public ResponseEntity<?> deleteCompany(@RequestBody int companyID, @RequestHeader("token") String token) throws CompanyNotFoundException {
		System.out.println("Got a company's ID to delete: #"+companyID+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				boolean status = adminService.deleteCompany(companyID);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getAllCompanies")
	public ResponseEntity<?> getAllCompanies(String token) {
		System.out.println("getting all the companies, token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				List<Company> allCompanies = adminService.getAllCompanies();
				return new ResponseEntity<List<Company>>(allCompanies, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getOneCompany")
	public ResponseEntity<?> getOneCompany(@RequestBody int companyID, @RequestHeader("token") String token) throws CompanyNotFoundException {
		System.out.println("getting a company by ID: #" + companyID + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				Company requestedCompany = adminService.getOneCompany(companyID);
				return new ResponseEntity<Company>(requestedCompany, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer, @RequestHeader("token") String token) throws CustomerAlreadyExistsException {
		System.out.println("Got a new customer: "+customer+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				Customer savedCustomer = adminService.addCustomer(customer);
				return new ResponseEntity<Integer>(savedCustomer.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @RequestHeader("token") String token) throws CustomerNotFoundException, CustomerAlreadyExistsException {
		System.out.println("Got a customer to update: "+customer+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				Customer updatedCustomer = adminService.updateCustomer(customer);
				return new ResponseEntity<Integer>(updatedCustomer.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
//	@DeleteMapping - Ask Rami
	@PostMapping("/deleteCustomer")
	public ResponseEntity<?> deleteCustomer(@RequestBody int customerID, @RequestHeader("token") String token) throws CustomerNotFoundException {
		System.out.println("Got a customer's ID to delete: #"+customerID+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				boolean status = adminService.deleteCustomer(customerID);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<?> getAllCustomers(@RequestHeader("token") String token) {
		System.out.println("getting all the customers, token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				List<Customer> allCustomers = adminService.getAllCustomers();
				return new ResponseEntity<List<Customer>>(allCustomers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getOneCustomer")
	public ResponseEntity<?> getOneCustomer(@RequestBody int customerID, @RequestHeader("token") String token) throws CustomerNotFoundException {
		System.out.println("getting a customer by ID: #" + customerID + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("admin")) {
				Customer requestedCustomer = adminService.getOneCustomer(customerID);
				return new ResponseEntity<Customer>(requestedCustomer, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

}