package com.swayaan.nysf.entity.DTO;

public class AsanaCategoryDTO {
	
	private Integer id;
	private String name;
	private String code;
	
	public AsanaCategoryDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public AsanaCategoryDTO(Integer id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	

}
