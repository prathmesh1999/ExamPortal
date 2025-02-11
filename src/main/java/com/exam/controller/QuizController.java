package com.exam.controller;

import java.util.List;

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

import com.exam.model.exam.Category;
import com.exam.model.exam.Quiz;
import com.exam.service.QuizService;

@RestController
@CrossOrigin("*")
@RequestMapping("/quiz")
public class QuizController {
	
	@Autowired
	private QuizService quizService;
	
	//add quiz
	@PostMapping("/")
	public ResponseEntity<Quiz>  add(@RequestBody Quiz quiz){
		return ResponseEntity.ok(this.quizService.addQuiz(quiz));
		
	}
	
	//update quiz
	
	@PutMapping("/")
	public ResponseEntity<Quiz>  update(@RequestBody Quiz quiz){
		return ResponseEntity.ok(this.quizService.updateQuiz(quiz));
	}
	
	// get quiz
	
	@GetMapping("/")
	public ResponseEntity<?> quizzes(){
		return ResponseEntity.ok(this.quizService.getQuizzes());
		
	}
	
	//get single quiz
	
	@GetMapping("/{qid}")
	public Quiz getQuiz(@PathVariable("qid") Long qid){
		return this.quizService.getQuiz(qid);
		
	}
	
	//delete quiz
	@DeleteMapping("/{qid}")
	public void deleteQuiz(@PathVariable("qid") Long qid){
		this.quizService.deleteQuiz(qid);
		
	}
	
	//Get quiz by category
	@GetMapping("/category/{cid}")
	public List<Quiz> getQuizzessOfCategory(@PathVariable("cid") Long cid) {
		Category category= new Category();
		category.setCid(cid);
		return this.quizService.getQuizzessOfCategory(category);
	}
	
	//get active quizzes
	
	@GetMapping("/active")
	public List<Quiz> getActiveQuizzes(){
		return this.quizService.getActiveQuizzes();
	}
	
	//get active quizzes of category
	
	@GetMapping("/category/active/{cid}")
	public List<Quiz> getActiveQuizOfCategory(@PathVariable("cid") Long cid){
		Category category=new Category();
		category.setCid(cid);
		return this.quizService.getActiveQuizzesOfCategory(category);
	}
	

}
