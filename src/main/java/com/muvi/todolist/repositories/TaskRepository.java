package com.muvi.todolist.repositories;

import com.muvi.todolist.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
