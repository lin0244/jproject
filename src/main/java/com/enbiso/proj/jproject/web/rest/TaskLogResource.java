package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskLog;
import com.enbiso.proj.jproject.repository.TaskLogRepository;
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
 * REST controller for managing TaskLog.
 */
@RestController
@RequestMapping("/api")
public class TaskLogResource {

    private final Logger log = LoggerFactory.getLogger(TaskLogResource.class);

    @Inject
    private TaskLogRepository taskLogRepository;

    /**
     * POST  /taskLogs -> Create a new taskLog.
     */
    @RequestMapping(value = "/taskLogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskLog> createTaskLog(@Valid @RequestBody TaskLog taskLog) throws URISyntaxException {
        log.debug("REST request to save TaskLog : {}", taskLog);
        if (taskLog.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskLog cannot already have an ID").body(null);
        }
        TaskLog result = taskLogRepository.save(taskLog);
        return ResponseEntity.created(new URI("/api/taskLogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskLog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taskLogs -> Updates an existing taskLog.
     */
    @RequestMapping(value = "/taskLogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskLog> updateTaskLog(@Valid @RequestBody TaskLog taskLog) throws URISyntaxException {
        log.debug("REST request to update TaskLog : {}", taskLog);
        if (taskLog.getId() == null) {
            return createTaskLog(taskLog);
        }
        TaskLog result = taskLogRepository.save(taskLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskLog", taskLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taskLogs -> get all the taskLogs.
     */
    @RequestMapping(value = "/taskLogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskLog> getAllTaskLogs() {
        log.debug("REST request to get all TaskLogs");
        return taskLogRepository.findAll();
    }

    /**
     * GET  /taskLogs/:id -> get the "id" taskLog.
     */
    @RequestMapping(value = "/taskLogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskLog> getTaskLog(@PathVariable Long id) {
        log.debug("REST request to get TaskLog : {}", id);
        return Optional.ofNullable(taskLogRepository.findOne(id))
            .map(taskLog -> new ResponseEntity<>(
                taskLog,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taskLogs/:id -> delete the "id" taskLog.
     */
    @RequestMapping(value = "/taskLogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskLog(@PathVariable Long id) {
        log.debug("REST request to delete TaskLog : {}", id);
        taskLogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskLog", id.toString())).build();
    }
}
