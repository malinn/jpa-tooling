package com.example.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@Getter
@Builder
class PharmacyFilter {

	private final SpecificationsUtil<Pharmacy> specificationsUtil = new SpecificationsUtil<>();

	private String name;
	private String registrationNumber;
	private LocalDateTime dateOriginFrom;

	Specification<Pharmacy> toFullFilterSpecifications() {
		return new Select<Pharmacy>()
				.where(nameLike(name))
				.and(registrationNumberLike(registrationNumber))
				.and(dateOriginAfter(dateOriginFrom))
				.toSpecification();
	}

	private Specification<Pharmacy> nameLike(String param) {
		return specificationsUtil.like(Pharmacy_.nazwa, param) : null;
	}

	private Specification<Pharmacy> dateOriginAfter(LocalDateTime dateOriginFrom) {
		return null;
	}

	private Specification<Pharmacy> registrationNumberLike(String registrationNumber) {
		specificationsUtil.fieldFromJoinedEntityOfJoinedEntityIn();
	}

}
