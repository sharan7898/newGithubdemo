package com.swayaan.nysf.API.entity.livescores;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "total_d_score", "total_a_score", "total_t_score", "total_e_score", "total_score", "rank" })
public class TeamScoresDTO {
	
	private String total_d_score;
	private String total_a_score;
	private String total_t_score;
	private String total_e_score;
	private String total_score;
	public TeamScoresDTO() {
		total_d_score = total_a_score = total_t_score = total_e_score = total_score = "-";
	}
	private String rank;
	
	public String getTotal_d_score() {
		return total_d_score;
	}
	public void setTotal_d_score(String total_d_score) {
		this.total_d_score = total_d_score;
	}
	public String getTotal_a_score() {
		return total_a_score;
	}
	public void setTotal_a_score(String total_a_score) {
		this.total_a_score = total_a_score;
	}
	public String getTotal_t_score() {
		return total_t_score;
	}
	public void setTotal_t_score(String total_t_score) {
		this.total_t_score = total_t_score;
	}
	public String getTotal_e_score() {
		return total_e_score;
	}
	public void setTotal_e_score(String total_e_score) {
		this.total_e_score = total_e_score;
	}
	public String getTotal_score() {
		return total_score;
	}
	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	@Override
	public String toString() {
		return "TeamScoresDTO [total_d_score=" + total_d_score + ", total_a_score=" + total_a_score + ", total_t_score="
				+ total_t_score + ", total_e_score=" + total_e_score + ", total_score=" + total_score + ", rank=" + rank
				+ "]";
	}
	
	
	
	
}
