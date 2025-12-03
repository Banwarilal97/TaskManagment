
package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired

	private TaskService service;

	@PostMapping
	public Object create(@RequestBody TaskRequest req, Authentication auth) {
		User u = (User) auth.getPrincipal();
		return service.create(req, u.getId());
	}

	@GetMapping("/{id}")
	public Task getById(@PathVariable Long id, Authentication auth) {

		User user = (User) auth.getPrincipal();
		return service.getById(id, user.getId());
	}

	@GetMapping
	public List<?> list(@RequestParam(required = false) String status, @RequestParam(required = false) String priority,
			Authentication auth) {

		User u = (User) auth.getPrincipal();
		return service.list(u.getId(), status, priority);
	}

	@PutMapping("/{id}")
	public Task update(@PathVariable Long id, @RequestBody TaskRequest req, Authentication auth) {

		User user = (User) auth.getPrincipal();
		return service.update(id, req, user.getId());
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, Authentication auth) {
		User user = (User) auth.getPrincipal();
		return service.delete(id, user.getId());
	}

	@GetMapping("/summary")
	public Map<String, Object> summary(Authentication auth) {
		return service.getTaskSummary();
	}

}
