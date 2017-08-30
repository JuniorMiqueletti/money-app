package com.juniormiqueletti.moneyapp.repository.release;

import java.util.List;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;

public interface ReleaseRepositoryQuery {

	public List<Release> filter(ReleaseFilter filter);
}
