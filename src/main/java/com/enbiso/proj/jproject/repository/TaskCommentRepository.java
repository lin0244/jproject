package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.TaskComment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskComment entity.
 */
public interface TaskCommentRepository extends JpaRepository<TaskComment,Long> {

}
