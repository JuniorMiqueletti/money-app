package com.juniormiqueletti.moneyapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;

@RestController
@RequestMapping("/release")
public class ReleaseController {

	@Autowired
	private ReleaseRepository repo;

	@GetMapping
	public List<Release> listAll() {

		List<Release> release = repo.findAll();
		return release;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Release> findById(@PathVariable Long id) {

		Release release = repo.findOne(id);

		if (release == null)
			return ResponseEntity.notFound().build();
		else
			return ResponseEntity.ok(release);
	}
}
