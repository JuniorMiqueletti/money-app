package com.juniormiqueletti.moneyapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juniormiqueletti.moneyapp.model.Category;
import com.juniormiqueletti.moneyapp.repository.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryRepository repo;
	
	@GetMapping
	public List<Category> listAll(){
		return repo.findAll();
	}
}
