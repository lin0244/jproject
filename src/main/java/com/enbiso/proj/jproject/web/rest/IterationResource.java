package com.enbiso.proj.jproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enbiso.proj.jproject.domain.Iteration;
import com.enbiso.proj.jproject.domain.Project;
import com.enbiso.proj.jproject.repository.IterationRepository;
import com.enbiso.proj.jproject.repository.ProjectRepository;
import com.enbiso.proj.jproject.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Iteration.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class IterationResource {

    private final Logger log = LoggerFactory.getLogger(IterationResource.class);

    @Inject
    private IterationRepository iterationRepository;

    @Inject
    private ProjectRepository projectRepository;

    /**
     * POST  /iterations -> Create a new iteration.
     */
    @RequestMapping(value = "/iterations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Iteration> createIteration(@Valid @RequestBody Iteration iteration, @PathVariable String projectId) throws URISyntaxException {
        log.debug("REST request to save Iteration : {}", iteration);
        if (iteration.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new iteration cannot already have an ID").body(null);
        }
        Project project = projectRepository.findOne(projectId);
        iteration.getId().setProject(project);
        Iteration result = iterationRepository.save(iteration);
        return ResponseEntity.created(new URI("/api/iterations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("iteration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /iterations -> Updates an existing iteration.
     */
    @RequestMapping(value = "/iterations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Iteration> updateIteration(@Valid @RequestBody Iteration iteration, @PathVariable String projectId) throws URISyntaxException {
        log.debug("REST request to update Iteration : {}", iteration);
        if (iteration.getId() == null) {
            return createIteration(iteration, projectId);
        }
        Iteration result = iterationRepository.save(iteration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("iteration", iteration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /iterations -> get all the iterations.
     */
    @RequestMapping(value = "/iterations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Iteration> getAllIterations(@PathVariable String projectId) {
        log.debug("REST request to get all Iterations");
        return iterationRepository.findAllByProject(projectRepository.findOne(projectId));
    }

    /**
     * GET  /iterations/:id -> get the "id" iteration.
     */
    @RequestMapping(value = "/iterations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Iteration> getIteration(@PathVariable String id, @PathVariable String projectId) {
        log.debug("REST request to get Iteration : {}", id);
        Iteration.Id idObj = new Iteration.Id(id, projectRepository.findOne(projectId));
        return Optional.ofNullable(iterationRepository.findOne(idObj))
            .map(iteration -> new ResponseEntity<>(
                iteration,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /iterations/:id -> delete the "id" iteration.
     */
    @RequestMapping(value = "/iterations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIteration(@PathVariable String id, @PathVariable String projectId) {
        log.debug("REST request to delete Iteration : {}", id);
        Iteration.Id idObj = new Iteration.Id(id, projectRepository.findOne(projectId));
        iterationRepository.delete(idObj);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("iteration", id)).build();
    }
}
