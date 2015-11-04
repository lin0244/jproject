package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskPriority;
import com.enbiso.proj.jproject.repository.TaskPriorityRepository;
import com.enbiso.proj.jproject.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TaskPriority.
 */
@RestController
@RequestMapping("/api")
public class TaskPriorityResource {

    private final Logger log = LoggerFactory.getLogger(TaskPriorityResource.class);

    @Inject
    private TaskPriorityRepository taskPriorityRepository;

    /**
     * POST  /taskPrioritys -> Create a new taskPriority.
     */
    @RequestMapping(value = "/taskPrioritys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskPriority> createTaskPriority(@Valid @RequestBody TaskPriority taskPriority) throws URISyntaxException {
        log.debug("REST request to save TaskPriority : {}", taskPriority);
        if (taskPriority.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskPriority cannot already have an ID").body(null);
        }
        TaskPriority result = taskPriorityRepository.save(taskPriority);
        return ResponseEntity.created(new URI("/api/taskPrioritys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskPriority", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taskPrioritys -> Updates an existing taskPriority.
     */
    @RequestMapping(value = "/taskPrioritys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskPriority> updateTaskPriority(@Valid @RequestBody TaskPriority taskPriority) throws URISyntaxException {
        log.debug("REST request to update TaskPriority : {}", taskPriority);
        if (taskPriority.getId() == null) {
            return createTaskPriority(taskPriority);
        }
        TaskPriority result = taskPriorityRepository.save(taskPriority);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskPriority", taskPriority.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taskPrioritys -> get all the taskPrioritys.
     */
    @RequestMapping(value = "/taskPrioritys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskPriority> getAllTaskPrioritys() {
        log.debug("REST request to get all TaskPrioritys");
        return taskPriorityRepository.findAll();
    }

    /**
     * GET  /taskPrioritys/:id -> get the "id" taskPriority.
     */
    @RequestMapping(value = "/taskPrioritys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskPriority> getTaskPriority(@PathVariable Integer id) {
        log.debug("REST request to get TaskPriority : {}", id);
        return Optional.ofNullable(taskPriorityRepository.findOne(id))
            .map(taskPriority -> new ResponseEntity<>(
                taskPriority,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taskPrioritys/:id -> delete the "id" taskPriority.
     */
    @RequestMapping(value = "/taskPrioritys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskPriority(@PathVariable Integer id) {
        log.debug("REST request to delete TaskPriority : {}", id);
        taskPriorityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskPriority", id.toString())).build();
    }
}
