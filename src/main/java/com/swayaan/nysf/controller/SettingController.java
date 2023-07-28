package com.swayaan.nysf.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.utils.CommonUtil;


@Controller
@RequestMapping("/admin")
public class SettingController {

	@Autowired
	private SettingService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);
	
	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-settings")
	public String listAll(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listAll SettingController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<Setting> listSettings = service.listAllSettings();
			
		
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		model.addAttribute("pageTitle", "Manage Site Settings");
		LOGGER.info("Exit listAll SettingController");

		return "settings/setting";
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		LOGGER.info("Entered updateSettingValuesFromForm method -SettingController");
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		
		service.saveAll(listSettings);
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-settings/save_mail_server")
	public String saveMailServerSetttings(HttpServletRequest request, RedirectAttributes ra) {
		LOGGER.info("Entered saveMailServerSetttings method -SettingController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<Setting> mailServerSettings = service.getMailServerSettings();
		updateSettingValuesFromForm(request, mailServerSettings);
		
		ra.addFlashAttribute("message", "Mail server settings have been saved");
		LOGGER.info("Exit saveMailServerSetttings method -SettingController");

		return "redirect:/admin/manage-settings";
	}
	
	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-settings/save_mail_templates")
	public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes ra) {
		LOGGER.info("Entered saveMailTemplateSetttings method -SettingController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
		updateSettingValuesFromForm(request, mailTemplateSettings);
		
		ra.addFlashAttribute("message", "Mail template settings have been saved");
		LOGGER.info("Exit saveMailTemplateSetttings method -SettingController");

		return "redirect:/admin/manage-settings";
	}
}
