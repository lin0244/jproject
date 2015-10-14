package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskStatus;
import com.enbiso.proj.jproject.repository.TaskStatusRepository;
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
 * REST controller for managing TaskStatus.
 */
@RestController
@RequestMapping("/api")
public class TaskStatusResource {

    private final Logger log = LoggerFactory.getLogger(TaskStatusResource.class);

    @Inject
    private TaskStatusRepository taskStatusRepository;

    /**
     * POST  /taskStatuss -> Create a new taskStatus.
     */
    @RequestMapping(value = "/taskStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskStatus> createTaskStatus(@Valid @RequestBody TaskStatus taskStatus) throws URISyntaxException {
        log.debug("REST request to save TaskStatus : {}", taskStatus);
        if (taskStatus.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskStatus cannot already have an ID").body(null);
        }
        TaskStatus result = taskStatusRepository.save(taskStatus);
        return ResponseEntity.created(new URI("/api/taskStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taskStatuss -> Updates an existing taskStatus.
     */
    @RequestMapping(value = "/taskStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskStatus> updateTaskStatus(@Valid @RequestBody TaskStatus taskStatus) throws URISyntaxException {
        log.debug("REST request to update TaskStatus : {}", taskStatus);
        if (taskStatus.getId() == null) {
            return createTaskStatus(taskStatus);
        }
        TaskStatus result = taskStatusRepository.save(taskStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskStatus", taskStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taskStatuss -> get all the taskStatuss.
     */
    @RequestMapping(value = "/taskStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskStatus> getAllTaskStatuss() {
        log.debug("REST request to get all TaskStatuss");
        return taskStatusRepository.findAll();
    }

    /**
     * GET  /taskStatuss/:id -> get the "id" taskStatus.
     */
    @RequestMapping(value = "/taskStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskStatus> getTaskStatus(@PathVariable Long id) {
        log.debug("REST request to get TaskStatus : {}", id);
        return Optional.ofNullable(taskStatusRepository.findOne(id))
            .map(taskStatus -> new ResponseEntity<>(
                taskStatus,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taskStatuss/:id -> delete the "id" taskStatus.
     */
    @RequestMapping(value = "/taskStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        log.debug("REST request to delete TaskStatus : {}", id);
        taskStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskStatus", id.toString())).build();
    }
}
