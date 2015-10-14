package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.TaskStatus;
import com.enbiso.proj.jproject.repository.TaskStatusRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.enbiso.proj.jproject.domain.enumeration.TaskStatusState;

/**
 * Test class for the TaskStatusResource REST controller.
 *
 * @see TaskStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskStatusResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";


private static final TaskStatusState DEFAULT_STATUS = TaskStatusState.NEW;
    private static final TaskStatusState UPDATED_STATUS = TaskStatusState.OPEN;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TaskStatusRepository taskStatusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskStatusMockMvc;

    private TaskStatus taskStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskStatusResource taskStatusResource = new TaskStatusResource();
        ReflectionTestUtils.setField(taskStatusResource, "taskStatusRepository", taskStatusRepository);
        this.restTaskStatusMockMvc = MockMvcBuilders.standaloneSetup(taskStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskStatus = new TaskStatus();
        taskStatus.setTitle(DEFAULT_TITLE);
        taskStatus.setStatus(DEFAULT_STATUS);
        taskStatus.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTaskStatus() throws Exception {
        int databaseSizeBeforeCreate = taskStatusRepository.findAll().size();

        // Create the TaskStatus

        restTaskStatusMockMvc.perform(post("/api/taskStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskStatus)))
                .andExpect(status().isCreated());

        // Validate the TaskStatus in the database
        List<TaskStatus> taskStatuss = taskStatusRepository.findAll();
        assertThat(taskStatuss).hasSize(databaseSizeBeforeCreate + 1);
        TaskStatus testTaskStatus = taskStatuss.get(taskStatuss.size() - 1);
        assertThat(testTaskStatus.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTaskStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTaskStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskStatusRepository.findAll().size();
        // set the field null
        taskStatus.setTitle(null);

        // Create the TaskStatus, which fails.

        restTaskStatusMockMvc.perform(post("/api/taskStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskStatus)))
                .andExpect(status().isBadRequest());

        List<TaskStatus> taskStatuss = taskStatusRepository.findAll();
        assertThat(taskStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskStatusRepository.findAll().size();
        // set the field null
        taskStatus.setStatus(null);

        // Create the TaskStatus, which fails.

        restTaskStatusMockMvc.perform(post("/api/taskStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskStatus)))
                .andExpect(status().isBadRequest());

        List<TaskStatus> taskStatuss = taskStatusRepository.findAll();
        assertThat(taskStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskStatuss() throws Exception {
        // Initialize the database
        taskStatusRepository.saveAndFlush(taskStatus);

        // Get all the taskStatuss
        restTaskStatusMockMvc.perform(get("/api/taskStatuss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTaskStatus() throws Exception {
        // Initialize the database
        taskStatusRepository.saveAndFlush(taskStatus);

        // Get the taskStatus
        restTaskStatusMockMvc.perform(get("/api/taskStatuss/{id}", taskStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskStatus.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskStatus() throws Exception {
        // Get the taskStatus
        restTaskStatusMockMvc.perform(get("/api/taskStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskStatus() throws Exception {
        // Initialize the database
        taskStatusRepository.saveAndFlush(taskStatus);

		int databaseSizeBeforeUpdate = taskStatusRepository.findAll().size();

        // Update the taskStatus
        taskStatus.setTitle(UPDATED_TITLE);
        taskStatus.setStatus(UPDATED_STATUS);
        taskStatus.setDescription(UPDATED_DESCRIPTION);

        restTaskStatusMockMvc.perform(put("/api/taskStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskStatus)))
                .andExpect(status().isOk());

        // Validate the TaskStatus in the database
        List<TaskStatus> taskStatuss = taskStatusRepository.findAll();
        assertThat(taskStatuss).hasSize(databaseSizeBeforeUpdate);
        TaskStatus testTaskStatus = taskStatuss.get(taskStatuss.size() - 1);
        assertThat(testTaskStatus.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTaskStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTaskStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTaskStatus() throws Exception {
        // Initialize the database
        taskStatusRepository.saveAndFlush(taskStatus);

		int databaseSizeBeforeDelete = taskStatusRepository.findAll().size();

        // Get the taskStatus
        restTaskStatusMockMvc.perform(delete("/api/taskStatuss/{id}", taskStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskStatus> taskStatuss = taskStatusRepository.findAll();
        assertThat(taskStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
