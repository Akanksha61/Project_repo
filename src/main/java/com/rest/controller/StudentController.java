package com.rest.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rest.dao.StudentRepo;
import com.rest.exception.StudentNotFoundException;
import com.rest.model.Student;

@RestController
public class StudentController {
	
	@Autowired
	StudentRepo stuRepo;
	
	@GetMapping(path = "/get")
	public List<Student> getAll()
	{
		List<Student> list=stuRepo.findAll();
		return list;
	}
	@GetMapping(path = "/get/{id}")
	public Student getByID(@PathVariable long id)
	{
		Optional<Student> student=stuRepo.findById(id);
		if(!student.isPresent())
			throw  new StudentNotFoundException("id-" +id);
		return student.get();
		
	}
	
	@PostMapping(path = "/post")
	public ResponseEntity<Object> createPost(@RequestBody Student student)
	{
		Student student2=stuRepo.save(student);
		URI uri=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(student2.getId()).toUri();
				return ResponseEntity.created(uri).build();
	}

	@PutMapping(path = "put/{id}")
	public ResponseEntity<Object> update(@RequestBody Student student,@PathVariable long id)
	{
		Optional<Student> optionalstudent=stuRepo.findById(id);
		
		if(!optionalstudent.isPresent())
			return ResponseEntity.notFound().build();
		student.setId(id);
		stuRepo.save(student);
		return ResponseEntity.noContent().build();
		}
	@DeleteMapping(path = "del/{id}")
	public void delete(@PathVariable long id)
	{
		stuRepo.deleteById(id);
	}
}
