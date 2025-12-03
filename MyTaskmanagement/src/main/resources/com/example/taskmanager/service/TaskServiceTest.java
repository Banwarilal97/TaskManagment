package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskServiceTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // ------------------ CREATE TASK ------------------
    @Transactional
    public Task create(TaskRequest req, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setDueDate(req.getDueDate());
        task.setPriority(req.getPriority());
        task.setStatus(req.getStatus());
        task.setUser(user);

        return taskRepository.save(task);
    }

    // ------------------ GET ALL TASKS ------------------
    public List<Task> list(Long userId, String status, String priority) {

        if (status != null)
            return taskRepository.findByUserIdAndStatus(userId, status);

        if (priority != null)
            return taskRepository.findByUserIdAndPriority(userId, priority);

        return taskRepository.findByUserId(userId);
    }

    // ------------------ GET TASK BY ID (For Test Cases) ------------------
    public Task getById(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This task does not belong to you");
        }

        return task;
    }

    // ------------------ UPDATE TASK ------------------
    @Transactional
    public Task update(Long taskId, TaskRequest req, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This task does not belong to you");
        }

        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setPriority(req.getPriority());
        task.setStatus(req.getStatus());
        task.setDueDate(req.getDueDate());

        return taskRepository.save(task);
    }

    // ------------------ DELETE TASK ------------------
    @Transactional
    public String delete(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You cannot delete this task");
        }

        taskRepository.delete(task);
        return "Task deleted successfully";
    }

    // ------------------ SUMMARY ------------------
    public Map<String, Object> getTaskSummary() {

        Map<String, Object> response = new HashMap<>();

        List<Object[]> statusCounts = taskRepository.countTasksByStatus();

        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : statusCounts) {
            statusMap.put((String) row[0], (Long) row[1]);
        }

        Long overdueCount = taskRepository.countOverdueTasks();

        response.put("statusCount", statusMap);
        response.put("overdueTasks", overdueCount);

        return response;
    }
}
