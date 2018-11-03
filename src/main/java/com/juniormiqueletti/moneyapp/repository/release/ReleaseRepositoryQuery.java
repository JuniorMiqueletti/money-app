package com.juniormiqueletti.moneyapp.repository.release;

import com.juniormiqueletti.moneyapp.controller.dto.StatisticalReleaseCategory;
import com.juniormiqueletti.moneyapp.repository.projection.ReleaseSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;

import java.time.LocalDate;
import java.util.List;

public interface ReleaseRepositoryQuery {

	public Page<Release> filter(final ReleaseFilter filter, final Pageable pageable);
	public Page<ReleaseSummary> filterSummary(final ReleaseFilter filter, final Pageable pageable);
	public List<StatisticalReleaseCategory> byCategory(final LocalDate referenceMonth);
}
