package com.swayaan.nysf.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.service.UserAuditLogService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;


@Controller
@RequestMapping("/admin")
public class UserAuditLogController {
	
	@Autowired private UserAuditLogService service;
	@Autowired private UserService userService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuditLogController.class);
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/audit-report")
	public String listFirstPageAudit(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listAllAudit method -UserAuditLogController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		LOGGER.info("Exit listAllAudit method -UserAuditLogController");

		return listAllAuditByPage(1, model, "loggedUser", "asc", "", "", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/audit-report/page/{pageNum}")
	public String listAllAuditByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String loggedUser,
			@RequestParam(name = "keyword2", required = false) String ipAddress,HttpServletRequest request) {
		LOGGER.info("Entered listAllAuditByPage method -UserAuditLogController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		Page<UserAuditLog> page=service.listByPage(pageNum, sortField, sortDir, loggedUser, ipAddress);
		List<UserAuditLog> listUserAuditLog = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/audit-report");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", loggedUser);
		model.addAttribute("keyword2", ipAddress);
		UserAuditLog userAuditLog = new UserAuditLog();

		model.addAttribute("listUserAuditLog", listUserAuditLog);
		model.addAttribute("pageTitle", "User Audit Log");
		model.addAttribute("userAuditLog", userAuditLog);
		LOGGER.info("Exit listAllAuditByPage method -UserAuditLogController");

		return "administration/user_audit_report";
	}
	
	
/*	public String listAuditPage(Model model, HttpServletRequest request) {
		List<UserAuditLog> listUserAuditLog = service.listAll();
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		
		model.addAttribute("listUserAuditLog", listUserAuditLog);
		model.addAttribute("pageTitle", "User Audit Log");
		
		return "administration/user_audit_report";
	} */
	
	public void saveUserLoginTime(HttpServletRequest httpRequest, User user) {
		LOGGER.info("Entered saveUserLoginTime in UserAuditLogController");

		HttpSession httpSession = httpRequest.getSession();
		String sessionId = httpSession.getId();
		LOGGER.info("Session ID from audit controller : " + sessionId);
		LOGGER.debug("Session ID : " + sessionId);
		
		// get Client IP Address
	//	String ipAddress = httpRequest.getLocalAddr();
		String ipAddress = httpRequest.getRemoteAddr();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
	            "yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	    //converting to IST
	    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
	    String istDateTime = simpleDateFormat.format(date);
		UserAuditLog userAuditLog = new UserAuditLog();
		userAuditLog.setLoggedUser(user);
		userAuditLog.setLoginTime(istDateTime);
		userAuditLog.setSessionId(sessionId);
		userAuditLog.setIpAddress(ipAddress);
		
		service.saveLoginTime(userAuditLog);
	}
	
	public void saveUserLogoutTime(HttpServletRequest httpRequest, User user) {
		LOGGER.info("Entered saveUserLogoutTime in UserAuditLogController");

		HttpSession httpSession = httpRequest.getSession(false);
		String sessionId = httpSession.getId();
		System.out.println("Session ID from httprequest : " + sessionId);
		LOGGER.debug("Session ID from httprequest : " + sessionId);
		
		
		UserAuditLog userAuditLog = service.getByUserAndSessionId(user,sessionId);
		LOGGER.debug("Session ID from database: " + userAuditLog.getSessionId());
		if(sessionId.equals(userAuditLog.getSessionId())) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
		            "yyyy-MM-dd HH:mm:ss");
		    Date date = new Date();
		    //converting to IST
		    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		    String istDateTime = simpleDateFormat.format(date);
		    //Date convertedISTDateTime = simpleDateFormat.parse(istDateTime);
			userAuditLog.setLogoutTime(istDateTime);
			service.saveLogoutTime(userAuditLog);
		}
		
	}
	
	public void saveUserLogoutTimeForSession(String sessionId) {
		LOGGER.info("Entered saveUserLogoutTimeForSession in UserAuditLogController");

		LOGGER.debug("Session ID from Destroyed Event : " + sessionId);
		
		UserAuditLog userAuditLog = service.getBySessionId(sessionId);
		if(userAuditLog != null) {
			LOGGER.debug("Session ID from database: " + userAuditLog.getSessionId());
			if(sessionId.equals(userAuditLog.getSessionId())) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			            "yyyy-MM-dd HH:mm:ss");
			    Date date = new Date();
			    //converting to IST
			    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
			    String istDateTime = simpleDateFormat.format(date);
			    //Date convertedISTDateTime = simpleDateFormat.parse(istDateTime);
				userAuditLog.setLogoutTime(istDateTime);
				service.saveLogoutTime(userAuditLog);
			}
		}
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/audit-report/export")
	public String exportAuditReportToCsv(Model model, RedirectAttributes redirectAttributes,
			HttpServletResponse response,HttpServletRequest request) throws IOException, ParseException {
		LOGGER.info("Entered exportAuditReportToCsv method -UserAuditLogController");

		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		
			String user = "";
			List<UserAuditLog> listAuditReport = service.listByUser(user);
			if(listAuditReport.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "No Logs found!");
				return "redirect:/admin/audit-report";
			}
			
			UserAuditLogCsvExporter userAuditLogCsvExporter = new UserAuditLogCsvExporter();
			userAuditLogCsvExporter.export(listAuditReport, response);
			LOGGER.info("Exit exportAuditReportToCsv method -UserAuditLogController");

			return "redirect:/admin/audit-report";
	}

	
	
	
	

	

}
