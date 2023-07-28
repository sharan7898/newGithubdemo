package com.swayaan.nysf.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.swayaan.nysf.entity.DTO.UserAuditLogDTO;

public class UserAuditLogCsvExporter {
	
	//DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	public void export(List<com.swayaan.nysf.entity.UserAuditLog> listAuditReport, HttpServletResponse response) throws IOException, ParseException
	{
		DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp=dateFormatter.format(new Date());
		String fileName= "User_Audit_Report"+ timeStamp +".csv";
		response.setContentType("text/csv");
		String headerKey="Content-Disposition";
		String headerValue= "attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
		ICsvBeanWriter csvWriter= new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"Id","User","Login Time","Logout Time","IP Address"};
		String[] fieldMapping= {"id","userName","LoginTime","LogoutTime","IPAddress"};
		csvWriter.writeHeader(csvHeader);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
		
		for(com.swayaan.nysf.entity.UserAuditLog auditReport: listAuditReport)
		{
			UserAuditLogDTO exportAuditLog = new UserAuditLogDTO();
			exportAuditLog.setId(auditReport.getId());
			exportAuditLog.setUserName(auditReport.getLoggedUser().getFullName());
			exportAuditLog.setLoginTime(auditReport.getLoginTime());
			if(auditReport.getLogoutTime()!= null) {
				exportAuditLog.setLogoutTime(auditReport.getLogoutTime());
			} else {
				exportAuditLog.setLogoutTime("-");
			}
			exportAuditLog.setIpAddress(auditReport.getIpAddress());
			csvWriter.write(exportAuditLog, fieldMapping);
		
		}
		csvWriter.close();
	}
	
}
