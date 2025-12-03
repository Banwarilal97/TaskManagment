
package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByUserId(Long id);

	List<Task> findByUserIdAndStatus(Long id, String status);

	List<Task> findByUserIdAndPriority(Long id, String priority);

	@Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
	List<Object[]> countTasksByStatus();

	@Query("SELECT COUNT(t) FROM Task t WHERE t.dueDate < CURRENT_DATE AND t.status <> 'COMPLETED'")
	Long countOverdueTasks();
}
