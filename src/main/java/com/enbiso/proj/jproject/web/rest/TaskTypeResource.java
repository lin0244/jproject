package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskType;
import com.enbiso.proj.jproject.repository.TaskTypeRepository;
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
 * REST controller for managing TaskType.
 */
@RestController
@RequestMapping("/api")
public class TaskTypeResource {

    private final Logger log = LoggerFactory.getLogger(TaskTypeResource.class);

    @Inject
    private TaskTypeRepository taskTypeRepository;

    /**
     * POST  /taskTypes -> Create a new taskType.
     */
    @RequestMapping(value = "/taskTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskType> createTaskType(@Valid @RequestBody TaskType taskType) throws URISyntaxException {
        log.debug("REST request to save TaskType : {}", taskType);
        if (taskType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskType cannot already have an ID").body(null);
        }
        TaskType result = taskTypeRepository.save(taskType);
        return ResponseEntity.created(new URI("/api/taskTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taskTypes -> Updates an existing taskType.
     */
    @RequestMapping(value = "/taskTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskType> updateTaskType(@Valid @RequestBody TaskType taskType) throws URISyntaxException {
        log.debug("REST request to update TaskType : {}", taskType);
        if (taskType.getId() == null) {
            return createTaskType(taskType);
        }
        TaskType result = taskTypeRepository.save(taskType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskType", taskType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taskTypes -> get all the taskTypes.
     */
    @RequestMapping(value = "/taskTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskType> getAllTaskTypes() {
        log.debug("REST request to get all TaskTypes");
        return taskTypeRepository.findAll();
    }

    /**
     * GET  /taskTypes/:id -> get the "id" taskType.
     */
    @RequestMapping(value = "/taskTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskType> getTaskType(@PathVariable String id) {
        log.debug("REST request to get TaskType : {}", id);
        return Optional.ofNullable(taskTypeRepository.findOne(id))
            .map(taskType -> new ResponseEntity<>(
                taskType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taskTypes/:id -> delete the "id" taskType.
     */
    @RequestMapping(value = "/taskTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskType(@PathVariable String id) {
        log.debug("REST request to delete TaskType : {}", id);
        taskTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskType", id.toString())).build();
    }
}
