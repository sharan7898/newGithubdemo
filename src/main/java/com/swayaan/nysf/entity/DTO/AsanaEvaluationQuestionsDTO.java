package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.QuestionTypeEnum;

public class AsanaEvaluationQuestionsDTO {

	private Integer id;

	private QuestionTypeEnum questionType;

	private AsanaEvaluationQuestions asanaEvaluationQuestions;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public QuestionTypeEnum getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionTypeEnum questionType) {
		this.questionType = questionType;
	}

	public AsanaEvaluationQuestions getAsanaEvaluationQuestions() {
		return asanaEvaluationQuestions;
	}

	public void setAsanaEvaluationQuestions(AsanaEvaluationQuestions asanaEvaluationQuestions) {
		this.asanaEvaluationQuestions = asanaEvaluationQuestions;
	}

	public AsanaEvaluationQuestionsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AsanaEvaluationQuestionsDTO(QuestionTypeEnum questionType,
			AsanaEvaluationQuestions asanaEvaluationQuestions) {
		super();
		this.questionType = questionType;
		this.asanaEvaluationQuestions = asanaEvaluationQuestions;
	}

	@Override
	public String toString() {
		return "AsanaEvaluationQuestionsDTO [id=" + id + ", questionType=" + questionType
				+ ", asanaEvaluationQuestions=" + asanaEvaluationQuestions + "]";
	}

}
