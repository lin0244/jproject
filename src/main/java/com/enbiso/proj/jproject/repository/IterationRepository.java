package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Iteration;

import com.enbiso.proj.jproject.domain.Project;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Iteration entity.
 */
public interface IterationRepository extends JpaRepository<Iteration,Long> {
    List<Iteration> findAllByProject(Project project);
}
