package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.repository.EventManagerRegistrationRepository;
import com.swayaan.nysf.repository.JudgeRegistrationRepository;
import com.swayaan.nysf.repository.ParticipantRegistrationRepository;
import com.swayaan.nysf.repository.RoleRepository;
import com.swayaan.nysf.repository.UserRepository;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private ParticipantRegistrationRepository participantRegistrationRepo;

	@Autowired
	private JudgeRegistrationRepository judgeRegistrationRepo;

	@Autowired
	private EventManagerRegistrationRepository eventmanagerRegistrationRepo;

	@Autowired
	private PasswordEncodeUtil passwordEncodeUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Value("${site.admin-mail}")
	private String siteAdminMail;

	public User getByEmail(String email) {
		LOGGER.info("Entered getByEmail method -UserService");
		LOGGER.info("Exit getByEmail method -UserService");
		return userRepo.getUserByEmail(email);
	}

	public List<User> listAll() {
		LOGGER.info("Entered listAll method -UserService");
		LOGGER.info("Exit listAll method -UserService");
		return (List<User>) userRepo.findAll();
	}

	public List<Role> listRoles() {
		LOGGER.info("Entered listRoles method -UserService");
		LOGGER.info("Exit listRoles method -UserService");
		return (List<Role>) roleRepo.findAll();
	}

	private void encodePassword(User user) {
		LOGGER.info("Entered encodePassword method -UserService");
		LOGGER.info("Exit encodePassword method -UserService");
		String encodedPassword = passwordEncodeUtil.encodePassword(user.getPassword());
		user.setPassword(encodedPassword);
	}

//	public String updateResetPasswordToken(String email) throws UserNotFoundException {
//		LOGGER.info("Entered updateResetPasswordToken method -UserService");
//		User user = userRepo.getByEmail(email);
//
//		if (user != null) {
//			String token = RandomString.make(30);
//			user.setResetpasswordToken(token);
//			userRepo.save(user);
//			LOGGER.info("Exit updateResetPasswordToken method -UserService");
//			return token;
//		} else {
//			throw new UserNotFoundException("could not find any user with the email" + " " + email);
//
//		}
//
//	}
	
	public String updateResetPasswordToken(String username) throws UserNotFoundException {
		LOGGER.info("Entered updateResetPasswordToken method -UserService");
		User user = userRepo.getUserByUserName(username);

		if (user != null) {
			String token = RandomString.make(30);
			user.setResetpasswordToken(token);
			userRepo.save(user);
			LOGGER.info("Exit updateResetPasswordToken method -UserService");
			return token;
		} else {
			throw new UserNotFoundException("could not find any user with the username" + " " + username);

		}

	}

	public User getByResetPasswordToken(String token) {
		LOGGER.info("Entered getByResetPasswordToken method -UserService");
		LOGGER.info("Exit getByResetPasswordToken method -UserService");
		return userRepo.findByResetpasswordToken(token);
	}

	public void updatePassword(String token, String newPassword) throws UserNotFoundException {
		LOGGER.info("Entered updatePassword method -UserService");
		LOGGER.info("Exit updatePassword method -UserService");
		User user = userRepo.findByResetpasswordToken(token);

		if (user == null) {
			throw new UserNotFoundException("No User found:  Invalid Token");
		}
		user.setPassword(newPassword);
		user.setResetpasswordToken(null);
		encodePassword(user);
		userRepo.save(user);
	}
//	/*public List<Country> listAllCountries(){
//		return (List<Country>) countryRepo.findAllByOrderByNameAsc();
//	}*/
//
//	public User save(User user) {
//		User currentUser = CommonUtil.getCurrentUser();	
//		boolean isUpdatingUser = (user.getId() != null);
//		if (isUpdatingUser) {
//			User existingUser = userRepo.findById(user.getId()).get();
//			if (user.getPassword().isEmpty()) {
//				user.setPassword(existingUser.getPassword());
//			} else {
//				encodePassword(user);
//			}
//			
//			String userEmail = user.getEmail();
//			if(userEmail.equals(siteAdminMail)) {
//				// do not change role or status
//				user.setRoles(existingUser.getRoles());
//				user.setEnabled(true);
//			}
//			
//			user.setLastModifiedBy(currentUser);
//			user.setLastModifiedTime(new Date());
//			user.setCreatedBy(existingUser.getCreatedBy());
//			user.setCreatedTime(existingUser.getCreatedTime());
//			
//		} else {		
//			encodePassword(user);
//			user.setCreatedBy(currentUser);
//			user.setCreatedTime(new Date());
//		}
//		return userRepo.save(user);
//		
//	}
//	
//	
//	private void encodePassword(User user) {
//		String encodedPassword = passwordEncoder.encode(user.getPassword());		
//		user.setPassword(encodedPassword);
//	}
//	
//	public boolean isEmailUnique(Integer id, String email) {
//		User userByEmail = userRepo.getByEmail(email);
//		
//		if (userByEmail == null) {
//			return true;
//		}
//		
//		boolean isCreatingNew = (id == null);
//		
//		if (isCreatingNew) {
//			if (userByEmail != null) {return false;}
//		} else {
//			if (userByEmail.getId() != id) {return false;}
//		}
//		
//		return true;
//	}

	public boolean isEmailUnique(Integer id, String email) {
		LOGGER.info("Entered isEmailUnique method -UserService");
		User userByEmail = userRepo.getByEmail(email);
		ParticipantRegistration participantRegistrationByEmail = participantRegistrationRepo
				.getParticipantRegistrationByEmail(email);
		JudgeRegistration judgeRegistrationByEmail = judgeRegistrationRepo.getJudgeRegistrationByEmail(email);
		EventManagerRegistration eventmanagerRegistrationByEmail = eventmanagerRegistrationRepo
				.geteventmanagerRegistrationByEmail(email);

		if (id != null) {
			return true;
		} else {

			if (userByEmail == null && participantRegistrationByEmail == null && judgeRegistrationByEmail == null
					&& eventmanagerRegistrationByEmail == null) {

				return true;
			} else {
				LOGGER.info("Exit isEmailUnique method -UserService");
				return false;
			}
		}

	}

	public User get(Integer id) throws UserNotFoundException {
		LOGGER.info("Entered get method -UserService");

		try {
			LOGGER.info("Exit get method -UserService");
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}

//	
//	public void delete(Integer id) throws UserNotFoundException {
//		Long countById = userRepo.countById(id);
//		if (countById == null || countById == 0) {
//			throw new UserNotFoundException("Could not find any user with ID " + id);
//		}
//		
//		userRepo.deleteById(id);
//	}
//	
//	public void updateUserEnabledStatus(Integer id, boolean enabled) {
//		userRepo.updateEnabledStatus(id, enabled);
//	}
//
	public User updateUserProfile(User userInForm) {
		LOGGER.info("Entered updateUserProfile method -UserService");

		User userInDB = userRepo.findById(userInForm.getId()).get();

		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			userInDB.setPassword(passwordEncodeUtil.encodePassword(userInDB.getPassword()));
		}
		userInDB.setUserName(userInDB.getUserName());
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		userInDB.setPhoneNumber(userInForm.getPhoneNumber());
		// add address fields
		userInDB.setAddressLine1(userInForm.getAddressLine1());
		userInDB.setAddressLine2(userInForm.getAddressLine2());
		userInDB.setCity(userInForm.getCity());
		userInDB.setState(userInForm.getState());
		userInDB.setDistrict(userInForm.getDistrict());
		// userInDB.setCountry(userInForm.getCountry());
		userInDB.setPostalCode(userInForm.getPostalCode());
		userInForm.setResetpasswordToken(userInDB.getResetpasswordToken());
		LOGGER.info("Exit updateUserProfile method -UserService");

		return userRepo.save(userInDB);

	}
//	
//
//	public void addUser(User user) {
//		
//		//encodePassword(user);
//		user.setEnabled(false);
//		user.setCreatedTime(new Date());
//		
//		String randomcode = RandomString.make(64);
////		user.setVerificationCode(randomcode);
//		
//		userRepo.save(user);
//	}
//	
//	public boolean verify(String verificationCode) {
//		
//		User user = userRepo.findUserByverificationCode(verificationCode);
//		if(user == null || user.isEnabled()) {
//			return false;
//		} else {
//			userRepo.updateEnabledStatus(user.getId(), true);
//			
//			
//		}
//		
//		
//		return false;
//	}
//
//	public String updateResetPasswordToken(String email) throws UserNotFoundException {
//		
//		User user = userRepo.getUserByEmail(email);
//	
//		if(user != null) {
//			String token = RandomString.make(30);
//			user.setResetpasswordToken(token);
//			userRepo.save(user);
//			
//			return token;
//		} else {
//			throw new UserNotFoundException("could not find any user with the email"+ " " + email );
//			
//		}
//		
//	}
//	
//	public User getByResetPasswordToken(String token) {
//		return userRepo.findByResetpasswordToken(token);
//	}
//	
//	public void updatePassword(String token,String newPassword) throws UserNotFoundException {
//		User user = userRepo.findByResetpasswordToken(token);
//		
//		if(user == null) {
//			throw new UserNotFoundException("No User found:  Invalid Token");
//		}
//		user.setPassword(newPassword);
//		user.setResetpasswordToken(null);
//		encodePassword(user);
//		userRepo.save(user);
//	}
//	
//	public User assignIetpForUser(User user) {
//		System.out.println("assignIetpForUser Repo called");
//		return userRepo.save(user);
//	}
//
//	public List<User> listAllReferees() {
//		List<User> listAllUsers = listAll();
//		List<User> listRefereeUsers = new ArrayList<>();
//		for(User user : listAllUsers) {
//			if(user.hasRole("Judge")) {
//				listRefereeUsers.add(user);
//			}
//		}
//		return listRefereeUsers;
//	}
//
//	public List<User> getTraditionalReferees(int count, String type) {
//		return userRepo.findReferees(count,type);	
//	}
//
//	public List<User> getArtisticReferees(int count, String type) {
//		return userRepo.findReferees(count,type);	
//	}
//	
//	public List<User> getTimekeeperReferees(int count, String type) {
//		return userRepo.findReferees(count,type);	
//	}
//	
////	public List<User> getTraditionalReferees(String type) {
////		return userRepo.findRefereesType(type);	
////	}
////
////	public List<User> getArtisticReferees(String type) {
////		return userRepo.findRefereesType(type);	
////	}
////	
////	public List<User> getTimekeeperReferees(String type) {
////		return userRepo.findRefereesType(type);	
////	}
////
////	public List<User> getNonSelectedTraditionalReferees(String type,Integer eventRefereePanelId) {
////	
////		return userRepo.findNonSelectedRefereesType(type,eventRefereePanelId);
////	}
////
////	public List<User> getNonSelectedTimeKeeperReferees(String type, Integer eventRefereePanelId) {
////		
////		return userRepo.findNonSelectedRefereesType(type,eventRefereePanelId);
////	}
////
////	public List<User> getNonSelectedArtisticReferees(String type, Integer eventRefereePanelId) {
////		return userRepo.findNonSelectedRefereesType(type,eventRefereePanelId);
////	}
//	public List<User> getNonSelectedEnabledReferees(Integer championshipRefereePanelId){
//		boolean enabled=true;
//		List<User> listUsers =userRepo.findAllBychampionshipRefereePanelIdAndEnabledOrderByIdAsc(championshipRefereePanelId,enabled);
//		List<User> listJudgeTypeUsers=new ArrayList<User>();
//		for(User user:listUsers)
//		{
////			if(user.getIsJudge()){
////				listJudgeTypeUsers.add(user);
////			}
//		}
//		return listJudgeTypeUsers;
//	}
//	

	public User getByUserName(String username) {
		LOGGER.info("Entered getByUserName method -UserService");
		LOGGER.info("Exit getByUserName method -UserService");
		return userRepo.getUserByUserName(username);
	}

	public int getCount() {
		LOGGER.info("Entered getCount method -UserService");
		LOGGER.info("Exit getCount method -UserService");
		return (int) userRepo.count();
	}

	public boolean ExistByUserName(String userName) {
		LOGGER.info("Entered ExistByUserName method -UserService");
		LOGGER.info("Exit ExistByUserName method -UserService");
		return userRepo.existsByUserName(userName);
	}

	public List<Integer> getUserByNotJudgeAndParticipant() {
		LOGGER.info("Entered getUserByNotJudgeAndParticipant method -UserService");
		LOGGER.info("Exit getUserByNotJudgeAndParticipant method -UserService");
		return userRepo.findAllByNotJudgeAndParticipant();
	}

	public boolean ExistByEmail(String email) {
		LOGGER.info("Entered ExistByEmail method -UserService");
		LOGGER.info("Exit ExistByEmail method -UserService");
		return userRepo.existsByEmail(email);
	}

	public List<Integer> getAllEventManagers() {
		LOGGER.info("Entered getAllEventManagers method -UserService");
		LOGGER.info("Exit getAllEventManagers method -UserService");
		return userRepo.findAllEventManagers();
	}
}
