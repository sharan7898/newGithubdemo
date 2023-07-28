package com.swayaan.nysf.entity;

public enum RefereeTypeEnum {
	Traditional_Judge("Traditional_Judge"), Artistic_Judge("Artistic_Judge"), TimeKeeper_Judge("TimeKeeper_Judge"),
	Timer("Timer"),Stage_Manager("Stage_Manager"),Co_ordinator("Co_ordinator");

	final String refereeType;

	private RefereeTypeEnum(String refereeType) {
		this.refereeType = refereeType;
	}

}