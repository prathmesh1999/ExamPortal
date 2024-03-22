package com.exam.service;

import java.util.Set;

import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;

public interface QuestionService {
	
	public Question addQuestion(Question question);
	
	public Question updateQuestion(Question question);
	
	public Set<Question> getQuestions(Question question);
	
	public Question getQuestion(Long questionid);
	
	public void deleteQuestion(Long questionid);
	
	public Set<Question> getQuestionsQuiz(Quiz quiz);
	
	public Question get(Long questionId);






}
