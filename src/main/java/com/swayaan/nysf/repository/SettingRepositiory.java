package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.SettingCategory;

public interface SettingRepositiory extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);

	public Setting findByKey(String string);
}
