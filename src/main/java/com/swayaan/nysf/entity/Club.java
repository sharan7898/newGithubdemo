package com.swayaan.nysf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "club")
public class Club {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(name = "address",length = 64, nullable = false)
	private String address;
	
	private Integer externalClubId;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;
	

	public Club() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Club(Integer id, String name, String address) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getExternalClubId() {
		return externalClubId;
	}

	public void setExternalClubId(Integer externalClubId) {
		this.externalClubId = externalClubId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Club [id=" + id + ", name=" + name + ", address=" + address + "]";
	}

	
}
