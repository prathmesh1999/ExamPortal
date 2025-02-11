package com.exam.model.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long quesid;

	@Column(length=5000)
	private String content;

	private String image;

	private String option1;
	private String option2;
	private String option3;
	private String option4;


	private String answer;
	
	//@Transient means it is not added in database
	@Transient
	private String givenAnswer;

	@ManyToOne(fetch = FetchType.EAGER)
	private Quiz quiz;

	
	public String getGivenAnswer() {
		return givenAnswer;
	}

	public void setGivenAnswer(String givenAnswer) {
		this.givenAnswer = givenAnswer;
	}

	//@jsonIgnore means at the of get answer it will not be display to the user
	//@JsonIgnore
	public String getAnswer() {
		return answer;
	}
	//@jsonProperty means at the time set answer .. answer is save in database
	//@JsonProperty("answer")
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Long getQuesid() {
		return quesid;
	}

	public void setQuesid(Long quesid) {
		this.quesid = quesid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Question(Long quesid, String content, String image, String option1, String option2, String option3,
			String option4, String answer, Quiz quiz) {
		super();
		this.quesid = quesid;
		this.content = content;
		this.image = image;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.answer = answer;
		this.quiz = quiz;
	}

	
	
}
