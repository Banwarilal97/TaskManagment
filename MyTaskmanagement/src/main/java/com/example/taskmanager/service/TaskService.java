
package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.entity.*;
import com.example.taskmanager.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repo;

	@Autowired
	private UserRepository userRepo;

	@Transactional
	public Task create(TaskRequest req, Long userId) {
		User u = userRepo.findById(userId).orElseThrow();

		Task t = new Task();
		t.setTitle(req.getTitle());
		t.setDescription(req.getDescription());
		t.setDueDate(req.getDueDate());
		t.setPriority(req.getPriority());
		t.setStatus(req.getStatus());
		t.setUser(u);
		return repo.save(t);

	}

	public Task getById(Long taskId, Long userId) {

		Task task = repo.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

		if (!task.getUser().getId().equals(userId)) {
			throw new RuntimeException("Unauthorized: This task does not belong to you");
		}

		return task;
	}

	public List<Task> list(Long userId, String status, String priority) {
		if (status != null)
			return repo.findByUserIdAndStatus(userId, status);
		if (priority != null)
			return repo.findByUserIdAndPriority(userId, priority);
		return repo.findByUserId(userId);
	}

	@Transactional
	public Task update(Long taskId, TaskRequest req, Long userId) {

		Task task = repo.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

		if (!task.getUser().getId().equals(userId)) {
			throw new RuntimeException("Unauthorized: This task does not belong to you.");
		}

		task.setTitle(req.getTitle());
		task.setDescription(req.getDescription());
		task.setPriority(req.getPriority());
		task.setStatus(req.getStatus());
		task.setDueDate(req.getDueDate());

		return repo.save(task);
	}

	@Transactional
	public String delete(Long taskId, Long userId) {

		Task task = repo.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

		if (!task.getUser().getId().equals(userId)) {
			throw new RuntimeException("Unauthorized: You cannot delete this task");
		}

		repo.delete(task);
		return "Task deleted successfully";
	}

	public Map<String, Object> summary() {
		Map<String, Object> m = new HashMap<>();
		m.put("statusCount", repo.countTasksByStatus());
		m.put("overdue", repo.countOverdueTasks());
		return m;
	}

	public Map<String, Object> getTaskSummary() {

		Map<String, Object> response = new HashMap<>();

		List<Object[]> statusCounts = repo.countTasksByStatus();

		Map<String, Long> statusMap = new HashMap<>();
		for (Object[] row : statusCounts) {
			statusMap.put((String) row[0], (Long) row[1]);
		}

		Long overdueCount = repo.countOverdueTasks();

		response.put("statusCount", statusMap);
		response.put("overdueTasks", overdueCount);

		return response;
	}
}
