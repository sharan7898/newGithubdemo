package com.swayaan.nysf.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.repository.NavigationHistoryRepository;
import com.swayaan.nysf.service.NavigationHistoryService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class NavigationHistoryController {
	
	@Autowired NavigationHistoryService service;


	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationHistoryController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/navigation-history")
	public String listFirstNavigation(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstNavigation method -ManageNavigationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		LOGGER.info("Exit listFirstNavigation method -ManageNavigationController");

		return listAllNavigationHistoryByPage(1, model, "userName", "asc", "", "","","", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/navigation-history/page/{pageNum}")
	public String listAllNavigationHistoryByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String userName,
			@RequestParam(name = "keyword2", required = false) String roleName,
			@RequestParam(name = "keyword3", required = false) String url,
			@RequestParam(name = "keyword4", required = false) String createdTime,HttpServletRequest request) {
		LOGGER.info("Entered listAllNavigationHistoryByPage method -ManageNavigationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		Page<NavigationHistory> page=service.listByPage(pageNum, sortField, sortDir, userName, roleName, url, createdTime);
		List<NavigationHistory> listNavigationHistory = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/navigation-history");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", userName);
		model.addAttribute("keyword2", roleName);
		model.addAttribute("keyword3", url);
		model.addAttribute("keyword4", createdTime);

	//	UserAuditLog userAuditLog = new UserAuditLog();

		model.addAttribute("listNavigationHistory", listNavigationHistory);
		model.addAttribute("pageTitle", "Navigation History");
	//	model.addAttribute("userAuditLog", userAuditLog);
		LOGGER.info("Exit listAllNavigationHistoryByPage method -ManageNavigationController");

		return "administration/navigation_history";
	}
	
/*	public String displayDashboard(Model model,HttpServletRequest request) {
		
		List<NavigationHistory> listNavigationHistory = service.listAllNavigationHistory();
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		
		model.addAttribute("listNavigationHistory", listNavigationHistory);
		model.addAttribute("pageTitle", "Navigation History");
		
		return "administration/navigation_history";
	} */
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/navigation-history/delete/{days}")
	public String deleteNavigationHistory(Model model, RedirectAttributes redirectAttributes,
			@PathVariable(name = "days") Integer days,
			 HttpServletRequest request) {
			
		LOGGER.info("Entered deleteNavigationHistory method -ManageNavigationController");
		System.out.println("days"+days);
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		
			List<NavigationHistory> listNavigationHistory = service.getNavigationHistoryInDays(days);
			
			if (listNavigationHistory.isEmpty()) {
				redirectAttributes.addFlashAttribute("message1",
						"No Navigation History found before "+days+ " days");
				return "redirect:/admin/navigation-history";
			}

			for (NavigationHistory navigationHistory : listNavigationHistory) {
				service.delete(navigationHistory.getId());
			}

			redirectAttributes.addFlashAttribute("message",
					"The Navigation History before "+ days +"days has been deleted successfully");
			LOGGER.info("Exit deleteNavigationHistory method -ManageNavigationController");

			return "redirect:/admin/navigation-history";
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/navigation-history/export")
	public String exportAuditReportToCsv(Model model, RedirectAttributes redirectAttributes,
			HttpServletResponse response,HttpServletRequest request) throws IOException, ParseException {
		LOGGER.info("Entered exportAuditReportToCsv method -ManageNavigationController");

		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		
			String user = "";
			List<NavigationHistory> listNavigationHistory = service.listByUser(user);
			if(listNavigationHistory.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "No Logs found!");
				return "redirect:/admin/navigation-history";
			}
			
			NavigationHistoryCsvExporter navigationHistoryCsvExporter = new NavigationHistoryCsvExporter();
			navigationHistoryCsvExporter.export(listNavigationHistory, response);
			LOGGER.info("Exit exportAuditReportToCsv method -ManageNavigationController");

			return "redirect:/admin/navigation-history";
	}


	
	

}
