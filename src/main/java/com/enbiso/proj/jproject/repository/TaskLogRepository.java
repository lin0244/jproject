package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Task;
import com.enbiso.proj.jproject.domain.TaskLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskLog entity.
 */
public interface TaskLogRepository extends JpaRepository<TaskLog,TaskLog.Id> {

    @Query("SELECT tl FROM TaskLog tl WHERE tl.id.task = :task")
    List<TaskLog> findAllByTask(Task task);
}
