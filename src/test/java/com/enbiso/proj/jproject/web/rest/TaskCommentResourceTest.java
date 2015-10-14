package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.TaskComment;
import com.enbiso.proj.jproject.repository.TaskCommentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TaskCommentResource REST controller.
 *
 * @see TaskCommentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskCommentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final DateTime DEFAULT_POSTED_ON = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_POSTED_ON = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_POSTED_ON_STR = dateTimeFormatter.print(DEFAULT_POSTED_ON);

    @Inject
    private TaskCommentRepository taskCommentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskCommentMockMvc;

    private TaskComment taskComment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskCommentResource taskCommentResource = new TaskCommentResource();
        ReflectionTestUtils.setField(taskCommentResource, "taskCommentRepository", taskCommentRepository);
        this.restTaskCommentMockMvc = MockMvcBuilders.standaloneSetup(taskCommentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskComment = new TaskComment();
        taskComment.setContent(DEFAULT_CONTENT);
        taskComment.setPostedOn(DEFAULT_POSTED_ON);
    }

    @Test
    @Transactional
    public void createTaskComment() throws Exception {
        int databaseSizeBeforeCreate = taskCommentRepository.findAll().size();

        // Create the TaskComment

        restTaskCommentMockMvc.perform(post("/api/taskComments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskComment)))
                .andExpect(status().isCreated());

        // Validate the TaskComment in the database
        List<TaskComment> taskComments = taskCommentRepository.findAll();
        assertThat(taskComments).hasSize(databaseSizeBeforeCreate + 1);
        TaskComment testTaskComment = taskComments.get(taskComments.size() - 1);
        assertThat(testTaskComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTaskComment.getPostedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_POSTED_ON);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskCommentRepository.findAll().size();
        // set the field null
        taskComment.setContent(null);

        // Create the TaskComment, which fails.

        restTaskCommentMockMvc.perform(post("/api/taskComments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskComment)))
                .andExpect(status().isBadRequest());

        List<TaskComment> taskComments = taskCommentRepository.findAll();
        assertThat(taskComments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskCommentRepository.findAll().size();
        // set the field null
        taskComment.setPostedOn(null);

        // Create the TaskComment, which fails.

        restTaskCommentMockMvc.perform(post("/api/taskComments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskComment)))
                .andExpect(status().isBadRequest());

        List<TaskComment> taskComments = taskCommentRepository.findAll();
        assertThat(taskComments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskComments() throws Exception {
        // Initialize the database
        taskCommentRepository.saveAndFlush(taskComment);

        // Get all the taskComments
        restTaskCommentMockMvc.perform(get("/api/taskComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskComment.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].postedOn").value(hasItem(DEFAULT_POSTED_ON_STR)));
    }

    @Test
    @Transactional
    public void getTaskComment() throws Exception {
        // Initialize the database
        taskCommentRepository.saveAndFlush(taskComment);

        // Get the taskComment
        restTaskCommentMockMvc.perform(get("/api/taskComments/{id}", taskComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskComment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.postedOn").value(DEFAULT_POSTED_ON_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTaskComment() throws Exception {
        // Get the taskComment
        restTaskCommentMockMvc.perform(get("/api/taskComments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskComment() throws Exception {
        // Initialize the database
        taskCommentRepository.saveAndFlush(taskComment);

		int databaseSizeBeforeUpdate = taskCommentRepository.findAll().size();

        // Update the taskComment
        taskComment.setContent(UPDATED_CONTENT);
        taskComment.setPostedOn(UPDATED_POSTED_ON);

        restTaskCommentMockMvc.perform(put("/api/taskComments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskComment)))
                .andExpect(status().isOk());

        // Validate the TaskComment in the database
        List<TaskComment> taskComments = taskCommentRepository.findAll();
        assertThat(taskComments).hasSize(databaseSizeBeforeUpdate);
        TaskComment testTaskComment = taskComments.get(taskComments.size() - 1);
        assertThat(testTaskComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTaskComment.getPostedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_POSTED_ON);
    }

    @Test
    @Transactional
    public void deleteTaskComment() throws Exception {
        // Initialize the database
        taskCommentRepository.saveAndFlush(taskComment);

		int databaseSizeBeforeDelete = taskCommentRepository.findAll().size();

        // Get the taskComment
        restTaskCommentMockMvc.perform(delete("/api/taskComments/{id}", taskComment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskComment> taskComments = taskCommentRepository.findAll();
        assertThat(taskComments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
