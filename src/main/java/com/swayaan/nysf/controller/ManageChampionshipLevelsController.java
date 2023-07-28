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

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.ChampionshipLevelsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageChampionshipLevelsController {

	@Autowired
	ChampionshipLevelsService service;

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageChampionshipLevelsController.class);

	private static final Integer ROLE_ADMINISTRATOR = 1;

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-championship-levels")
	public String listFirstPageChampionshipLevels(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageChampionshipLevels method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			LOGGER.info("Exit listFirstPageChampionshipLevels method -ManageChampionshipLevelsController");

			return listAllChampionshipLevelsByPage(1, model, "title", "asc", "", request);

		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-championship-levels/page/{pageNum}")
	public String listAllChampionshipLevelsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String title, HttpServletRequest request) {
		LOGGER.info("Entered listAllChampionshipLevelsByPage method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			Page<ChampionshipLevels> page = service.listByPage(pageNum, sortField, sortDir, title);
			List<ChampionshipLevels> listChampionshipLevels = page.getContent();

			long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
			long endCount = startCount + service.RECORDS_PER_PAGE - 1;

			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
			model.addAttribute("moduleURL", "/admin/manage-championship-levels");
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", reverseSortDir);
			model.addAttribute("keyword1", title);
			// model.addAttribute("keyword2", jrnNumber);
			// Role role = new Role();
			ChampionshipLevels championshipLevels = new ChampionshipLevels();

			model.addAttribute("listChampionshipLevels", listChampionshipLevels);
			model.addAttribute("pageTitle", "Manage Championship Levels");
			model.addAttribute("championshipLevels", championshipLevels);
			LOGGER.info("Exit listAllChampionshipLevelsByPage method -ManageChampionshipLevelsController");

			return "administration/manage_championship_levels";
		}
	}

	/*
	 * public String listAllChampionshipLevels(Model model, HttpServletRequest
	 * request) {
	 * 
	 * LOGGER.
	 * info("Entered listAllChampionshipLevels ManageChampionshipLevelsController");
	 * // For Navigation history String mappedUrl = request.getRequestURI(); User
	 * currentUser = CommonUtil.getCurrentUser();
	 * CommonUtil.setNavigationHistory(mappedUrl, currentUser); if
	 * (currentUser.getRoleId() != ROLE_ADMINISTRATOR) { return "error/403"; } else
	 * {
	 * 
	 * List<ChampionshipLevels> listChampionshipLevels =
	 * service.listAllChampionshipLevels();
	 * model.addAttribute("listChampionshipLevels", listChampionshipLevels);
	 * model.addAttribute("pageTitle", "Manage Championship Levels"); return
	 * "administration/manage_championship_levels"; } }
	 */

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-championship-levels/new")
	public String newChampionshipLevels(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newChampionshipLevels method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			ChampionshipLevels championshipLevels = new ChampionshipLevels();
			championshipLevels.setEnabled(true);
			model.addAttribute("championshipLevels", championshipLevels);
			model.addAttribute("pageTitle", "Add Championship level");
			LOGGER.info("Exit newChampionshipLevels method -ManageChampionshipLevelsController");

			return "administration/championship_level_form";

		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-championship-levels/save")
	public String saveChampionshipLevels(ChampionshipLevels championshipLevels, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException {
		LOGGER.info("Entered saveChampionshipLevels method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			ChampionshipLevels checkChampionshipLevels = service
					.getChampionshipLevelsByTitle(championshipLevels.getTitle());
			String message = "";
			String errorMessage = "";

			if (championshipLevels.getId() == null) { // if saving new record
				if (checkChampionshipLevels == null) {
					message = "The Championship levels has been saved successfully!";
					service.save(championshipLevels);
					redirectAttributes.addFlashAttribute("message", message);
				} else {
					errorMessage = "The Championship levels already exists!";
					redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
				}
			} else {
				try {
					ChampionshipLevels existingChampionshipLevels = service.get(championshipLevels.getId());
					if (checkChampionshipLevels != null) {
						if (checkChampionshipLevels.getTitle().equals(championshipLevels.getTitle())
								&& checkChampionshipLevels.getId().equals(championshipLevels.getId())) {
							existingChampionshipLevels.setEnabled(championshipLevels.isEnabled());
							service.save(existingChampionshipLevels);
							message = "The Championship levels has been updated successfully!";
							redirectAttributes.addFlashAttribute("message", message);

						} else if (!checkChampionshipLevels.getTitle().equals(championshipLevels.getTitle())) {
							existingChampionshipLevels.setTitle(championshipLevels.getTitle());
							existingChampionshipLevels.setEnabled(championshipLevels.isEnabled());
							message = "The Championship levels has been updated successfully!";
							service.save(existingChampionshipLevels);
							redirectAttributes.addFlashAttribute("message", message);

						} else {
							errorMessage = "The Championship levels already exists!";
							redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
						}
					} else {
						existingChampionshipLevels.setTitle(championshipLevels.getTitle());
						existingChampionshipLevels.setEnabled(championshipLevels.isEnabled());
						message = "The Championship levels has been updated successfully!";
						service.save(existingChampionshipLevels);
						redirectAttributes.addFlashAttribute("message", message);
					}

				} catch (EntityNotFoundException e) {
					LOGGER.error("No Championship levels found for the id " + championshipLevels.getId());
				}
			}
			LOGGER.info("Exit saveChampionshipLevels method -ManageChampionshipLevelsController");

			return "redirect:/admin/manage-championship-levels";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-championship-levels/edit/{id}")
	public String editChampionshipLevels(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered editChampionshipLevels method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				ChampionshipLevels championshipLevels = service.get(id);
				model.addAttribute("championshipLevels", championshipLevels);
				model.addAttribute("pageTitle", "Edit Championship Level");
				return "administration/championship_level_form";
			} catch (EntityNotFoundException ex) {
				LOGGER.error("Championship level not found" + ex.getMessage());
				redirectAttributes.addFlashAttribute("errorMessage", "Championship level with id" + id + "not found");
				LOGGER.info("Exit editChampionshipLevels method -ManageChampionshipLevelsController");
				return "redirect:/admin/manage-championship-levels";
			}
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-championship-levels/delete/{id}")
	public String deleteChampionshipLevels(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered deleteChampionshipLevels method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			Boolean flag = false;
			try {
				ChampionshipLevels championshipLevels = service.get(id);

				List<Championship> listChampionship = championshipService.getByChampionshipLevel(championshipLevels);

				if (!listChampionship.isEmpty()) {
					redirectAttributes.addFlashAttribute("errorMessage", "The championship Level "
							+ championshipLevels.getTitle()
							+ " cannot be deleted because championship level is already used in the championship");
					return "redirect:/admin/manage-championship-levels";
				}

				/*
				 * List<User> users = userService.listAll(); for (User u : users) { if
				 * (u.hasRole(role.getName())) { flag = true; break; } }
				 */

				// if (flag == true) {
				// redirectAttributes.addFlashAttribute("message1",
				// "The role " + role.getName() + " cannot be deleted. It has been assigned to
				// the user");
				// } else {

				service.delete(id);

				redirectAttributes.addFlashAttribute("message",
						"The Championship Levels " + championshipLevels.getTitle() + " has been deleted successfully");
				// }

			} catch (EntityNotFoundException ex) {
				LOGGER.error("Championship Levels with id" + id + "not found" + ex.getMessage());
				redirectAttributes.addFlashAttribute("errorMessage", "Championship Levels with id" + id + "not found");
			}
			LOGGER.info("Exit deleteChampionshipLevels method -ManageChampionshipLevelsController");

			return "redirect:/admin/manage-championship-levels";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-championship-levels/{id}/enabled/{status}")
	public String updateChampionshipLevelsEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EntityNotFoundException {
		LOGGER.info("Entered updateChampionshipLevelsEnabledStatus method -ManageChampionshipLevelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			ChampionshipLevels championshipLevels = service.get(id);

			service.updateChampionshipLevelsEnabledStatus(id, enabled);
			String status = enabled ? "enabled" : "disabled";
			String message = "The Championship Levels " + championshipLevels.getTitle() + " has been " + status;
			redirectAttributes.addFlashAttribute("message", message);
			LOGGER.info("Exit updateChampionshipLevelsEnabledStatus method -ManageChampionshipLevelsController");
			return "redirect:/admin/manage-championship-levels";
		}
	}
}
