package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.TaskType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskType entity.
 */
public interface TaskTypeRepository extends JpaRepository<TaskType,Long> {

}
