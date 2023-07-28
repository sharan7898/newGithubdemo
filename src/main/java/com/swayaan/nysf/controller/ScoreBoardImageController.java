package com.swayaan.nysf.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.repository.ChampionshipRepository;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
@RequestMapping("/admin")
public class ScoreBoardImageController {

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ChampionshipRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreBoardImageController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-score-board-image")
	public String listScoreBoardImage(Model model, HttpServletRequest request, RedirectAttributes re)
			throws IOException {
		LOGGER.info("Entered listScoreBoardImage method - ScoreBoardImageController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listScoreBoardImage method - ScoreBoardImageController");

		return listAllScoreBoardImagesByPage(1, model, "name", "asc", "", request);
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-score-board-image/page/{pageNum}")
	public String listAllScoreBoardImagesByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name, HttpServletRequest request) {
		LOGGER.info("Entered listAllScoreBoardImagesByPage method -ScoreBoardImageController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Page<Championship> page = championshipService
				.listAllScoreBoardImageChampionshipsByNotDeletedAndImagesNotNull(pageNum, sortField, sortDir, name);
		List<Championship> listChampionships = page.getContent();

		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-score-board-image");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		Championship championship = new Championship();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Manage Score Board Image");
		model.addAttribute("championship", championship);
		LOGGER.info("Exit listAllScoreBoardImagesByPage method -ScoreBoardImageController");

		return "administration/manage_scoreboard_image";

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/score-board-image")
	public String scoreBoardImage(Model model, HttpServletRequest request) {
		LOGGER.info("Entered scoreBoardImage method -ScoreBoardImageController");
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = new Championship();
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeletedAndImagesNull();
		model.addAttribute("pageTitle", "Add Image");
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("championship", championship);

		LOGGER.info("Exit scoreBoardImage method -ScoreBoardImageController");
		return "administration/score_board";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator','EventManager')")
	@PostMapping("/score-board-image/save")
	public String saveScoreBoardImage(Model model, @RequestParam("championship") Integer championshipId,
			@RequestParam("scoreBoardImage1") MultipartFile scoreBoardImage1,
			@RequestParam("scoreBoardImage2") MultipartFile scoreBoardImage2,
			@RequestParam("scoreBoardImage3") MultipartFile scoreBoardImage3,
			@RequestParam("scoreBoardImage4") MultipartFile scoreBoardImage4, HttpServletRequest request,
			RedirectAttributes re) throws Exception {
		LOGGER.info("Entered saveScoreBoardImage method -ScoreBoardImageController");

		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String scoreboardImageFileName = "";

		Championship championship = championshipService.get(championshipId);
		if (scoreBoardImage1.isEmpty() && scoreBoardImage2.isEmpty() && scoreBoardImage3.isEmpty()
				&& scoreBoardImage4.isEmpty()) {
			re.addFlashAttribute("message1", "Upload atleast one image.");
			return "redirect:/admin/manage-score-board-image";
		}

		if (!scoreBoardImage1.isEmpty()) {

			String uploadDir = "scoreboard-image/" + championshipId + "/image1";
			String extension = FilenameUtils.getExtension(scoreBoardImage1.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage1");
			scoreboardImageFileName = "scoreboardimage1." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage1);
			championship.setImage1(scoreboardImageFileName);

		}

		if (!scoreBoardImage2.isEmpty()) {
			String uploadDir = "scoreboard-image/" + championshipId + "/image2";
			String extension = FilenameUtils.getExtension(scoreBoardImage2.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage2");
			scoreboardImageFileName = "scoreboardimage2." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage2);
			championship.setImage2(scoreboardImageFileName);

		}

		if (!scoreBoardImage3.isEmpty()) {
			String uploadDir = "scoreboard-image/" + championshipId + "/image3";
			String extension = FilenameUtils.getExtension(scoreBoardImage3.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage3");
			scoreboardImageFileName = "scoreboardimage3." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage3);
			championship.setImage3(scoreboardImageFileName);
		}

		if (!scoreBoardImage4.isEmpty()) {
			String uploadDir = "scoreboard-image/" + championshipId + "/image4";
			String extension = FilenameUtils.getExtension(scoreBoardImage4.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage4");
			scoreboardImageFileName = "scoreboardimage4." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage4);
			championship.setImage4(scoreboardImageFileName);
		}
		championshipService.update(championship);
		re.addFlashAttribute("message", "Score board image added successfully.");
		LOGGER.info("Exit saveScoreBoardImage method -ScoreBoardImageController");
		return "redirect:/admin/manage-score-board-image";

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/score-board-image/edit/{id}")
	public String editScoreBoardImage(@PathVariable(name = "id") Integer championshipId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editScoreBoardImage method -ScoreBoardImageController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Championship championship = championshipService.get(championshipId);
			if (!championship.getStatus().equals(ChampionshipStatusEnum.DELETED)
					|| !championship.getStatus().equals(ChampionshipStatusEnum.REJECTED)) {
				model.addAttribute("pageTitle", "Edit Score Board Image");
				model.addAttribute("championship", championship);
				return "administration/score_board";

			} else {
				return "error/403";
			}
		} catch (Exception ex) {
			LOGGER.error("scoreBoardImage with given id " + championshipId + "not found!");
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editScoreBoardImage method -ScoreBoardImageController");
			return "redirect:/admin/manage-score-board-image";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/score-board-image/delete/{id}")
	public String deletescoreboardImage(@PathVariable(name = "id") Integer championshipId, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered deletescoreboardImage method -ScoreBoardImageController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String uploadDir = "scoreboard-image/" + championshipId;
		Championship championship = championshipService.findById(championshipId);
//		File directory = new File(uploadDir);
//		if (directory.exists()) {
		try {
			FileUtils.deleteDirectory(new File(uploadDir));
		} catch (IOException e) {

			LOGGER.info(e.getMessage());
		}
		championshipService.clearImages(championshipId);

		redirectAttributes.addFlashAttribute("message",
				championship.getName() + "- Images has been deleted successfully");
		// }
		LOGGER.info("Exit deletescoreboardImage method -ScoreBoardImageController");
		return "redirect:/admin/manage-score-board-image";

	}
}