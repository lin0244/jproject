package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.TaskPriority;
import com.enbiso.proj.jproject.repository.TaskPriorityRepository;

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
 * Test class for the TaskPriorityResource REST controller.
 *
 * @see TaskPriorityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskPriorityResourceTest {


    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TaskPriorityRepository taskPriorityRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskPriorityMockMvc;

    private TaskPriority taskPriority;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskPriorityResource taskPriorityResource = new TaskPriorityResource();
        ReflectionTestUtils.setField(taskPriorityResource, "taskPriorityRepository", taskPriorityRepository);
        this.restTaskPriorityMockMvc = MockMvcBuilders.standaloneSetup(taskPriorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskPriority = new TaskPriority();
        taskPriority.setCode(DEFAULT_CODE);
        taskPriority.setTitle(DEFAULT_TITLE);
        taskPriority.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTaskPriority() throws Exception {
        int databaseSizeBeforeCreate = taskPriorityRepository.findAll().size();

        // Create the TaskPriority

        restTaskPriorityMockMvc.perform(post("/api/taskPrioritys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskPriority)))
                .andExpect(status().isCreated());

        // Validate the TaskPriority in the database
        List<TaskPriority> taskPrioritys = taskPriorityRepository.findAll();
        assertThat(taskPrioritys).hasSize(databaseSizeBeforeCreate + 1);
        TaskPriority testTaskPriority = taskPrioritys.get(taskPrioritys.size() - 1);
        assertThat(testTaskPriority.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTaskPriority.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTaskPriority.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskPriorityRepository.findAll().size();
        // set the field null
        taskPriority.setCode(null);

        // Create the TaskPriority, which fails.

        restTaskPriorityMockMvc.perform(post("/api/taskPrioritys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskPriority)))
                .andExpect(status().isBadRequest());

        List<TaskPriority> taskPrioritys = taskPriorityRepository.findAll();
        assertThat(taskPrioritys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskPriorityRepository.findAll().size();
        // set the field null
        taskPriority.setTitle(null);

        // Create the TaskPriority, which fails.

        restTaskPriorityMockMvc.perform(post("/api/taskPrioritys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskPriority)))
                .andExpect(status().isBadRequest());

        List<TaskPriority> taskPrioritys = taskPriorityRepository.findAll();
        assertThat(taskPrioritys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskPrioritys() throws Exception {
        // Initialize the database
        taskPriorityRepository.saveAndFlush(taskPriority);

        // Get all the taskPrioritys
        restTaskPriorityMockMvc.perform(get("/api/taskPrioritys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskPriority.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTaskPriority() throws Exception {
        // Initialize the database
        taskPriorityRepository.saveAndFlush(taskPriority);

        // Get the taskPriority
        restTaskPriorityMockMvc.perform(get("/api/taskPrioritys/{id}", taskPriority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskPriority.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskPriority() throws Exception {
        // Get the taskPriority
        restTaskPriorityMockMvc.perform(get("/api/taskPrioritys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskPriority() throws Exception {
        // Initialize the database
        taskPriorityRepository.saveAndFlush(taskPriority);

		int databaseSizeBeforeUpdate = taskPriorityRepository.findAll().size();

        // Update the taskPriority
        taskPriority.setCode(UPDATED_CODE);
        taskPriority.setTitle(UPDATED_TITLE);
        taskPriority.setDescription(UPDATED_DESCRIPTION);

        restTaskPriorityMockMvc.perform(put("/api/taskPrioritys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskPriority)))
                .andExpect(status().isOk());

        // Validate the TaskPriority in the database
        List<TaskPriority> taskPrioritys = taskPriorityRepository.findAll();
        assertThat(taskPrioritys).hasSize(databaseSizeBeforeUpdate);
        TaskPriority testTaskPriority = taskPrioritys.get(taskPrioritys.size() - 1);
        assertThat(testTaskPriority.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaskPriority.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTaskPriority.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTaskPriority() throws Exception {
        // Initialize the database
        taskPriorityRepository.saveAndFlush(taskPriority);

		int databaseSizeBeforeDelete = taskPriorityRepository.findAll().size();

        // Get the taskPriority
        restTaskPriorityMockMvc.perform(delete("/api/taskPrioritys/{id}", taskPriority.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskPriority> taskPrioritys = taskPriorityRepository.findAll();
        assertThat(taskPrioritys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
