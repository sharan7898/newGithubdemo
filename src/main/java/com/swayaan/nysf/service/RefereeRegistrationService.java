//package com.swayaan.nysf.service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.swayaan.nysf.entity.RefereeRegistration;
//import com.swayaan.nysf.entity.User;
//import com.swayaan.nysf.exception.RefereeRegistrationNotFoundException;
//import com.swayaan.nysf.repository.RefereeRegistrationRepository;
//import com.swayaan.nysf.repository.UserRepository;
//import com.swayaan.nysf.utils.CommonUtil;
//
//@Service
//@Transactional
//public class RefereeRegistrationService {
//	
//	@Autowired
//	private RefereeRegistrationRepository repo;
//	
//	@Autowired
//	private UserRepository userRepo;
//	
//	@Autowired RecaptchaService captchaService;
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	 @Value("${site.admin-mail}")
//	 private String siteAdminMail;
//	
//	public RefereeRegistration getByEmail(String email) {
//		return repo.getRefereeRegistrationByEmail(email);
//	}
//	
//	public List<RefereeRegistration> listAllEnabledReferees() {
//		return (List<RefereeRegistration>) repo.findByEnabledTrue();
//	}
//	
//	public boolean checkCaptcha(String recaptchaResponse,HttpServletRequest request) {
//		
//		String ip = request.getRemoteAddr();
//		String captchaVerifyMessage = 
//			captchaService.verifyRecaptcha(ip, recaptchaResponse);
//		
//		if ( StringUtils.isNotEmpty(captchaVerifyMessage)) {
//			return 	false;
//		}
//		return true;
//	}
//	
//	public RefereeRegistration save(RefereeRegistration refereeRegistration) {
//		User currentUser = CommonUtil.getCurrentUser();	
//		boolean isUpdatingRefereeRegistration = (refereeRegistration.getId() != null);
//		if (isUpdatingRefereeRegistration) {
//			RefereeRegistration existingRefereeRegistration = repo.findById(refereeRegistration.getId()).get();
//			if (refereeRegistration.getPassword().isEmpty()) {
//				refereeRegistration.setPassword(existingRefereeRegistration.getPassword());
//			} else {
//				encodePassword(existingRefereeRegistration);
//			}
//			
//			refereeRegistration.setLastModifiedBy(currentUser);
//			refereeRegistration.setLastModifiedTime(new Date());
//			refereeRegistration.setCreatedTime(existingRefereeRegistration.getCreatedTime());
//			
//		} else {		
//			encodePassword(refereeRegistration);
//			refereeRegistration.setCreatedTime(new Date());
//		}
//		return repo.save(refereeRegistration);
//	}
//	
//	
//	private void encodePassword(RefereeRegistration refereeRegistration) {
//		String encodedPassword = passwordEncoder.encode(refereeRegistration.getPassword());		
//		refereeRegistration.setPassword(encodedPassword);
//	}
//	
//	public boolean isEmailUnique(Integer id, String email) {
//		RefereeRegistration refereeRegistrationByEmail = repo.getRefereeRegistrationByEmail(email);
//		User userByEmail = userRepo.getUserByEmail(email);
//		if (refereeRegistrationByEmail == null && userByEmail == null) {
//			return true;
//		}
//		boolean isCreatingNew = (id == null);
//		if (isCreatingNew) {
//			if (refereeRegistrationByEmail != null && userByEmail != null) {
//				return false;
//				}
//		} else {
//			if (refereeRegistrationByEmail.getId() != id) {
//				return false;
//			} else {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public RefereeRegistration get(Integer id) throws RefereeRegistrationNotFoundException {
//		try {
//			return repo.findById(id).get();
//		} catch (NoSuchElementException ex) {
//			throw new RefereeRegistrationNotFoundException("Could not find any Referee with ID " + id);
//		}
//	}
//	
//	public void delete(Integer id) throws RefereeRegistrationNotFoundException {
//		Long countById = repo.countById(id);
//		if (countById == null || countById == 0) {
//			throw new RefereeRegistrationNotFoundException("Could not find any Referee with ID " + id);
//		}
//		repo.deleteById(id);
//	}
//	
//	public void updateRefereeRegistrationEnabledStatus(Integer id, boolean enabled) {
//		repo.updateEnabledStatus(id, enabled);
//	}
//	
//	public void updateRefereeRegistrationApprovedStatus(Integer id, boolean approvalStatus) {
//		repo.updateApprovalStatus(id, approvalStatus);
//	}
//
//	public void addRefereeRegistration(RefereeRegistration refereeRegistration) {
//		//encodePassword(user);
//		refereeRegistration.setEnabled(true);
//		refereeRegistration.setApprovalStatus(false);
//		refereeRegistration.setCreatedTime(new Date());
//		
//		repo.save(refereeRegistration);
//	}
//
//	
//
//}
