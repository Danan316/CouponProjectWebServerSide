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
import com.couponProject.entities.Coupon;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CouponAlreadyExpiredException;
import com.couponProject.exceptions.CouponAlreadyPurchasedException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.CouponNotInStockException;
import com.couponProject.services.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController extends ClientController {
	
	@Autowired
	private CustomerService customerService;

	public CustomerController() {}

	@Override
	public boolean login(String email, String password) {
		if (customerService.login(email, password)) {
			return true;
		}
		return false;
	}

	/*
	 * Checks if the coupon with the ID in the argument exists;
	 * If so, checks if the coupon is in stock, already purchades by the customer and is not expired;
	 * If all the conditions are false, the coupon will be purchased by
	 * the customer and adds a purchase history to the database.
	 */
	@PostMapping("/purchaseCoupon")
	public ResponseEntity<?> purchaseCoupon(@RequestBody int couponID, @RequestHeader("token") String token) throws CouponNotFoundException, CouponNotInStockException, CouponAlreadyPurchasedException, CouponAlreadyExpiredException {
		System.out.println("Got a coupon to buy: #"+couponID+", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("customer")) {
				Coupon purchasedCoupon = customerService.PurchaseCoupon(couponID);
				return new ResponseEntity<Integer>(purchasedCoupon.getId(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons")
	public ResponseEntity<?> getCustomerCoupons(@RequestHeader("token") String token) {
		System.out.println("getting all the customer's coupons, token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("customer")) {
				List<Coupon> customerCoupons = customerService.getCustomerCoupons();
				return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons/Category")
	public ResponseEntity<?> getCustomerCoupons(@RequestBody Category category, @RequestHeader("token") String token) {
		System.out.println("getting all the customer's coupons with the category: " + category + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("customer")) {
				List<Coupon> customerCouponsByCategory = customerService.getCustomerCoupons(category);
				return new ResponseEntity<List<Coupon>>(customerCouponsByCategory, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons/MaxPrice")
	public ResponseEntity<?> getCustomerCoupons(@RequestBody double maxPrice, @RequestHeader("token") String token) {
		System.out.println("getting all the customer's coupons with the max price: " + maxPrice + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("customer")) {
				List<Coupon> customerCouponsByMaxPrice = customerService.getCustomerCoupons(maxPrice);
				return new ResponseEntity<List<Coupon>>(customerCouponsByMaxPrice, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons/MinPrice")
	public ResponseEntity<?> getCustomerCouponsByMinPrice(@RequestBody double minPrice, @RequestHeader("token") String token) {
		System.out.println("getting all the customer's coupons with the min price: " + minPrice + ", token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("customer")) {
				List<Coupon> customerCouponsByMinPrice = customerService.getCustomerCouponsByMinPrice(minPrice);
				return new ResponseEntity<List<Coupon>>(customerCouponsByMinPrice, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerDetails")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader("token") String token) {
		System.out.println("getting the customer's details, token="+token);
		if (tokenManager.isTokenExist(token)) {
			if(tokenManager.getClientTypeByKey(token).equals("customer")) {
				Customer customerCouponsByCategory = customerService.getCustomerDetails();
				return new ResponseEntity<Customer>(customerCouponsByCategory, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("No Auth!", HttpStatus.BAD_REQUEST);
			}	
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
}