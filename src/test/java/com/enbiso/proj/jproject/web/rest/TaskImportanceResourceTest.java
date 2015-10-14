package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.TaskImportance;
import com.enbiso.proj.jproject.repository.TaskImportanceRepository;

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
 * Test class for the TaskImportanceResource REST controller.
 *
 * @see TaskImportanceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskImportanceResourceTest {


    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TaskImportanceRepository taskImportanceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskImportanceMockMvc;

    private TaskImportance taskImportance;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskImportanceResource taskImportanceResource = new TaskImportanceResource();
        ReflectionTestUtils.setField(taskImportanceResource, "taskImportanceRepository", taskImportanceRepository);
        this.restTaskImportanceMockMvc = MockMvcBuilders.standaloneSetup(taskImportanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskImportance = new TaskImportance();
        taskImportance.setCode(DEFAULT_CODE);
        taskImportance.setTitle(DEFAULT_TITLE);
        taskImportance.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTaskImportance() throws Exception {
        int databaseSizeBeforeCreate = taskImportanceRepository.findAll().size();

        // Create the TaskImportance

        restTaskImportanceMockMvc.perform(post("/api/taskImportances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskImportance)))
                .andExpect(status().isCreated());

        // Validate the TaskImportance in the database
        List<TaskImportance> taskImportances = taskImportanceRepository.findAll();
        assertThat(taskImportances).hasSize(databaseSizeBeforeCreate + 1);
        TaskImportance testTaskImportance = taskImportances.get(taskImportances.size() - 1);
        assertThat(testTaskImportance.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTaskImportance.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTaskImportance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskImportanceRepository.findAll().size();
        // set the field null
        taskImportance.setCode(null);

        // Create the TaskImportance, which fails.

        restTaskImportanceMockMvc.perform(post("/api/taskImportances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskImportance)))
                .andExpect(status().isBadRequest());

        List<TaskImportance> taskImportances = taskImportanceRepository.findAll();
        assertThat(taskImportances).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskImportanceRepository.findAll().size();
        // set the field null
        taskImportance.setTitle(null);

        // Create the TaskImportance, which fails.

        restTaskImportanceMockMvc.perform(post("/api/taskImportances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskImportance)))
                .andExpect(status().isBadRequest());

        List<TaskImportance> taskImportances = taskImportanceRepository.findAll();
        assertThat(taskImportances).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskImportances() throws Exception {
        // Initialize the database
        taskImportanceRepository.saveAndFlush(taskImportance);

        // Get all the taskImportances
        restTaskImportanceMockMvc.perform(get("/api/taskImportances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskImportance.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTaskImportance() throws Exception {
        // Initialize the database
        taskImportanceRepository.saveAndFlush(taskImportance);

        // Get the taskImportance
        restTaskImportanceMockMvc.perform(get("/api/taskImportances/{id}", taskImportance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskImportance.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskImportance() throws Exception {
        // Get the taskImportance
        restTaskImportanceMockMvc.perform(get("/api/taskImportances/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskImportance() throws Exception {
        // Initialize the database
        taskImportanceRepository.saveAndFlush(taskImportance);

		int databaseSizeBeforeUpdate = taskImportanceRepository.findAll().size();

        // Update the taskImportance
        taskImportance.setCode(UPDATED_CODE);
        taskImportance.setTitle(UPDATED_TITLE);
        taskImportance.setDescription(UPDATED_DESCRIPTION);

        restTaskImportanceMockMvc.perform(put("/api/taskImportances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskImportance)))
                .andExpect(status().isOk());

        // Validate the TaskImportance in the database
        List<TaskImportance> taskImportances = taskImportanceRepository.findAll();
        assertThat(taskImportances).hasSize(databaseSizeBeforeUpdate);
        TaskImportance testTaskImportance = taskImportances.get(taskImportances.size() - 1);
        assertThat(testTaskImportance.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaskImportance.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTaskImportance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTaskImportance() throws Exception {
        // Initialize the database
        taskImportanceRepository.saveAndFlush(taskImportance);

		int databaseSizeBeforeDelete = taskImportanceRepository.findAll().size();

        // Get the taskImportance
        restTaskImportanceMockMvc.perform(delete("/api/taskImportances/{id}", taskImportance.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskImportance> taskImportances = taskImportanceRepository.findAll();
        assertThat(taskImportances).hasSize(databaseSizeBeforeDelete - 1);
    }
}
