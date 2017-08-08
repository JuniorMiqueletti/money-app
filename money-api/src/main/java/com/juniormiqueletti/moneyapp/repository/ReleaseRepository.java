package com.juniormiqueletti.moneyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juniormiqueletti.moneyapp.model.Release;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

}
