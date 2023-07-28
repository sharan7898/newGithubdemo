package com.swayaan.nysf.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AsanaLimit {
	
	@Value("${asana.traditional.single.limit}")
	private Integer traditionalSingleLimit;
	
	@Value("${asana.artistic.single.limit}")
	private Integer artisticSingleLimit;

	@Value("${asana.artistic.pair.limit}")
	private Integer artisticPairLimit;
	
	@Value("${asana.rhythmic.pair.limit}")
	private Integer rhythmicPairLimit;
	
	@Value("${asana.artistic.group.limit}")
	private Integer artisticGroupLimit;
	
	@Value("${asana.common.limit}")
	private Integer commonLimit;

	public Integer getTraditionalSingleLimit() {
		return traditionalSingleLimit;
	}

	public void setTraditionalSingleLimit(Integer traditionalSingleLimit) {
		this.traditionalSingleLimit = traditionalSingleLimit;
	}

	public Integer getArtisticSingleLimit() {
		return artisticSingleLimit;
	}

	public void setArtisticSingleLimit(Integer artisticSingleLimit) {
		this.artisticSingleLimit = artisticSingleLimit;
	}

	public Integer getArtisticPairLimit() {
		return artisticPairLimit;
	}

	public void setArtisticPairLimit(Integer artisticPairLimit) {
		this.artisticPairLimit = artisticPairLimit;
	}

	public Integer getRhythmicPairLimit() {
		return rhythmicPairLimit;
	}

	public void setRhythmicPairLimit(Integer rhythmicPairLimit) {
		this.rhythmicPairLimit = rhythmicPairLimit;
	}

	public Integer getArtisticGroupLimit() {
		return artisticGroupLimit;
	}

	public void setArtisticGroupLimit(Integer artisticGroupLimit) {
		this.artisticGroupLimit = artisticGroupLimit;
	}

	public Integer getCommonLimit() {
		return commonLimit;
	}

	public void setCommonLimit(Integer commonLimit) {
		this.commonLimit = commonLimit;
	}

	
}
