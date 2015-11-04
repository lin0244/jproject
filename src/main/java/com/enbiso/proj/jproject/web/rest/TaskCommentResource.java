package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.TaskComment;
import com.enbiso.proj.jproject.repository.ProjectRepository;
import com.enbiso.proj.jproject.repository.TaskCommentRepository;
import com.enbiso.proj.jproject.repository.TaskRepository;
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
@RequestMapping("/api/projects/{taskId}")
public class TaskCommentResource {

    private final Logger log = LoggerFactory.getLogger(TaskCommentResource.class);

    @Inject
    private TaskCommentRepository taskCommentRepository;

    @Inject
    private TaskRepository taskRepository;

    /**
     * POST  /taskComments -> Create a new taskComment.
     */
    @RequestMapping(value = "/taskComments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskComment> createTaskComment(@Valid @RequestBody TaskComment taskComment, @PathVariable Long taskId) throws URISyntaxException {
        log.debug("REST request to save TaskComment : {}", taskComment);
        if (taskComment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new taskComment cannot already have an ID").body(null);
        }
        TaskComment result = taskCommentRepository.save(taskComment);
        result.getId().setTask(taskRepository.findOne(taskId));
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
    public ResponseEntity<TaskComment> updateTaskComment(@Valid @RequestBody TaskComment taskComment, @PathVariable Long taskId) throws URISyntaxException {
        log.debug("REST request to update TaskComment : {}", taskComment);
        if (taskComment.getId() == null) {
            return createTaskComment(taskComment, taskId);
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
    public List<TaskComment> getAllTaskComments(@PathVariable Long taskId) {
        log.debug("REST request to get all TaskComments");
        return taskCommentRepository.findAllByTask(taskRepository.findOne(taskId));
    }

    /**
     * GET  /taskComments/:id -> get the "id" taskComment.
     */
    @RequestMapping(value = "/taskComments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskComment> getTaskComment(@PathVariable Integer id, @PathVariable Long taskId) {
        log.debug("REST request to get TaskComment : {}", id);
        TaskComment.Id idObj = new TaskComment.Id(id, taskRepository.findOne(taskId));
        return Optional.ofNullable(taskCommentRepository.findOne(idObj))
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
    public ResponseEntity<Void> deleteTaskComment(@PathVariable Integer id, @PathVariable Long taskId) {
        log.debug("REST request to delete TaskComment : {}", id);
        TaskComment.Id idObj = new TaskComment.Id(id, taskRepository.findOne(taskId));
        taskCommentRepository.delete(idObj);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskComment", id.toString())).build();
    }
}
