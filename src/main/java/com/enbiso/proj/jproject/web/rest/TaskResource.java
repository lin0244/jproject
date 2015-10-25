package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.Task;
import com.enbiso.proj.jproject.repository.TaskRepository;
import com.enbiso.proj.jproject.web.rest.util.HeaderUtil;
import com.enbiso.proj.jproject.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    @Inject
    private TaskRepository taskRepository;

    /**
     * POST  /tasks -> Create a new task.
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        if (task.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new task cannot already have an ID").body(null);
        }
        Task result = taskRepository.save(task);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("task", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks -> Updates an existing task.
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to update Task : {}", task);
        if (task.getId() == null) {
            return createTask(task);
        }
        Task result = taskRepository.save(task);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("task", task.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tasks -> get all the tasks.
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Task>> getAllTasks(Pageable pageable)
        throws URISyntaxException {
        Page<Task> page = taskRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tasks/:id -> get the "id" task.
     */
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        return Optional.ofNullable(taskRepository.findOne(id))
            .map(task -> new ResponseEntity<>(
                task,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tasks/:id -> delete the "id" task.
     */
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("task", id.toString())).build();
    }
}
