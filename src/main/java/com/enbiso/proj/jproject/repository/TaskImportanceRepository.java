package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.TaskImportance;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskImportance entity.
 */
public interface TaskImportanceRepository extends JpaRepository<TaskImportance,Long> {

}
