package com.exam.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.model.exam.Category;
import com.exam.model.exam.Quiz;

public interface QuizRepository extends JpaRepository<Quiz,Long> {

	public List<Quiz> findBycategory(Category category);
	
	
	//Created custom method (syntax: findBy{then name}())
	
	// this is for user only to display active quizzes
	
	//return true value if quiz is active
	public List<Quiz> findByActive(Boolean b);
	
	//return active quiz by category
	public List<Quiz> findByCategoryAndActive(Category c,Boolean b);

}
