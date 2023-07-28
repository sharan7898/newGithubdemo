package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.AsanaStatusEnum;

public class RoundAsanaStatusDTO {
	
	private Integer roundNumber;
	
	private String status;
	
	public RoundAsanaStatusDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoundAsanaStatusDTO(Integer roundNumber, String status) {
		super();
		this.roundNumber = roundNumber;
		this.status = status;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RoundAsanaStatusDTO [roundNumber=" + roundNumber + ", status=" + status + "]";
	}
	
	

}
