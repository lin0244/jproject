package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.TaskLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskLog entity.
 */
public interface TaskLogRepository extends JpaRepository<TaskLog,Long> {

}
