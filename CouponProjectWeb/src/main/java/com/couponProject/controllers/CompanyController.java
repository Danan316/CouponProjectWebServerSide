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

import com.couponProject.entities.Category;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.exceptions.CouponAlreadyExistsException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.DataNotValidException;
import com.couponProject.services.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController extends ClientController {
	
	@Autowired
	private CompanyService companyService;
	
	public CompanyController() {}
	
	@Override
	public boolean login(String email, String password) {
		if (companyService.login(email, password)) {
			return true;
		}
		return false;
	}
	
	@PostMapping("/addCoupon")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon, @RequestHeader("token") String token) throws CouponAlreadyExistsException, CouponNotFoundException, DataNotValidException {
		System.out.println("Got a new coupon: "+coupon+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				Coupon savedCoupon = companyService.addCoupon(coupon);
				return new ResponseEntity<Integer>(savedCoupon.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/updateCoupon")
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon, @RequestHeader("token") String token) throws CouponNotFoundException, CouponAlreadyExistsException, DataNotValidException {
		System.out.println("Got a coupon to update: "+coupon+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				Coupon updatedCoupon = companyService.updateCoupon(coupon);
				return new ResponseEntity<Integer>(updatedCoupon.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
//	@DeleteMapping - Ask Rami
	@PostMapping("/deleteCoupon")
	public ResponseEntity<?> deleteCoupon(@RequestBody int couponID, @RequestHeader("token") String token) throws CouponNotFoundException {
		System.out.println("Got a coupon's ID to delete: #" + couponID + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				boolean status = companyService.deleteCoupon(couponID);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/getOneCoupon")
	public ResponseEntity<?> getOneCoupon(@RequestBody int couponID, @RequestHeader("token") String token) throws CouponNotFoundException {
		System.out.println("getting a coupon by ID: #" + couponID + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				Coupon requestedCoupon = companyService.getOneCoupon(couponID);
				return new ResponseEntity<Coupon>(requestedCoupon, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/getCompanyCoupons")
	public ResponseEntity<?>  getCompanyCoupons(@RequestHeader("token") String token) {
		System.out.println("getting all the company's coupons, token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				List<Coupon> companyCoupons = companyService.getCompanyCoupons();
				return new ResponseEntity<List<Coupon>>(companyCoupons, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCoupons/Category")
	public ResponseEntity<?> getCompanyCoupons(@RequestBody Category category, @RequestHeader("token") String token) {
		System.out.println("getting all the company's coupons with the category: " + category + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				List<Coupon> companyCouponsByCategory = companyService.getCompanyCoupons(category);
				return new ResponseEntity<List<Coupon>>(companyCouponsByCategory, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCoupons/MaxPrice")
	public ResponseEntity<?> getCompanyCoupons(@RequestBody double maxPrice, @RequestHeader("token") String token) {
		System.out.println("getting all the company's coupons with the max price: " + maxPrice + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				List<Coupon> companyCouponsByMaxPrice = companyService.getCompanyCoupons(maxPrice);
				return new ResponseEntity<List<Coupon>>(companyCouponsByMaxPrice, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCoupons/MinPrice")
	public ResponseEntity<?> getCompanyCouponsByMinPrice(@RequestBody double minPrice, @RequestHeader("token") String token) {
		System.out.println("getting all the company's coupons with the min price: " + minPrice + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				List<Coupon> companyCouponsByMinPrice = companyService.getCompanyCouponsByMinPrice(minPrice);
				return new ResponseEntity<List<Coupon>>(companyCouponsByMinPrice, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/getCompanyDetails")
	public ResponseEntity<?> getCompanyDetails(@RequestHeader("token") String token) {
		System.out.println("getting the company's details, token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("company")) {
				Company compmanyDetails = companyService.getCompanyDetails();
				return new ResponseEntity<Company>(compmanyDetails, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
}