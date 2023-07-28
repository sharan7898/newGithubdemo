package com.swayaan.nysf.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.controller.UserAuditLogController;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.UserService;


@Service
public class CustomLogoutHandler implements LogoutHandler {
	
	@Autowired UserService userService;
	@Autowired UserAuditLogController userAuditLogController;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
					   Authentication authentication) {
		
		HttpSession httpSession = request.getSession(false);
		String sessionId = httpSession.getId();
		System.out.println("Session ID from logout handler : " + sessionId);
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
	        	
	        	NysfUserDetails user = (NysfUserDetails) authentication.getPrincipal();
				User loggedOutUser = userService.getByEmail(user.getUsername());
				userAuditLogController.saveUserLogoutTime(request,loggedOutUser);
				
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	      
			response.sendRedirect(request.getContextPath() + "/login?logout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}