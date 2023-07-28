package com.swayaan.nysf.entity;

import javax.persistence.Transient;

import com.swayaan.nysf.entity.Championship;

public class ScoreBoardImageDTO {
	private Championship championship;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
	public ScoreBoardImageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Transient
	public String getImagePath1() {
		if (this.championship == null)
			return "championship/image1/image-thumbnail.png";

		return "/scoreboard-image/" + this.championship + "/" + this.image1;
	}
	
	@Transient
	public String getImagePath2() {
		if (this.championship == null)
			return "championship/image2/image-thumbnail.png";

		return "/scoreboard-image/" + this.championship + "/" + this.image2;
	}
	@Transient
	public String getImagePath3() {
		if (this.championship == null)
			return "championship/image3/image-thumbnail.png";

		return "/scoreboard-image/" + this.championship + "/" + this.image3;
	}
	@Transient
	public String getImagePath4() {
		if (this.championship == null)
			return "championship/image4/image-thumbnail.png";

		return "/scoreboard-image/" + this.championship + "/" + this.image4;
	}
	
	public Championship getChampionship() {
		return championship;
	}
	public void setChampionship(Championship championship) {
		this.championship = championship;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
	public String getImage4() {
		return image4;
	}
	public void setImage4(String image4) {
		this.image4 = image4;
	}
	
	public ScoreBoardImageDTO(Championship championship, String image1, String image2, String image3, String image4) {
		super();
		this.championship = championship;
		this.image1 = image1;
		this.image2 = image2;
		this.image3 = image3;
		this.image4 = image4;
	}
	
}