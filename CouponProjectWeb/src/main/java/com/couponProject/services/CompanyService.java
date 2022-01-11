package com.couponProject.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.couponProject.entities.Category;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.exceptions.CouponAlreadyExistsException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.DataNotValidException;

@Scope("prototype")
@Service
@Transactional
public class CompanyService extends ClientService {
	
	private int companyID;
	
	public CompanyService(int companyID) {
		this.companyID = companyID;
	}
	
	public CompanyService() {}
	
	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	
	@Override
	public boolean login(String email, String password) {
		Company company = companyRepository.findByEmailAndPassword(email, password);
		if(company == null) {
			return false;
		}
		setCompanyID(company.getId());
		return true;
	}
	
	
	public Coupon addCoupon(Coupon coupon) throws CouponAlreadyExistsException, CouponNotFoundException, DataNotValidException {
		List<Coupon> check = couponRepository.findByTitle(coupon.getTitle());
		if(!check.isEmpty()) {
			throw new CouponAlreadyExistsException("There is already a coupon with the same title!");
		}
		if(!isCouponValid(coupon.getStartDate(), coupon.getEndDate(), coupon.getPrice(), coupon.getAmount())) {
			throw new DataNotValidException("One of the parameters isn't valid, Try again!");
		}
		return couponRepository.save(coupon);
	}
	
	public Coupon updateCoupon(Coupon coupon) throws CouponNotFoundException, CouponAlreadyExistsException, DataNotValidException {
		List<Coupon> check = couponRepository.findByIdAndCompanyId(coupon.getId(), coupon.getCompany());
		if(check.isEmpty()) {
			throw new CouponNotFoundException("The coupon that you tried to update doesn't exist.");
		}
		if(!isCouponValid(coupon.getStartDate(), coupon.getEndDate(), coupon.getPrice(), coupon.getAmount())) {
			throw new DataNotValidException("One of the parameters isn't valid, Try again!");
		}
		List<Coupon> checkTitle = couponRepository.findByTitle(coupon.getTitle());
		if(!checkTitle.isEmpty()) {
			throw new CouponAlreadyExistsException("There is already a coupon with the same title!");
		}
		return couponRepository.save(coupon);
	}
	
	public boolean deleteCoupon(int couponID) throws CouponNotFoundException {
		Coupon coupon = getOneCoupon(couponID);
		if(coupon == null) {
			return false;
		}
		couponRepository.delete(coupon);
		return true;
	}
	
	public Coupon getOneCoupon(int couponID) throws CouponNotFoundException {
		List<Coupon> check = couponRepository.findByIdAndCompanyId(couponID, companyID);
		if (check.isEmpty()) {
			throw new CouponNotFoundException("The coupon that you tried to get doesn't exist in this company.");
		}
		return couponRepository.getById(couponID);
	}
	
	public List<Coupon> getCompanyCoupons() {
		return couponRepository.findByCompanyId(companyID);
	}

	public List<Coupon> getCompanyCoupons(Category category) {
		return couponRepository.findByCompanyIdAndCategory(companyID, category);
	}

	public List<Coupon> getCompanyCoupons(double maxPrice) {
		return couponRepository.findByCompanyIdAndPriceLessThanEqual(companyID, maxPrice);
	}

	public List<Coupon> getCompanyCouponsByMinPrice(double minPrice) {
		return couponRepository.findByCompanyIdAndPriceGreaterThanEqual(companyID, minPrice);
	}
	
	public Company getCompanyDetails() {
		return companyRepository.getById(companyID);
	}
}