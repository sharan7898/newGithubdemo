package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "settings")
public class Setting {
	
	@Id
	@Column(name="keyname",nullable = false,length = 128)
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String key;
	
	@Column(nullable = false,length = 1024)
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45,nullable = false)
	private SettingCategory category;
	
    public Setting() {
    }
    
    public Setting(String key) {
    	this.key = key;
    }

	public Setting(String key, String value, SettingCategory category) {
		super();
		this.key = key;
		this.value = value;
		this.category = category;
	}


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SettingCategory getCategory() {
		return category;
	}

	public void setCategory(SettingCategory category) {
		this.category = category;
	}


	@Override
	public int hashCode() {
		return Objects.hash(key);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Setting other = (Setting) obj;
		return Objects.equals(key, other.key);
	}
	
	
}
