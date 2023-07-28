package com.swayaan.nysf.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.DTO.NavigationHistoryDTO;


public class NavigationHistoryCsvExporter {
	
	//DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationHistoryCsvExporter.class);

	
	public void export(List<NavigationHistory> listNavigationHistory, HttpServletResponse response) throws IOException, ParseException
	{
		LOGGER.info("Entered export method -NavigationHistoryCsvExporterController");

		DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp=dateFormatter.format(new Date());
		String fileName= "Navigation_History"+ timeStamp +".csv";
		response.setContentType("text/csv");
		String headerKey="Content-Disposition";
		String headerValue= "attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
		ICsvBeanWriter csvWriter= new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"Id","UserName","Role","Url Hit","Date&Time"};
		String[] fieldMapping= {"id","userName","roleName","url","createdTime"};
		csvWriter.writeHeader(csvHeader);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
		
		for(NavigationHistory NavigationHistory: listNavigationHistory)
		{
			NavigationHistoryDTO exportAuditLog = new NavigationHistoryDTO();
			exportAuditLog.setId(NavigationHistory.getId());
			exportAuditLog.setUserName(NavigationHistory.getUser().getFullName());
			exportAuditLog.setRoleName(NavigationHistory.getRoleName());
			exportAuditLog.setUrl(NavigationHistory.getUrl());
			
			exportAuditLog.setCreatedTime(NavigationHistory.getCreatedTime());
			csvWriter.write(exportAuditLog, fieldMapping);
		
		}
		csvWriter.close();
	}
	
}
