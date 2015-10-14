package com.enbiso.proj.jproject.repository;

import com.enbiso.proj.jproject.domain.Person;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Person entity.
 */
public interface PersonRepository extends JpaRepository<Person,Long> {

}
