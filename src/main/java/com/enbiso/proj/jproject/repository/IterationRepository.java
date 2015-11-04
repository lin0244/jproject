package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Iteration;

import com.enbiso.proj.jproject.domain.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Iteration entity.
 */
public interface IterationRepository extends JpaRepository<Iteration,Iteration.Id> {
    @Query("SELECT i FROM Iteration i WHERE i.id.project = :project")
    List<Iteration> findAllByProject(@Param("project") Project project);
}
