package com.juniormiqueletti.moneyapp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.juniormiqueletti.moneyapp.repository.projection.ReleaseSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.juniormiqueletti.moneyapp.event.ResourceCreatedEvent;
import com.juniormiqueletti.moneyapp.exceptionhandler.MoneyAppExceptionHandler.Error;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;
import com.juniormiqueletti.moneyapp.service.ReleaseService;
import com.juniormiqueletti.moneyapp.service.exception.PersonInexistsOrInactiveException;

@RestController
@RequestMapping("/release")
public class ReleaseController {

	private ReleaseRepository repo;
	private ReleaseService service;
	private ApplicationEventPublisher publisher;
	private MessageSource ms;

    @Autowired
    public ReleaseController(
        ReleaseRepository repo,
        ReleaseService service,
        ApplicationEventPublisher publisher,
        MessageSource ms
    ) {
        this.repo = repo;
        this.service = service;
        this.publisher = publisher;
        this.ms = ms;
    }

    @GetMapping
	@PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public Page<Release> findAll(ReleaseFilter filter, Pageable pageable) {

		return repo.filter(filter, pageable);
	}

	@GetMapping(params = "summary")
	@PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public Page<ReleaseSummary> findSummary(ReleaseFilter filter, Pageable pageable) {

		return repo.filterSummary(filter, pageable);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public ResponseEntity<Release> findById(@PathVariable Long id) {

		Optional<Release> release = repo.findById(id);

		if (!release.isPresent()) {
			return ResponseEntity.notFound().build();
		}
        return ResponseEntity.ok(release.get());
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ROLE_CREATE_RELEASE')")
	public ResponseEntity<Release> create(@Valid @RequestBody Release release, HttpServletResponse response) {

		service.save(release);

		Release created = repo.save(release);

		publisher.publishEvent(new ResourceCreatedEvent(this, response, created.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@ExceptionHandler({ PersonInexistsOrInactiveException.class })
	public ResponseEntity<Object> handlePersonInexistsOrInactiveException(PersonInexistsOrInactiveException ex) {
		String userMessage = ms.getMessage("message.inexistsOrInactive", null, Locale.US);
		String developerMessage = ex.toString();

		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));

		return ResponseEntity.badRequest().body(errors);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_DELETE_RELEASE')")
	public ResponseEntity<Object> delete(@PathVariable Long id) {

		Optional<Release> release = repo.findById(id);

		if (!release.isPresent()) {
			return ResponseEntity.notFound().build();
		}

        service.delete(id);
        return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CREATE_RELEASE')")
	public ResponseEntity<Release> update(@PathVariable Long id, @Valid @RequestBody Release release) {
		try {
			Release releaseSaved = service.update(id, release);
			return ResponseEntity.ok(releaseSaved);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
