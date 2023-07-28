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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageRoleController {

	@Autowired
	RoleService service;

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageRoleController.class);

	private static final Integer ROLE_ADMINISTRATOR = 1;

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-roles")
	public String listFirstPageRoles(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageRoles method -ManageRoleController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			LOGGER.info("Exit listFirstPageRoles method -ManageRoleController");

		return listAllRolesByPage(1, model, "name", "asc", "", request);
		
	}
	}
	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-roles/page/{pageNum}")
	public String listAllRolesByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name, HttpServletRequest request) {
		LOGGER.info("Entered listAllRolesByPage method -ManageRoleController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();

		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {


		Page<Role> page=service.listByPage(pageNum, sortField, sortDir, name);
		List<Role> listRoles = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-roles");
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
		Role role = new Role();
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Manage Roles");
		model.addAttribute("role", role);
		LOGGER.info("Exit listAllRolesByPage method -ManageRoleController");

		return "administration/manage_roles";
	}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-roles/new")
	public String newRole(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newRole method -ManageRoleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			Role role = new Role();
			role.setEnabled(true);
			model.addAttribute("role", role);
			model.addAttribute("pageTitle", "Add Role");
			LOGGER.info("Exit newRole method -ManageRoleController");

			return "administration/role_form";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-roles/save")
	public String saveRole(Role role, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException {
		LOGGER.info("Entered saveRole method -ManageRoleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			Boolean roleNames = service.checkRoleName(role.getName());
			boolean isUpdatingRole = (role.getId() != null);
			if (!roleNames) {

				if (isUpdatingRole) {
					if (role.getId() == 1) {
						redirectAttributes.addFlashAttribute("message1", "The Administartor cannot be edited.");
					} else {
						service.save(role);
						redirectAttributes.addFlashAttribute("message",
								"The Role " + role.getName() + " has been updated successfully.");
					}
				} else {
					service.save(role);
					redirectAttributes.addFlashAttribute("message",
							"The Role " + role.getName() + " has been saved successfully.");
				}
			} else {
				if (!isUpdatingRole) {
					redirectAttributes.addFlashAttribute("message1",
							"The Role " + role.getName() + " is already present.");
				} else {
					service.save(role);
					redirectAttributes.addFlashAttribute("message",
							"The Role " + role.getName() + " has been updated successfully.");
				}

			}
			LOGGER.info("Exit saveRole method -ManageRoleController");

			return "redirect:/admin/manage-roles";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-roles/edit/{id}")
	public String editRole(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws RoleNotFoundException {
		LOGGER.info("Entered editRole method -ManageRoleController");
		try {
			// For Navigation history
			String mappedUrl = request.getRequestURI();
			User currentUser = CommonUtil.getCurrentUser();
			CommonUtil.setNavigationHistory(mappedUrl, currentUser);
			if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
				return "error/403";
			} else {

				Role role = service.get(id);
				model.addAttribute("role", role);
				model.addAttribute("pageTitle", "Edit Role");
				return "administration/role_form";
			}
		} catch (RoleNotFoundException ex) {
			LOGGER.error("Role with id" + id + "not found" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", "Role with id" + id + "not found");
			LOGGER.info("Exit editRole method -ManageRoleController");

			return "redirect:/admin/manage-roles";
		}

	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-roles/delete/{id}")
	public String deleteRole(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered deleteRole method -ManageRoleController");
		Boolean flag = false;
		try {
			// For Navigation history
			String mappedUrl = request.getRequestURI();
			User currentUser = CommonUtil.getCurrentUser();
			CommonUtil.setNavigationHistory(mappedUrl, currentUser);
			if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
				return "error/403";
			} else {

				Role role = service.get(id);
				if (role.getName().equals("Administrator")) {
					String message = "The Administrator role cannot be deleted!";
					redirectAttributes.addFlashAttribute("message", message);
				} else {
					List<User> users = userService.listAll();
					for (User u : users) {
						if (u.hasRole(role.getName())) {
							flag = true;
							break;
						}
					}
					if (flag == true) {
						redirectAttributes.addFlashAttribute("message1",
								"The role " + role.getName() + " cannot be deleted. It has been assigned to the user");
					} else {
						service.delete(id);
						redirectAttributes.addFlashAttribute("message",
								"The role " + role.getName() + " has been deleted successfully");
					}
				}
			}
		} catch (RoleNotFoundException ex) {
			LOGGER.error("Role with id" + id + "not found" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", "Role with id" + id + "not found");
		}
		LOGGER.info("Exit deleteRole method -ManageRoleController");

		return "redirect:/admin/manage-roles";
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-roles/{id}/enabled/{status}")
	public String updateRoleEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws RoleNotFoundException {
		LOGGER.info("Entered updateRoleEnabledStatus method -ManageRoleController");
		Role role = service.get(id);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			if (role.getName().equals("Administrator")) {
				String message = "The Administrator role cannot be disabled!";
				redirectAttributes.addFlashAttribute("message", message);
			} else {
				service.updateRoleEnabledStatus(id, enabled);
				String status = enabled ? "enabled" : "disabled";
				String message = "The Role " + role.getName() + " has been " + status;
				redirectAttributes.addFlashAttribute("message", message);
			}
			LOGGER.info("Exit updateRoleEnabledStatus method -ManageRoleController");

			return "redirect:/admin/manage-roles";
		}
	}
}
