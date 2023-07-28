package com.swayaan.nysf.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.EmailSettingBag;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.SettingCategory;
import com.swayaan.nysf.repository.SettingRepositiory;

@Service
public class SettingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SettingService.class);

	@Autowired
	private SettingRepositiory repo;

	public List<Setting> listAllSettings() {
		LOGGER.info("Entered listAllSettings method -SettingService");
		LOGGER.info("Exit listAllSettings method -SettingService");
		return (List<Setting>) repo.findAll();
	}

	public void saveAll(Iterable<Setting> settings) {
		LOGGER.info("Entered saveAll method -SettingService");
		LOGGER.info("Exit saveAll method -SettingService");
		repo.saveAll(settings);
	}

	public List<Setting> getMailServerSettings() {
		LOGGER.info("Entered getMailServerSettings method -SettingService");
		LOGGER.info("Exit getMailServerSettings method -SettingService");
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}

	public List<Setting> getMailTemplateSettings() {
		LOGGER.info("Entered getMailTemplateSettings method -SettingService");
		LOGGER.info("Exit getMailTemplateSettings method -SettingService");
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}

	public EmailSettingBag getEmailSettings() {
		LOGGER.info("Entered getEmailSettings method -SettingService");
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));
		LOGGER.info("Exit getEmailSettings method -SettingService");
		return new EmailSettingBag(settings);
	}

	public Setting getMailTemplateValueForSubjectAndContent(String string) {
		LOGGER.info("Entered getMailTemplateValueForSubjectAndContent method -SettingService");
		LOGGER.info("Exit getMailTemplateValueForSubjectAndContent method -SettingService");
		return repo.findByKey(string);
	}

}
