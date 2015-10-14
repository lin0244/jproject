package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Iteration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Iteration entity.
 */
public interface IterationRepository extends JpaRepository<Iteration,Long> {

}
