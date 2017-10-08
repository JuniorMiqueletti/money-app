package com.juniormiqueletti.moneyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juniormiqueletti.moneyapp.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
