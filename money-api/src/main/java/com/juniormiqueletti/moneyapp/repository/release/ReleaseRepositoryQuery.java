package com.juniormiqueletti.moneyapp.repository.release;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;

public interface ReleaseRepositoryQuery {

	public Page<Release> filter(ReleaseFilter filter, Pageable pageable);
}
