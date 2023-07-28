package com.swayaan.nysf.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantScoringJudge;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.Role;

@Repository
public interface ParticipantScoringJudgeRepository extends CrudRepository<ParticipantScoringJudge, Integer> {

	ParticipantScoringJudge findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
			ParticipantTeamReferees participantTeamReferees, ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipCategory championshipCategory, Championship championship, JudgeType judgeType, Integer round);

}
