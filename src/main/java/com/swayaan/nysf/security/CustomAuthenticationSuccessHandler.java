package com.swayaan.nysf.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.swayaan.nysf.controller.UserAuditLogController;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.UserService;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private UserService userService;
	
	@Autowired UserAuditLogController userAuditLogController;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(request,authentication);

		if (response.isCommitted()) {
			// logger.debug(
			// "Response has already been committed. Unable to redirect to "
			// + targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, final Authentication authentication) {

		Map<String, String> roleTargetUrlMap = new HashMap<>();
		NysfUserDetails user = (NysfUserDetails) authentication.getPrincipal();
		User loginUser = userService.getByEmail(user.getUsername());
		userAuditLogController.saveUserLoginTime(request,loginUser);
		
//		User currentUser = userService.getByEmail(user.getUsername());
//		
//		RefereesPanels refereePanels = refereesPanelsService.getByUser(currentUser);
//		if(refereePanels!=null) {
//			System.out.println("aaaa"+refereePanels.getType().getType());
//		if (refereePanels.getType().getId() == 2) {
//			roleTargetUrlMap.put("D Judge", "/judge/team-scoring");
//		} else if (refereePanels.getType().getId() == 5) {
//			roleTargetUrlMap.put("Evaluator", "/judge/team-scoring");
//		}  else if (refereePanels.getType().getId() == 6) {
//			roleTargetUrlMap.put("Scorer", "/referee/view-scores");
//		}else if (refereePanels.getType().getType().equals("Timekeeper Judge")) {
//			System.out.println("bbbb"+refereePanels.getType().getType());
//			roleTargetUrlMap.put("TimeKeeper", "/referee/manage-championship");
//		} else if (refereePanels.getType().getId() == 7) {
//			roleTargetUrlMap.put("Stage Manager", "/referee/manage-championship");
//		} else if (refereePanels.getType().getId() == 8) {
//			roleTargetUrlMap.put("Co-ordinator", "/referee/manage-championship");
//		} else if (refereePanels.getType().getId() == 1) {
//			roleTargetUrlMap.put("Chief Judge", "/referee/view-scores");
//		}else if (refereePanels.getType().getId() == 3) {
//			roleTargetUrlMap.put("Artistic Judge", "/judge/team-scoring");
//		}
//		}
		
		if(user.hasRole("Judge")) {
			roleTargetUrlMap.put("Judge", "/judge/common-team-scoring");
			
		}
		if(user.hasRole("Participant")) {
			roleTargetUrlMap.put("Participant", "/participant/dashboard");
			
		}
		if(user.hasRole("EventManager")) {
			roleTargetUrlMap.put("EventManager", "/eventmanager/dashboard");
			
		}
		if(user.hasRole("Administrator")) {
			roleTargetUrlMap.put("Administrator", "/dashboard");
			
		}
		if(user.hasRole("SubAdministrator")) {
			roleTargetUrlMap.put("SubAdministrator", "/");
		}
	

		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (final GrantedAuthority grantedAuthority : authorities) {
			String authorityName = grantedAuthority.getAuthority();
			if (roleTargetUrlMap.containsKey(authorityName)) {
				return roleTargetUrlMap.get(authorityName);
			}
		}

		throw new IllegalStateException();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
