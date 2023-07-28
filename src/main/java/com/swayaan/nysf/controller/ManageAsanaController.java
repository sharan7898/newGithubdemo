package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
@RequestMapping("/admin")
public class ManageAsanaController {

	@Autowired
	AsanaService service;

	@Autowired
	AsanaCategoryService asanaCategoryService;

	@Autowired
	RoleService roleservice;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageAsanaController.class);

	private static final Integer ROLE_ADMINISTRATOR = 1;

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana")
	public String listFirstPageJudges(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageJudges method -ManageAsanasController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			LOGGER.info("Exit listFirstPageJudges method -ManageAsanasController");

		return listAllAsanasByPage(1, model, "name", "asc", "", request);
		
	}
	}
	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana/page/{pageNum}")
	public String listAllAsanasByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name, HttpServletRequest request) {
		LOGGER.info("Entered listAllAsanasByPage method -ManageAsanasController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

		Page<Asana> page=service.listByPage(pageNum, sortField, sortDir, name);
		List<Asana> listAsanas = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-asana");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
	//	model.addAttribute("keyword2", jrnNumber);
		Asana asana = new Asana();
		model.addAttribute("listAsanas", listAsanas);
		model.addAttribute("pageTitle", "Manage Asanas");
		model.addAttribute("asana", asana);
		LOGGER.info("Exit listAllAsanasByPage method -ManageAsanasController");

		return "administration/manage_asana";
	}
	}
	
/*	public String listAllAsanas(Model model, HttpServletRequest request, Role role) {
		LOGGER.info("Entered list all asanas ManageAsanaController");
		// For Navigation history

		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			List<Asana> listAsanas = service.listAllAsanas();
			model.addAttribute("listAsanas", listAsanas);
			model.addAttribute("pageTitle", "Manage Asanas");
			return "administration/manage_asana";
		}
	}*/

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana/new")
	public String newAsana(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newAsana method -ManageAsanasController");
		// For Navigation history

		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			Asana asana = new Asana();
			model.addAttribute("asana", asana);
			model.addAttribute("pageTitle", "Add Asana");
			LOGGER.info("Exit newAsana method -ManageAsanasController");

			return "administration/asana_form";
		}

	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-asana/save")
	public String saveAsana(Asana asana, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
		LOGGER.info("Entered saveAsana method -ManageAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				asana.setImage(fileName);

				Asana savedAsana = service.save(asana);
				String uploadDir = "asana-images/" + savedAsana.getId();

				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} else {
				if (asana.getImage().isEmpty()) {
					asana.setImage(null);
				}
				service.save(asana);
			}

			// service.save(asana);
			redirectAttributes.addFlashAttribute("message", "The Asana has been saved successfully.");
			LOGGER.info("Exit saveAsana method -ManageAsanasController");

			return "redirect:/admin/manage-asana";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana/edit/{id}")
	public String editAsana(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws AsanaNotFoundException {
		LOGGER.info("Entered editAsana method -ManageAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				Asana asana = service.get(id);
				// List<AsanaCategory> listCategories =
				// asanaCategoryService.listAllAsanaCategories();

				model.addAttribute("asana", asana);
				model.addAttribute("pageTitle", "Edit Asana");
				// model.addAttribute("listCategories", listCategories);
				LOGGER.info("Exit editAsana method -ManageAsanasController");
                 return "administration/asana_form";
			} catch (AsanaNotFoundException ex) {
				redirectAttributes.addFlashAttribute("message", "Asana not found.");
				LOGGER.error("Asana not found!" + ex.getMessage());
				return "redirect:/admin/manage-asana";
			}
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana/delete/{id}")
	public String deleteAsana(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered deleteAsana method -ManageAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				Asana asana = service.get(id);

//			List<ParticipantGroupAsanas> participantGroupAsanas = participantGroupAsanasService.getAsanasAssignedForGroup(asana);
//			
//			
//			if(!participantGroupAsanas.isEmpty()) {
//				System.out.println("participantGroupAsanas");
//				redirectAttributes.addFlashAttribute("message1", 
//						"The Asana " + asana.getName() + " is allocated to participant group asanas, Please remove before you delete");
//				return "redirect:/admin/manage-asana";
//			}

				service.delete(id);
				redirectAttributes.addFlashAttribute("message",
						"The Asana " + asana.getName() + " has been deleted successfully");

			} catch (AsanaNotFoundException ex) {
				LOGGER.error("Asana not found!" + ex.getMessage());
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
			LOGGER.info("Exit deleteAsana method -ManageAsanasController");
          return "redirect:/admin/manage-asana";
		}
	}

}
