package com.swayaan.nysf.API.entity.winners;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"prn_id","kitd_unique_id","name","PlayerImage","player_total_score","asanas"})
public class AthletesDTO {
	private String prn_id;
	private String kitd_unique_id;
	private String name;
	private String PlayerImage;
	private String player_total_score;
	private List<AsanasDTO> asanas;
	public String getPrn_id() {
		return prn_id;
	}
	public void setPrn_id(String prn_id) {
		this.prn_id = prn_id;
	}
	public String getKitd_unique_id() {
		return kitd_unique_id;
	}
	public void setKitd_unique_id(String kitd_unique_id) {
		this.kitd_unique_id = kitd_unique_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonGetter("PlayerImage")
	public String getPlayerImage() {
		return PlayerImage;
	}
	public void setPlayerImage(String playerImage) {
		PlayerImage = playerImage;
	}
	public String getPlayer_total_score() {
		return player_total_score;
	}
	public void setPlayer_total_score(String player_total_score) {
		this.player_total_score = player_total_score;
	}
	public List<AsanasDTO> getAsanas() {
		return asanas;
	}
	public void setAsanas(List<AsanasDTO> asanas) {
		this.asanas = asanas;
	}
	public AthletesDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AthletesDTO(String prn_id, String kitd_unique_id, String name, String playerImage, String player_total_score,
			List<AsanasDTO> asanas) {
		super();
		this.prn_id = prn_id;
		this.kitd_unique_id = kitd_unique_id;
		this.name = name;
		PlayerImage = playerImage;
		this.player_total_score = player_total_score;
		this.asanas = asanas;
	}
	

}
