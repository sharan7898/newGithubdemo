package com.swayaan.nysf.entity;

public enum ChampionshipLevel {
	District("District"),
	State("State"),
	Zonal("Zonal"),
	National("National"),
	International("International");

    private final String championshipCode;

    private ChampionshipLevel(String championshipCode) {
        this.championshipCode = championshipCode;
    }

}

