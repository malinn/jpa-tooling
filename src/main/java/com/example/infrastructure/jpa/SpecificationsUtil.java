package com.example.infrastructure.jpa;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.Join;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;
import static javax.persistence.criteria.JoinType.LEFT;

public class SpecificationsUtil<U> {

	public Specification<U> startsWith(SingularAttribute<U, String> field, String param) {
		return (root, query, cb) -> cb.like(cb.lower(root.get(field)), param.toLowerCase() + "%");
	}

	public Specification<U> like(SingularAttribute<U, String> field, String param) {
		return (root, query, cb) -> cb.like(cb.lower(root.get(field)), getWildcard(param));
	}

	public <T> Specification<U> like(SingularAttribute<U, T> joinField, SingularAttribute<T, String> field, String param) {
		return (root, query, cb) -> cb.like(cb.lower(root.join(joinField, LEFT).get(field)), getWildcard(param));
	}

	public <T> Specification<U> equal(SingularAttribute<U, T> field, T param) {
		return (root, query, cb) -> cb.equal(root.get(field), param);
	}

	public <T, P> Specification<U> equal(SingularAttribute<U, T> joinField, SingularAttribute<T, P> field, P param) {
		return (root, query, cb) -> cb.equal(root.join(joinField).get(field), param);
	}

	public <T> Specification<U> notEqual(SingularAttribute<U, T> field, T param) {
		return (root, query, cb) -> cb.notEqual(root.get(field), param);
	}

	public Specification<U> isNull(SingularAttribute<U, String> field) {
		return (root, query, cb) -> cb.isNull(root.get(field));
	}

	public <T> Specification<U> isNullEntity(SingularAttribute<U, T> field) {
		return (root, query, cb) -> cb.isNull(root.get(field));
	}

	public Specification<U> isNotNull(SingularAttribute<U, String> field) {
		return (root, query, cb) -> cb.isNotNull(root.get(field));
	}

	public <T> Specification<U> isNotNullEntity(SingularAttribute<U, T> field) {
		return (root, query, cb) -> cb.isNotNull(root.get(field));
	}

	public Specification<U> afterOrEqual(SingularAttribute<U, LocalDateTime> field, LocalDateTime param) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), param);
	}

	public Specification<U> beforeOrEqual(SingularAttribute<U, LocalDateTime> field, LocalDateTime param) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), param);
	}

	public Specification<U> afterOrEqualInstant(SingularAttribute<U, Instant> field, LocalDateTime param) {
		var instant = param.atZone(systemDefault()).toInstant();
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), instant);
	}

	public Specification<U> beforeOrEqualInstant(SingularAttribute<U, Instant> field, LocalDateTime param) {
		var instant = param.atZone(systemDefault()).toInstant();
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), instant);
	}

	public Specification<U> afterOrEqualDate(SingularAttribute<U, Date> field, LocalDateTime param) {
		var instant = param.atZone(systemDefault()).toInstant();
		var date = from(instant);
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), date);
	}

	public Specification<U> beforeOrEqualDate(SingularAttribute<U, Date> field, LocalDateTime param) {
		var instant = param.atZone(systemDefault()).toInstant();
		var date = from(instant);
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), date);
	}

	public <T> Specification<U> fetchJoin(SingularAttribute<U, T> joinField) {
		return (root, query, cb) -> {
			FetchParent<U, T> fetch = root.fetch(joinField, LEFT);
			Join<U, T> join = (Join<U, T>) fetch;
			return join.getOn();
		};
	}

	public <T> Specification<U> fetchJoin(SetAttribute<U, T> joinField) {
		return (root, query, cb) -> {
			FetchParent<U, T> fetch = root.fetch(joinField, LEFT);
			Join<U, T> join = (Join<U, T>) fetch;
			return join.getOn();
		};
	}

	public <T, V> Specification<U> fetchJoinJoin(SetAttribute<U, T> joinField1, SetAttribute<T, V> joinField2) {
		return (root, query, cb) -> {
			FetchParent<U, T> fetch = root.fetch(joinField1, LEFT);
			FetchParent<T, V> fetch2 = fetch.fetch(joinField2, LEFT);
			Join<U, V> join = (Join<U, V>) fetch2;
			return join.getOn();
		};
	}

	public <V, T, X> Specification<U> fetchJoinJoinJoin(SetAttribute<U, T> joinField1, SetAttribute<T, V> joinField2, SingularAttribute<V, X> joinField3) {
		return (root, query, cb) -> {
			FetchParent<U, T> fetch = root.fetch(joinField1, LEFT);
			FetchParent<T, V> fetch2 = fetch.fetch(joinField2, LEFT);
			FetchParent<V, X> fetch3 = fetch2.fetch(joinField3, LEFT);
			Join<U, X> join = (Join<U, X>) fetch3;
			return join.getOn();
		};
	}

	public <T, V> Specification<U> fieldFromJoinedEntityOfJoinedEntityIn(ListAttribute<U, T> joinFieldFromRootEntity, SingularAttribute<T, V> joinFieldFromNestedEntity, String field, List<Long> param) {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.in(root
					.join(joinFieldFromRootEntity, LEFT)
					.join(joinFieldFromNestedEntity, LEFT)
					.get(field))
					.value(param);
		};
	}

	public <T, V> Specification<U> fieldFromJoinedEntityOfJoinedEntityIn(SetAttribute<U, T> joinFieldFromRootEntity, SingularAttribute<T, V> joinFieldFromNestedEntity, String field, List<Long> param) {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.in(root
					.join(joinFieldFromRootEntity, LEFT)
					.join(joinFieldFromNestedEntity, LEFT)
					.get(field))
					.value(param);
		};
	}

	public <T> Specification<U> fieldFromJoinedEntityIn(SingularAttribute<U, T> joinFieldFromRootEntity, String fieldFromJoinedEntity, List<Long> param) {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.in(root
					.join(joinFieldFromRootEntity, LEFT)
					.get(fieldFromJoinedEntity))
					.value(param);
		};
	}

	private String getWildcard(String param) {
		return "%" + param.toLowerCase() + "%";
	}
}
