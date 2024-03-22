package com.exam.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;
import com.exam.service.QuestionService;
import com.exam.service.QuizService;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuizService quizService;
	
	//add quiz
		@PostMapping("/")
		public ResponseEntity<Question>  add(@RequestBody Question question){
			return ResponseEntity.ok(this.questionService.addQuestion(question));
			
		}
		
		//update quiz
		
		@PutMapping("/")
		public ResponseEntity<Question>  update(@RequestBody Question question){
			return ResponseEntity.ok(this.questionService.updateQuestion(question));
		}
		
		// get all question of any quiz
		
		@GetMapping("/quiz/{qid}")
		public ResponseEntity<?> getqQestionsOfQuiz(@PathVariable("qid") Long qid){
//			Quiz quiz =new Quiz();
//			quiz.setQid(qid);
//			Set<Question> questionsOfQuiz = this.questionService.getQuestionsQuiz(quiz);
//			return ResponseEntity.ok(questionsOfQuiz);
			
			Quiz quiz = this.quizService.getQuiz(qid);
			Set<Question> questions = quiz.getQuestions();
			List list =new ArrayList(questions);
			if(list.size() > Integer.parseInt(quiz.getNumberOfQuestion()))
			{
				list=list.subList(0, Integer.parseInt(quiz.getNumberOfQuestion()+1));
			}
			Collections.shuffle(list);
			return ResponseEntity.ok(list);
			
		}
		//get all quiz for admin
		@GetMapping("/quiz/all/{qid}")
		public ResponseEntity<?> getqQestionsOfQuizAdmin(@PathVariable("qid") Long qid){
			Quiz quiz =new Quiz();
			quiz.setQid(qid);
			Set<Question> questionsOfQuiz = this.questionService.getQuestionsQuiz(quiz);
			return ResponseEntity.ok(questionsOfQuiz);
		}
	
//		//get single question	
		@GetMapping("/{quesId}")
		public Question getQuestion(@PathVariable("quesId") Long quesId){
			return this.questionService.getQuestion(quesId);
			
		}
		
		//delete quiz
		@DeleteMapping("/{quesId}")
		public void deleteQuiz(@PathVariable("quesId") Long quesId){
			this.questionService.deleteQuestion(quesId);
			
		}
		
		//eval quiz
		
		@PostMapping("/eval-quiz")
		public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions){
			 Double marksGot= 0.0;
			 Integer correctAnswer=0;
			 Integer attempted=0;
			for(Question q:questions) {
				Question question =this.questionService.get(q.getQuesid());
				if(question.getAnswer().equals(q.getGivenAnswer())) {
					//correct
					correctAnswer++;
					double marksSingle= Double.parseDouble(questions.get(0).getQuiz().getMaxMarks())/questions.size();
					marksGot+=marksSingle;
				}
				if(q.getGivenAnswer()!=null) {
					attempted++;
				}
			}
			Map<String, Object> map=Map.of("marksGot",marksGot,"correctAnswer",correctAnswer,"attempted",attempted);
			
			return ResponseEntity.ok(map);
			
		}
		

}
