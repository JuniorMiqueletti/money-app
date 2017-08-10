package com.juniormiqueletti.moneyapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.juniormiqueletti.moneyapp.event.ResourceCreatedEvent;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;

@RestController
@RequestMapping("/release")
public class ReleaseController {

	@Autowired
	private ReleaseRepository repo;
	
	@Autowired
	private ApplicationEventPublisher publisher;

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
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<Release> create(@Valid @RequestBody Release release, HttpServletResponse response) {

		Release created = repo.save(release);

		publisher.publishEvent(new ResourceCreatedEvent(this, response, created.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
}
