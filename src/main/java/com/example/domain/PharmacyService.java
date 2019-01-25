package com.example.domain;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PharmacyService {

	private PharmacyRepository pharmacyRepository;

	List<Pharmacy> search666(PharmacyFilter pharmacyFilter) {
		return pharmacyRepository.findAll(pharmacyFilter.toFullFilterSpecifications());
	}
}

