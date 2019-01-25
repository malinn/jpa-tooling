package com.example.domain;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequestMapping("/api/pharmacies")
@RestController
public class PharmacyController {

	 PharmacyService pharmacyService;

	public PharmacyController(PharmacyService pharmacyService) {
		this.pharmacyService = pharmacyService;
	}

	@GetMapping("/search")
	List<CompanyDTO> search(@Valid @RequestBody PharmacyFilter pharmacyFilter) {
		return pharmacyService.search666(pharmacyFilter);
	}




}