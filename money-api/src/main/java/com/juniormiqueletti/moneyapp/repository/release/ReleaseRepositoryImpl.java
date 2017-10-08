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

import com.juniormiqueletti.moneyapp.repository.projection.ReleaseSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.repository.filter.ReleaseFilter;

public class ReleaseRepositoryImpl implements ReleaseRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Release> filter(ReleaseFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Release> criteria = builder.createQuery(Release.class);

		Root<Release> root = criteria.from(Release.class);

		Predicate[] predicates = createRestriction(filter, builder, root);
		criteria.where(predicates);

		TypedQuery<Release> query = manager.createQuery(criteria);

		addPageableRestrictions(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, getTotalFrom(filter));
	}

	@Override
	public Page<ReleaseSummary> filterSummary(ReleaseFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ReleaseSummary> criteria = builder.createQuery(ReleaseSummary.class);

		Root<Release> root = criteria.from(Release.class);

		criteria.select(builder.construct(ReleaseSummary.class,
				root.get("id"),
				root.get("description"),
				root.get("dueDate"),
				root.get("payDate"),
				root.get("value"),
				root.get("type"),
				root.get("category").get("name"),
				root.get("person").get("name")));

		Predicate[] predicates = createRestriction(filter, builder, root);
		criteria.where(predicates);

		TypedQuery<ReleaseSummary> query = manager.createQuery(criteria);

		addPageableRestrictions(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, getTotalFrom(filter));
	}

	private Predicate[] createRestriction(ReleaseFilter filter, CriteriaBuilder builder, Root<Release> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescription())) {
			predicates.add(builder.like(builder.lower(root.get("description")),
					"%" + filter.getDescription().toLowerCase() + "%"));
		}
		if (filter.getDueDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo((root.get("dueDate")), filter.getDueDateFrom()));
		}
		if (filter.getDueDateUntil() != null) {
			predicates.add(builder.lessThanOrEqualTo((root.get("dueDate")), filter.getDueDateFrom()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void addPageableRestrictions(TypedQuery<?> query, Pageable pageable) {
		int actualPage = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int firstRegistry = actualPage * pageSize;

		query.setFirstResult(firstRegistry);
		query.setMaxResults(pageSize);
	}

	private Long getTotalFrom(ReleaseFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		Root<Release> root = criteria.from(Release.class);
		Predicate[] predicates = createRestriction(filter, builder, root);

		criteria.where(predicates);
		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}


}
