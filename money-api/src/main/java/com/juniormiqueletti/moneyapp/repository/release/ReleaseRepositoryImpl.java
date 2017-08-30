package com.juniormiqueletti.moneyapp.repository.release;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;

public class ReleaseRepositoryImpl implements ReleaseRepositoryQuery{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Release> filter(ReleaseFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Release> criteria = builder.createQuery(Release.class);

		Root<Release> root = criteria.from(Release.class);
		
		Predicate[] predicates = createRestriction(filter,builder, root);
		criteria.where(predicates);
		
		TypedQuery<Release> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] createRestriction(ReleaseFilter filter, CriteriaBuilder builder, Root<Release> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(!StringUtils.isEmpty(filter.getDescription())) {
			predicates.add(builder.like(
					builder.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"
					));
		}
		if(filter.getDueDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo((root.get("dueDate")), filter.getDueDateFrom()));
		}
		if(filter.getDueDateUntil() != null) {
			predicates.add(builder.lessThanOrEqualTo((root.get("dueDate")), filter.getDueDateFrom()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
