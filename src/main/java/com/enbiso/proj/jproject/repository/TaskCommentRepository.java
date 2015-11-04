package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Task;
import com.enbiso.proj.jproject.domain.TaskComment;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskComment entity.
 */
public interface TaskCommentRepository extends JpaRepository<TaskComment,TaskComment.Id> {

    @Query("SELECT tc FROM TaskComment tc WHERE tc.id.task = :task")
    List<TaskComment> findAllByTask(@Param("task") Task task);
}
