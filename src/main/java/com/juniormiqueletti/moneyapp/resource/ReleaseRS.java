package com.juniormiqueletti.moneyapp.resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.juniormiqueletti.moneyapp.dto.StatisticalReleaseCategory;
import com.juniormiqueletti.moneyapp.dto.StatisticalReleaseDaily;
import com.juniormiqueletti.moneyapp.repository.projection.ReleaseSummary;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.juniormiqueletti.moneyapp.event.ResourceCreatedEvent;
import com.juniormiqueletti.moneyapp.exceptionhandler.MoneyAppExceptionHandler.Error;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.ReleaseRepository;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;
import com.juniormiqueletti.moneyapp.service.ReleaseService;
import com.juniormiqueletti.moneyapp.service.exception.PersonInexistsOrInactiveException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/release")
public class ReleaseRS {

	private ReleaseRepository repository;
	private ReleaseService service;
	private ApplicationEventPublisher publisher;
	private MessageSource ms;

    @Autowired
    public ReleaseRS(
        ReleaseRepository repository,
        ReleaseService service,
        ApplicationEventPublisher publisher,
        MessageSource ms
    ) {
        this.repository = repository;
        this.service = service;
        this.publisher = publisher;
        this.ms = ms;
    }

    @GetMapping
	@PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public Page<Release> findAll(final ReleaseFilter filter, final Pageable pageable) {

		return repository.filter(filter, pageable);
	}

	@GetMapping(params = "summary")
	@PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public Page<ReleaseSummary> findSummary(final ReleaseFilter filter, final Pageable pageable) {

		return repository.filterSummary(filter, pageable);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public ResponseEntity<Release> findById(@PathVariable final Long id) {

		Optional<Release> release = repository.findById(id);

		if (!release.isPresent()) {
			return ResponseEntity.notFound().build();
		}
        return ResponseEntity.ok(release.get());
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ROLE_CREATE_RELEASE')")
	public ResponseEntity<Release> create(@Valid @RequestBody final Release release, final HttpServletResponse response) {

		service.save(release);

		Release created = repository.save(release);

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
	public ResponseEntity<Object> delete(@PathVariable final Long id) {

		Optional<Release> release = repository.findById(id);

		if (!release.isPresent()) {
			return ResponseEntity.notFound().build();
		}

        service.delete(id);
        return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CREATE_RELEASE')")
	public ResponseEntity<Release> update(@PathVariable final Long id, @Valid @RequestBody final Release release) {
		try {
			Release releaseSaved = service.update(id, release);
			return ResponseEntity.ok(releaseSaved);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/statistical/by-category/{year}-{month}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
	public List<StatisticalReleaseCategory> byCategory(@PathVariable final int year, @PathVariable final int month) {
        LocalDate referenceDate = LocalDate.of(year, month, 1);
        return this.repository.byCategory(referenceDate);
    }

    @GetMapping("/statistical/by-day/{year}-{month}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_RELEASE')")
    public List<StatisticalReleaseDaily> byDay(@PathVariable final int year, @PathVariable final int month) {
        LocalDate referenceDate = LocalDate.of(year, month, 1);
        return this.repository.byDay(referenceDate);
    }

    @GetMapping("/reports/by-person")
    public ResponseEntity<byte[]> reportByPerson(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate start,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate end) throws JRException {

        byte[] report = service.reportByPerson(start, end);

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_PDF_VALUE
            )
            .body(report);
    }

    @PostMapping("/attached")
    @PreAuthorize("hasAuthority('ROLE_CREATE_RELEASE')")
    public String uploadFile(@RequestParam MultipartFile file) throws IOException {
        OutputStream outputStream = new FileOutputStream("/attached--" + file.getOriginalFilename());
        outputStream.write(file.getBytes());
        outputStream.close();
        return "Ok";
    }
}
