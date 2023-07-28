package com.swayaan.nysf.entity;

public enum GenderEnum {
	Male("Male"),
	Female("Female"),
	Common("Common");

    private final String gender;

    private GenderEnum(String gender) {
        this.gender = gender;
    }

}
