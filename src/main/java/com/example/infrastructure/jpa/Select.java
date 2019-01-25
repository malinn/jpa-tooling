package com.example.infrastructure.jpa;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Wrapper for Specifications class (syntactic sugar)
 * Example usage:
 * 		Select<PatientBriefEntity> select = new Select<PatientBriefEntity>()
 * 			.where(lastNameStartsWith(query))
 * 			.or(peselStartsWith(query));
 * <p>
 * lastNameStartsWith(query) and peselStartsWith(query) methods are implemented by hand for now,
 * but in future all will be automatically generated
 */
public class Select<T> {

	private Specifications<T> specifications;

	public Specification<T> toSpecification() {
		return specifications;
	}

	public Select<T> where(Specification<T> specification) {
		specifications = Specifications.where(specification);
		return this;
	}

	public Select<T> and(Specification<T> specification) {
		if (specifications == null) {
			throw new IllegalStateException("No specifications provided. You should run `where()` method first...");
		}
		specifications = specifications.and(specification);
		return this;
	}

	public Select<T> or(Specification<T> specification) {
		if (specifications == null) {
			throw new IllegalStateException("No specifications provided. You should run `where()` method first...");
		}
		specifications = specifications.or(specification);
		return this;
	}
}
