package com.swayaan.nysf.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.utils.CommonUtil;


@Controller
@RequestMapping("/judge")
public class JudgeDashboardController {
	
	
	
	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/dashboard")
	public String displayDashboard(Model model,HttpServletRequest request) {
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		model.addAttribute("pageTitle", "Dashboard");

		return "judge/judge_dashboard";
	}
	

}
