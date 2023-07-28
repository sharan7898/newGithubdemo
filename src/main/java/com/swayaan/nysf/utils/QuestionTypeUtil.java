package com.swayaan.nysf.utils;

import org.springframework.stereotype.Component;

import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.QuestionTypeEnum;

@Component
public class QuestionTypeUtil {
	
	
	public QuestionTypeEnum getQuestionType(AsanaEvaluationQuestions asanaEvaluationQuestions) {
		QuestionTypeEnum questionType = null;
		
		if(asanaEvaluationQuestions.getForEachAsana()==true && asanaEvaluationQuestions.getCommonForEachParticipant()==true) {
			 questionType=QuestionTypeEnum.COMMONQUESTIONFORASANA;
			}else if(asanaEvaluationQuestions.getForEachAsana()==true  &&asanaEvaluationQuestions.getCommonForEachParticipant()==false) {
				 questionType=QuestionTypeEnum.PARTICIPANTSPECIFICASANAQUESTION;
			}else if(asanaEvaluationQuestions.getForEachAsana()==false && asanaEvaluationQuestions.getCommonForEachParticipant()==false) {
				 questionType=QuestionTypeEnum.COMMONTEAMQUESTION;
			}
		
		return questionType;
	}
	

}
