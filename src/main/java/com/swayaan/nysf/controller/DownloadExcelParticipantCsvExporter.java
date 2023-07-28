package com.swayaan.nysf.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.swayaan.nysf.entity.DTO.ExcelToDBImportDTO;

public class DownloadExcelParticipantCsvExporter {

	// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public void export(HttpServletResponse response) throws IOException, ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String timeStamp = dateFormatter.format(new Date());
		String fileName = "Participant_data" + timeStamp + ".csv";
		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "FirstName", "LastName", "DOB", "Email", "UserPrnNumber", "Gender", "District", "State",
				"Note" };

		csvWriter.writeHeader(csvHeader);

		// write the beans
		csvWriter.write(getNote(), csvHeader);

		csvWriter.close();
	}

	private ExcelToDBImportDTO getNote() {
		String note = "All fields are mandatory except UserPrnNumber,Email Ids should be unique,DOB must be in the dd-MM-yyyy format,Gender must be Male or Female,District and State name must be according to the system - For details - https://en.wikipedia.org/wiki/List_of_districts_in_India";

		ExcelToDBImportDTO noteData = new ExcelToDBImportDTO(note);
		return noteData;
	}
}
