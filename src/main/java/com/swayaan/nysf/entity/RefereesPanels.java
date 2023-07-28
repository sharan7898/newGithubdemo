package com.swayaan.nysf.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "referees_panels")
public class RefereesPanels {
	
	private Integer id;
	private Judge judgeUser;
	private ChampionshipRefereePanels championshipRefereePanels;
	private JudgeType type;
	
	
	 @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "judge_type_id")
	public JudgeType getType() {
		return type;
	}

	public void setType(JudgeType type) {
		this.type = type;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
     
    
    @Transient
	public String getUserName() {
				if (judgeUser.getFirstName() != null) {
					return judgeUser.getFirstName() + " " + judgeUser.getLastName();
				}
		
		return null;
	}
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "judge_user_referee_id")
    public Judge getJudgeUser() {
		return judgeUser;
	}

	public void setJudgeUser(Judge judgeUser) {
		this.judgeUser = judgeUser;
	}

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "championship_referee_panels_id")  
    public ChampionshipRefereePanels getChampionshipRefereePanels() {
        return championshipRefereePanels;
    }
 
    public void setChampionshipRefereePanels(ChampionshipRefereePanels championshipRefereePanels) {
        this.championshipRefereePanels = championshipRefereePanels;
    }
	
}
