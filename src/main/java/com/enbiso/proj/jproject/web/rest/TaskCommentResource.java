package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskComment;
import com.enbiso.proj.jproject.repository.TaskCommentRepository;
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
 * REST controller for managing TaskComment.
 */
@RestController
@RequestMapping("/api")
public class TaskCommentResource {

    private final Logger log = LoggerFactory.getLogger(TaskCommentResource.class);

    @Inject
    private TaskCommentRepository taskCommentRepository;

    /**
     * POST  /taskComments -> Create a new taskComment.
     */
    @RequestMapping(value = "/taskComments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskComment> createTaskComment(@Valid @RequestBody TaskComment taskComment) throws URISyntaxException {
        log.debug("REST request to save TaskComment : {}", taskComment);
        if (taskComment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskComment cannot already have an ID").body(null);
        }
        TaskComment result = taskCommentRepository.save(taskComment);
        return ResponseEntity.created(new URI("/api/taskComments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskComment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taskComments -> Updates an existing taskComment.
     */
    @RequestMapping(value = "/taskComments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskComment> updateTaskComment(@Valid @RequestBody TaskComment taskComment) throws URISyntaxException {
        log.debug("REST request to update TaskComment : {}", taskComment);
        if (taskComment.getId() == null) {
            return createTaskComment(taskComment);
        }
        TaskComment result = taskCommentRepository.save(taskComment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskComment", taskComment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taskComments -> get all the taskComments.
     */
    @RequestMapping(value = "/taskComments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskComment> getAllTaskComments() {
        log.debug("REST request to get all TaskComments");
        return taskCommentRepository.findAll();
    }

    /**
     * GET  /taskComments/:id -> get the "id" taskComment.
     */
    @RequestMapping(value = "/taskComments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskComment> getTaskComment(@PathVariable Long id) {
        log.debug("REST request to get TaskComment : {}", id);
        return Optional.ofNullable(taskCommentRepository.findOne(id))
            .map(taskComment -> new ResponseEntity<>(
                taskComment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taskComments/:id -> delete the "id" taskComment.
     */
    @RequestMapping(value = "/taskComments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskComment(@PathVariable Long id) {
        log.debug("REST request to delete TaskComment : {}", id);
        taskCommentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskComment", id.toString())).build();
    }
}
