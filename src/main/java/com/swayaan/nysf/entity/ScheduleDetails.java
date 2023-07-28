package com.swayaan.nysf.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScheduleDetails {
	
	@Value("${schedule.livescores.description}")
	private String livescoresDescription;
	
	@Value("${schedule.winners.description}")
	private String winnersDescription;
	
	@Value("${schedule.male.category.id}")
	private Integer maleCategoryId;
	
	@Value("${schedule.female.category.id}")
	private Integer femaleCategoryId;
	
	@Value("${schedule.traditional.male.event.id}")
	private Integer traditionalMaleEventId;
	
	@Value("${schedule.traditional.female.event.id}")
	private Integer traditionalFemaleEventId;
	
	@Value("${schedule.artistic.single.male.event.id}")
	private Integer artisticSingleMaleEventId;
	
	@Value("${schedule.artistic.single.female.event.id}")
	private Integer artisticSingleFemaleEventId;
	
	@Value("${schedule.artistic.pair.male.event.id}")
	private Integer artisticPairMaleEventId;
	
	@Value("${schedule.artistic.pair.female.event.id}")
	private Integer artisticPairFemaleEventId;
	
	@Value("${schedule.rhythmic.pair.male.event.id}")
	private Integer rhythmicPairMaleEventId;
	
	@Value("${schedule.rhythmic.pair.female.event.id}")
	private Integer rhythmicPairFemailEventId;
	
	@Value("${schedule.artistic.group.male.event.id}")
	private Integer artisticGroupMaleEventId;
	
	@Value("${schedule.artistic.group.female.event.id}")
	private Integer artisticGroupFemaleEventId;

	public String getLivescoresDescription() {
		return livescoresDescription;
	}

	public void setLivescoresDescription(String livescoresDescription) {
		this.livescoresDescription = livescoresDescription;
	}

	public String getWinnersDescription() {
		return winnersDescription;
	}

	public void setWinnersDescription(String winnersDescription) {
		this.winnersDescription = winnersDescription;
	}

	public Integer getMaleCategoryId() {
		return maleCategoryId;
	}

	public void setMaleCategoryId(Integer maleCategoryId) {
		this.maleCategoryId = maleCategoryId;
	}

	public Integer getFemaleCategoryId() {
		return femaleCategoryId;
	}

	public void setFemaleCategoryId(Integer femaleCategoryId) {
		this.femaleCategoryId = femaleCategoryId;
	}

	public Integer getTraditionalMaleEventId() {
		return traditionalMaleEventId;
	}

	public void setTraditionalMaleEventId(Integer traditionalMaleEventId) {
		this.traditionalMaleEventId = traditionalMaleEventId;
	}

	public Integer getTraditionalFemaleEventId() {
		return traditionalFemaleEventId;
	}

	public void setTraditionalFemaleEventId(Integer traditionalFemaleEventId) {
		this.traditionalFemaleEventId = traditionalFemaleEventId;
	}

	public Integer getArtisticSingleMaleEventId() {
		return artisticSingleMaleEventId;
	}

	public void setArtisticSingleMaleEventId(Integer artisticSingleMaleEventId) {
		this.artisticSingleMaleEventId = artisticSingleMaleEventId;
	}

	public Integer getArtisticSingleFemaleEventId() {
		return artisticSingleFemaleEventId;
	}

	public void setArtisticSingleFemaleEventId(Integer artisticSingleFemaleEventId) {
		this.artisticSingleFemaleEventId = artisticSingleFemaleEventId;
	}

	public Integer getArtisticPairMaleEventId() {
		return artisticPairMaleEventId;
	}

	public void setArtisticPairMaleEventId(Integer artisticPairMaleEventId) {
		this.artisticPairMaleEventId = artisticPairMaleEventId;
	}

	public Integer getArtisticPairFemaleEventId() {
		return artisticPairFemaleEventId;
	}

	public void setArtisticPairFemaleEventId(Integer artisticPairFemaleEventId) {
		this.artisticPairFemaleEventId = artisticPairFemaleEventId;
	}

	public Integer getRhythmicPairMaleEventId() {
		return rhythmicPairMaleEventId;
	}

	public void setRhythmicPairMaleEventId(Integer rhythmicPairMaleEventId) {
		this.rhythmicPairMaleEventId = rhythmicPairMaleEventId;
	}

	public Integer getRhythmicPairFemailEventId() {
		return rhythmicPairFemailEventId;
	}

	public void setRhythmicPairFemailEventId(Integer rhythmicPairFemailEventId) {
		this.rhythmicPairFemailEventId = rhythmicPairFemailEventId;
	}

	public Integer getArtisticGroupMaleEventId() {
		return artisticGroupMaleEventId;
	}

	public void setArtisticGroupMaleEventId(Integer artisticGroupMaleEventId) {
		this.artisticGroupMaleEventId = artisticGroupMaleEventId;
	}

	public Integer getArtisticGroupFemaleEventId() {
		return artisticGroupFemaleEventId;
	}

	public void setArtisticGroupFemaleEventId(Integer artisticGroupFemaleEventId) {
		this.artisticGroupFemaleEventId = artisticGroupFemaleEventId;
	}


	
	

}
