package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.TaskType;
import com.enbiso.proj.jproject.repository.TaskTypeRepository;

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


/**
 * Test class for the TaskTypeResource REST controller.
 *
 * @see TaskTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskTypeResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TaskTypeRepository taskTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskTypeMockMvc;

    private TaskType taskType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        ReflectionTestUtils.setField(taskTypeResource, "taskTypeRepository", taskTypeRepository);
        this.restTaskTypeMockMvc = MockMvcBuilders.standaloneSetup(taskTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskType = new TaskType();
        taskType.setTitle(DEFAULT_TITLE);
        taskType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTaskType() throws Exception {
        int databaseSizeBeforeCreate = taskTypeRepository.findAll().size();

        // Create the TaskType

        restTaskTypeMockMvc.perform(post("/api/taskTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskType)))
                .andExpect(status().isCreated());

        // Validate the TaskType in the database
        List<TaskType> taskTypes = taskTypeRepository.findAll();
        assertThat(taskTypes).hasSize(databaseSizeBeforeCreate + 1);
        TaskType testTaskType = taskTypes.get(taskTypes.size() - 1);
        assertThat(testTaskType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTaskType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTypeRepository.findAll().size();
        // set the field null
        taskType.setTitle(null);

        // Create the TaskType, which fails.

        restTaskTypeMockMvc.perform(post("/api/taskTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskType)))
                .andExpect(status().isBadRequest());

        List<TaskType> taskTypes = taskTypeRepository.findAll();
        assertThat(taskTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskTypes() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypes
        restTaskTypeMockMvc.perform(get("/api/taskTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskType.getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/taskTypes/{id}", taskType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskType.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingTaskType() throws Exception {
        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/taskTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

		int databaseSizeBeforeUpdate = taskTypeRepository.findAll().size();

        // Update the taskType
        taskType.setTitle(UPDATED_TITLE);
        taskType.setDescription(UPDATED_DESCRIPTION);

        restTaskTypeMockMvc.perform(put("/api/taskTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskType)))
                .andExpect(status().isOk());

        // Validate the TaskType in the database
        List<TaskType> taskTypes = taskTypeRepository.findAll();
        assertThat(taskTypes).hasSize(databaseSizeBeforeUpdate);
        TaskType testTaskType = taskTypes.get(taskTypes.size() - 1);
        assertThat(testTaskType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTaskType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

		int databaseSizeBeforeDelete = taskTypeRepository.findAll().size();

        // Get the taskType
        restTaskTypeMockMvc.perform(delete("/api/taskTypes/{id}", taskType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskType> taskTypes = taskTypeRepository.findAll();
        assertThat(taskTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
