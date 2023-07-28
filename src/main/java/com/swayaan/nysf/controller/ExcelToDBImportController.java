package com.swayaan.nysf.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.StateNotFoundException;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.ExcelImportUtil;

@Controller
@RequestMapping("/admin")
public class ExcelToDBImportController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelToDBImportController.class);

	@Autowired
	ExcelImportUtil excelImportUtil;

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/import-excel-file")
	public String addExcelFileform(Model model) {
		LOGGER.info("Entered addExcelFileform method -ExcelToDBImportController");

		model.addAttribute("pageTitle", "Excel To DB Import");
		LOGGER.info("Exit addExcelFileform method -ExcelToDBImportController");

		return "administration/excel_import_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/import-excel-file/import")
	@ResponseBody
	public ResponseEntity<Object> processExcelForm(@RequestParam(name = "excelFile") MultipartFile file, Model model,
			RedirectAttributes re) throws ParseException, StateNotFoundException {
		LOGGER.info("Entered processExcelForm method -ExcelToDBImportController");

		try {
			List<Participant> accountlist = excelImportUtil.processCSV(file);
			return new ResponseEntity<>(accountlist, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Exception occured "+e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);

		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/download-excel/content")
	public String downloadExcelFile(Model model, RedirectAttributes redirectAttributes, HttpServletResponse response,
			HttpServletRequest request) throws IOException, ParseException {
		LOGGER.info("Entered downloadExcelFile method -ExcelToDBImportController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		DownloadExcelParticipantCsvExporter downloadExcelParticipantCsvExporter = new DownloadExcelParticipantCsvExporter();
		downloadExcelParticipantCsvExporter.export(response);
		LOGGER.info("Exit downloadExcelFile method -ExcelToDBImportController");

		return "redirect:/admin/manage-participant";
	}

	

}
