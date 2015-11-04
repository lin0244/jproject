package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.TaskStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskStatus entity.
 */
public interface TaskStatusRepository extends JpaRepository<TaskStatus,String> {

}
