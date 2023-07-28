package com.swayaan.nysf.utils;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.NavigationHistoryService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.UserService;

@Component
public class CommonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	public static UserService userService;

	public static JudgeService judgeService;

	public static ParticipantService participantService;

	public static NavigationHistoryService navigationHistoryService;
	

	@Autowired
	private CommonUtil(UserService userService, JudgeService judgeService, ParticipantService participantService,
			NavigationHistoryService navigationHistoryService) {
		CommonUtil.userService = userService;
		CommonUtil.judgeService = judgeService;
		CommonUtil.participantService = participantService;
		CommonUtil.navigationHistoryService = navigationHistoryService;
	}

	public static User getCurrentUser() {
		LOGGER.info("Entered getCurrentUser in CommonUtil");
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		LOGGER.debug("Username :" + username);
		User user = userService.getByEmail(username);
		return user;
	}

	public static Judge getCurrentJudge() throws JudgeNotFoundException {
		LOGGER.info("Entered getCurrentJudge in CommonUtil");
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		LOGGER.debug("Username :" + username);
		User user = userService.getByEmail(username);
		Judge judge = judgeService.findById(user.getId());
		return judge;
	}

	public static Participant getCurrentParticipant() throws ParticipantNotFoundException {
		LOGGER.info("Entered getCurrentparticipant in CommonUtil");
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		LOGGER.debug("Username :" + username);
		User user = userService.getByEmail(username);
		Participant participant = participantService.getParticipantById(user.getId());
		return participant;
	}

	public static void setNavigationHistory(String mappedUrl, User currentUser) {
		LOGGER.info("Entered setNavigationHistory in CommonUtil");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		// converting to IST
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		String istDateTime = simpleDateFormat.format(date);
		NavigationHistory navigation = new NavigationHistory();
		navigation.setUser(currentUser);
		navigation.setUserName(currentUser.getFullName());
		navigation.setRoleName(currentUser.getRoleName());
		navigation.setUrl(mappedUrl);
		navigation.setCreatedTime(istDateTime);
		navigationHistoryService.save(navigation);
	}

//	public static int generateJRNNumber() {
//		Random random = new Random();
//		int number = 10000 + random.nextInt(89999);
//		return number;
//	}
//
//	public static int generateERNNumber() {
//		Random random = new Random();
//		int number = 1000 + random.nextInt(8999);
//		return number;
//	}

	public String getSystemGeneratedNumber() {
		Random random = new Random();
		Year currentYear = Year.now();
		String year = currentYear.toString();
		int number = random.nextInt(89999999);
		int concatenatedNumber = 10000000 + number;
		String finalGeneratedNumber= year+ String.valueOf(concatenatedNumber);
		boolean isPresent = userService.ExistByUserName(finalGeneratedNumber);
		if(isPresent) {
			return getSystemGeneratedNumber();
		} else {
			return finalGeneratedNumber;
		}
	

	}

	

}
