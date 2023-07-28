package com.swayaan.nysf.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.swayaan.nysf.csvcustomvalidators.CsvEmailCustomValidator;
import com.swayaan.nysf.csvcustomvalidators.CsvGenderCustomValidator;
import com.swayaan.nysf.csvcustomvalidators.CsvStringCustomValidator;
import com.swayaan.nysf.entity.District;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.DTO.ExcelToDBImportDTO;
import com.swayaan.nysf.exception.DistrictNotFoundException;
import com.swayaan.nysf.exception.StateNotFoundException;
import com.swayaan.nysf.repository.ParticipantRepository;
import com.swayaan.nysf.service.DistrictService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserService;

@Component
public class ExcelImportUtil {

	@Autowired
	ParticipantService participantService;

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	ParticipantRepository participantRepository;

	@Autowired
	PasswordEncodeUtil passwordEncodeUtil;

	@Autowired
	CommonUtil commonUtil;

	@Autowired
	StateService stateService;

	@Autowired
	DistrictService districtService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportUtil.class);

	public List<Participant> processCSV(MultipartFile file) throws Exception {

		LOGGER.info("Entered processCSV method - ExcelImportUtil");
		if (file.getContentType().contains("csv")) {
			List<ExcelToDBImportDTO> listExcelParticipants = new ArrayList<>();
			try {
				listExcelParticipants = validateCSV(file);
			} catch (Exception e) {
				LOGGER.info("Exception occured " + e);
				throw new Exception(e.getMessage());
			}
			if (listExcelParticipants.isEmpty()) {
				throw new Exception("FILEEMPTY");
			}
			List<Participant> accountList = importToDb(listExcelParticipants);

			return accountList;
		} else {
			throw new Exception("INVALID");
		}

	}

	private List<ExcelToDBImportDTO> validateCSV(MultipartFile file) throws Exception {
		LOGGER.info("Entered validateCSV method - ExcelImportUtil");
		List<ExcelToDBImportDTO> participantList = new ArrayList<>();
		ICsvBeanReader beanReader = null;

		try {
			InputStream inputStream = file.getInputStream();
			// beanReader = new CsvBeanReader(new FileReader(CSV_FILENAME),
			// CsvPreference.STANDARD_PREFERENCE);
			beanReader = new CsvBeanReader(
					new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)),
					CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names must match)
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			ExcelToDBImportDTO participant;
			while ((participant = beanReader.read(ExcelToDBImportDTO.class, header, processors)) != null) {

				LOGGER.info(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
						beanReader.getRowNumber(), participant));

				customValidate(participant);
				participantList.add(participant);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			
			throw new Exception("Invalid data at row " + beanReader.getLineNumber() + " - " + e.getMessage());

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}

		return participantList;

	}

	private ExcelToDBImportDTO customValidate(ExcelToDBImportDTO participant) throws Exception {
		LOGGER.info("Entered validateAndAddToList method - ExcelImportUtil");
		
		if (participant.getEmail().trim().length()>128) {
			throw new Exception("Invalid Email Id " + participant.getEmail() + ".The length should not exceed 128 characters");
		}
		if (userService.ExistByEmail(participant.getEmail().trim())) {
			throw new Exception("The User with the email " + participant.getEmail() + " already exists");
		}

		State state = stateService.findByName(participant.getState());

		if (state != null) {
			District district = districtService.findByStateAndName(state, participant.getDistrict());
			if (district == null) {
				throw new Exception("The district is not present");
			}
		} else {
			throw new Exception("The State is not present");
		}
		return participant;

	}

	private List<Participant> importToDb(List<ExcelToDBImportDTO> listExcelParticipants) throws Exception {
		LOGGER.info("Entered importToDb method - ExcelImportUtil");
		List<Participant> participantList = new ArrayList<>();
		for (ExcelToDBImportDTO excelParticipant : listExcelParticipants) {
			Participant participant = null;
			try {
				participant = convertExcelImportParticipantToParticipant(excelParticipant);
			} catch (StateNotFoundException | DistrictNotFoundException e) {
				LOGGER.info("Exception occured " + e);
				throw new Exception("Not found exception occured");
			}
			Participant savedParticipant = participantService.save(participant);
			participantList.add(savedParticipant);
		}
		return participantList;
	}

	private Participant convertExcelImportParticipantToParticipant(ExcelToDBImportDTO excelParticipant)
			throws StateNotFoundException, DistrictNotFoundException {
		LOGGER.info("Entered convertToParticipant method - ExcelImportUtil");
		Participant participant = new Participant();
		String prnNumber = commonUtil.getSystemGeneratedNumber();
		String userName = String.valueOf(prnNumber);
		String password = "Prn$" + userName;
		String encodedPassword = passwordEncodeUtil.encodePassword(password);
		Set<Role> role = roleService.getRoleById(3);

		participant.setFirstName(excelParticipant.getFirstName());
		participant.setLastName(excelParticipant.getLastName());
		participant.setEmail(excelParticipant.getEmail());
		participant.setDob(excelParticipant.getDob());
		participant.setUserPrnNumber(excelParticipant.getUserPrnNumber());
		participant.setGender(excelParticipant.getGender());
		State state = stateService.findByName(excelParticipant.getState());
		participant.setState(state);
		participant.setDistrict(districtService.findByStateAndName(state, excelParticipant.getDistrict()));
		participant.setPrnNumber(prnNumber);
		participant.setUserName(userName);
		participant.setPassword(encodedPassword);
		participant.setApprovalStatus(true);
		participant.setEnabled(true);
		participant.setAcceptRules(true);
		participant.setPhysicallyFit(true);
		participant.setRoles(role);

		return participant;
	}

	private CellProcessor[] getProcessors() {
		LOGGER.info("Entered getProcessors method - ExcelImportUtil");
		    
		CellProcessor[] processors = new CellProcessor[] { new NotNull(new CsvStringCustomValidator()), // firstname
				new NotNull(new CsvStringCustomValidator()), // lastname
				new NotNull(new ParseDate("dd-MM-yyyy")), // dob
				new NotNull(new Unique(new CsvEmailCustomValidator())), // Email
				new Optional(), // userPrnNumber
				new NotNull(new CsvGenderCustomValidator()), // gender
				new NotNull(), // district
				new NotNull(), // state
				null // note
		};

		return processors;
	}

}
