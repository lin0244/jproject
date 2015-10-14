package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskImportance;
import com.enbiso.proj.jproject.repository.TaskImportanceRepository;
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
 * REST controller for managing TaskImportance.
 */
@RestController
@RequestMapping("/api")
public class TaskImportanceResource {

    private final Logger log = LoggerFactory.getLogger(TaskImportanceResource.class);

    @Inject
    private TaskImportanceRepository taskImportanceRepository;

    /**
     * POST  /taskImportances -> Create a new taskImportance.
     */
    @RequestMapping(value = "/taskImportances",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskImportance> createTaskImportance(@Valid @RequestBody TaskImportance taskImportance) throws URISyntaxException {
        log.debug("REST request to save TaskImportance : {}", taskImportance);
        if (taskImportance.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskImportance cannot already have an ID").body(null);
        }
        TaskImportance result = taskImportanceRepository.save(taskImportance);
        return ResponseEntity.created(new URI("/api/taskImportances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskImportance", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taskImportances -> Updates an existing taskImportance.
     */
    @RequestMapping(value = "/taskImportances",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskImportance> updateTaskImportance(@Valid @RequestBody TaskImportance taskImportance) throws URISyntaxException {
        log.debug("REST request to update TaskImportance : {}", taskImportance);
        if (taskImportance.getId() == null) {
            return createTaskImportance(taskImportance);
        }
        TaskImportance result = taskImportanceRepository.save(taskImportance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskImportance", taskImportance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taskImportances -> get all the taskImportances.
     */
    @RequestMapping(value = "/taskImportances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskImportance> getAllTaskImportances() {
        log.debug("REST request to get all TaskImportances");
        return taskImportanceRepository.findAll();
    }

    /**
     * GET  /taskImportances/:id -> get the "id" taskImportance.
     */
    @RequestMapping(value = "/taskImportances/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskImportance> getTaskImportance(@PathVariable Long id) {
        log.debug("REST request to get TaskImportance : {}", id);
        return Optional.ofNullable(taskImportanceRepository.findOne(id))
            .map(taskImportance -> new ResponseEntity<>(
                taskImportance,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taskImportances/:id -> delete the "id" taskImportance.
     */
    @RequestMapping(value = "/taskImportances/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskImportance(@PathVariable Long id) {
        log.debug("REST request to delete TaskImportance : {}", id);
        taskImportanceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskImportance", id.toString())).build();
    }
}
