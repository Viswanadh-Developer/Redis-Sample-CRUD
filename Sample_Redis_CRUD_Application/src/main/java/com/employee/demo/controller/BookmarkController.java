package com.employee.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.demo.entity.Employee;
import com.employee.demo.repo.EmployeeRepository;

@RestController
@RequestMapping("/api")
public class BookmarkController {
	
	public final Logger LOG = LoggerFactory.getLogger(getClass());
	@Autowired
	EmployeeRepository repo;
	
	@GetMapping("/bookmarkers")
	public List<Employee> getAllBookmarkers() {
		LOG.info("Get All BookMarkers :: ");
		return repo.findAll();
	}

	@PostMapping("/bookmarker")
	public Employee createBookmarker(@Valid @RequestBody Employee emp) {
		LOG.info("Create A BookMarker :: ");
		return repo.save(emp);
	}

	@Cacheable(value = "bookmarker", key = "#bookmarkerId")
	@GetMapping("/bookmarker/{id}")
	public Employee getBookmarkerById(@PathVariable(value = "id") String bookmarkerId) {

		LOG.info("Getting Bookmarker with ID {}.", bookmarkerId);

		return repo.findById(Integer.valueOf(bookmarkerId)).get();
	}

	@CachePut(value = "bookmarker", key = "#bookmarkerId")
	@PutMapping("/bookmarkers/{id}")
	public Employee updateBookmarker(@PathVariable(value = "id") String bookmarkerId,
			@Valid @RequestBody Employee bookmarkerDetails) {
		LOG.info("Updating Bookmarker with ID {}.", bookmarkerId);
		Employee bookmarker = repo.findById(Integer.valueOf(bookmarkerId)).get();

		bookmarker.setTitle(bookmarkerDetails.getTitle());
		bookmarker.setContent(bookmarkerDetails.getContent());

		Employee updatedBookmarker = repo.save(bookmarker);
		return updatedBookmarker;
	}

	@CacheEvict(value = "bookmarker", key = "#bookmarkerId")
	@DeleteMapping("/bookmarkers/{id}")
	public String deleteBookmarker(@PathVariable(value = "id") String bookmarkerId) {
		LOG.info("deleting Bookmarker with ID {}.", bookmarkerId);
		Employee bookmarker = repo.findById(Integer.valueOf(bookmarkerId)).get()	;
		LOG.info("delete Bookmarker with ID {}.", bookmarkerId);
		repo.delete(bookmarker);
		return "bookmarker has deleted";
	}
}
