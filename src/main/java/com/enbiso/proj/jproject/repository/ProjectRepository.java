package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Project;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project, String> {

}
