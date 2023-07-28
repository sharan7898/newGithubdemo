package com.swayaan.nysf.entity.DTO;


public class RoundTotalScoringDTO {
	//private Championship	championship;
	//private ChampionshipRounds championshipRounds;
	private Integer championshipId;
	private Integer championshipRoundsId;
	private Integer participantTeamId;
	private String chestNumber;
	private String participantTeamName;
	private String finalScore;
	private Integer ranking;
	private boolean winner;
	private boolean finalRound;
	private String status;
	private String tieScore;

	public String getTieScore() {
		return tieScore;
	}

	public void setTieScore(String tieScore) {
		this.tieScore = tieScore;
	}

	public Integer getChampionshipId() {
		return championshipId;
	}

	public void setChampionshipId(Integer championshipId) {
		this.championshipId = championshipId;
	}

	public Integer getChampionshipRoundsId() {
		return championshipRoundsId;
	}

	public void setChampionshipRoundsId(Integer championshipRoundsId) {
		this.championshipRoundsId = championshipRoundsId;
	}

	public Integer getParticipantTeamId() {
		return participantTeamId;
	}

	public void setParticipantTeamId(Integer participantTeamId) {
		this.participantTeamId = participantTeamId;
	}

	public String getChestNumber() {
		return chestNumber;
	}

	public void setChestNumber(String chestNumber) {
		this.chestNumber = chestNumber;
	}

	public String getParticipantTeamName() {
		return participantTeamName;
	}

	public void setParticipantTeamName(String participantTeamName) {
		this.participantTeamName = participantTeamName;
	}

	
	
	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer rank) {
		this.ranking = ranking;
	}
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RoundTotalScoringDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public RoundTotalScoringDTO(Integer participantTeamId, String chestNumber, String participantTeamName,
			String finalScore) {
		super();
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
	}
	
	

	public RoundTotalScoringDTO(Integer participantTeamId, String chestNumber, String participantTeamName,
			String finalScore, Integer ranking) {
		super();
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
	}

	/*public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public ChampionshipRounds getChampionshipRounds() {
		return championshipRounds;
	}

	public void setChampionshipRounds(ChampionshipRounds championshipRounds) {
		this.championshipRounds = championshipRounds;
	}*/


	public boolean isFinalRound() {
		return finalRound;
	}

	public void setFinalRound(boolean finalRound) {
		this.finalRound = finalRound;
	}
	

	/*@Override
	public String toString() {
		return "RoundTotalScoringDTO [championship=" + championship + ", championshipRounds=" + championshipRounds
				+ ", participantTeamId=" + participantTeamId + ", chestNumber=" + chestNumber + ", participantTeamName="
				+ participantTeamName + ", finalScore=" + finalScore + ", ranking=" + ranking + "]";
	}

	public RoundTotalScoringDTO(Championship championship, ChampionshipRounds championshipRounds,
			Integer participantTeamId, String chestNumber, String participantTeamName, String finalScore,
			Integer ranking) {
		super();
		this.championship = championship;
		this.championshipRounds = championshipRounds;
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
	}*/
	
	
	@Override
	public String toString() {
		return "RoundTotalScoringDTO [championshipId=" + championshipId + ", championshipRoundsId="
				+ championshipRoundsId + ", participantTeamId=" + participantTeamId + ", chestNumber=" + chestNumber
				+ ", participantTeamName=" + participantTeamName + ", finalScore=" + finalScore + ", ranking=" + ranking
				+ ", finalRound=" + finalRound + "]";
	}
	
	
	public RoundTotalScoringDTO(Integer championshipId, Integer championshipRoundsId,
			Integer participantTeamId, String chestNumber, String participantTeamName, String finalScore,
			Integer ranking) {
		super();
		this.championshipId = championshipId;
		this.championshipRoundsId = championshipRoundsId;
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
	}
	
	
	public RoundTotalScoringDTO(Integer championshipId, Integer championshipRoundsId,
			Integer participantTeamId, String chestNumber, String participantTeamName, String finalScore,
			Integer ranking, boolean finalRound,boolean winner) {
		super();
		this.championshipId = championshipId;
		this.championshipRoundsId = championshipRoundsId;
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
		this.finalRound = finalRound;
		this.winner=winner;
	}

	public RoundTotalScoringDTO(Integer championshipId, Integer championshipRoundsId, Integer participantTeamId,
			String chestNumber, String participantTeamName, String finalScore, Integer ranking, boolean finalRound,
			boolean winner, String status) {
		super();
		this.championshipId = championshipId;
		this.championshipRoundsId = championshipRoundsId;
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
		this.finalRound = finalRound;
		this.winner=winner;
		this.status = status;
	}
	
	public RoundTotalScoringDTO(Integer championshipId, Integer championshipRoundsId, Integer participantTeamId,
			String chestNumber, String participantTeamName, String finalScore, Integer ranking, boolean finalRound,
			boolean winner, String status,String tieScore) {
		super();
		this.championshipId = championshipId;
		this.championshipRoundsId = championshipRoundsId;
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
		this.finalRound = finalRound;
		this.winner=winner;
		this.status = status;
		this.tieScore=tieScore;
	}

	
	
	

	

	/*public RoundTotalScoringDTO(Championship championship, ChampionshipRounds championshipRounds,
			Integer participantTeamId, String chestNumber, String participantTeamName, String finalScore,
			Integer ranking, boolean finalRound) {
		super();
		this.championship = championship;
		this.championshipRounds = championshipRounds;
		this.participantTeamId = participantTeamId;
		this.chestNumber = chestNumber;
		this.participantTeamName = participantTeamName;
		this.finalScore = finalScore;
		this.ranking = ranking;
		this.finalRound = finalRound;
	}*/

	
	
	
	
	
	

}
