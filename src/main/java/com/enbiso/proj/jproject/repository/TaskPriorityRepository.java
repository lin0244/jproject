package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.TaskPriority;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskPriority entity.
 */
public interface TaskPriorityRepository extends JpaRepository<TaskPriority,Integer> {

}
