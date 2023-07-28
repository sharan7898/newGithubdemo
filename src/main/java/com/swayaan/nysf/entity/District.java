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
@Table(name="district")
public class District {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 64, nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

	public District() {
		super();
		// TODO Auto-generated constructor stub
	}

	public District(Integer id, String name, State state) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "District [id=" + id + ", name=" + name + ", state=" + state + "]";
	}
	
	
	
}