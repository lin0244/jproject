package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.TaskLog;
import com.enbiso.proj.jproject.repository.TaskLogRepository;

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
 * Test class for the TaskLogResource REST controller.
 *
 * @see TaskLogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskLogResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_MESSAGE = "AAAAA";
    private static final String UPDATED_MESSAGE = "BBBBB";

    private static final DateTime DEFAULT_CREATED_ON = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_ON = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_ON_STR = dateTimeFormatter.print(DEFAULT_CREATED_ON);

    @Inject
    private TaskLogRepository taskLogRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskLogMockMvc;

    private TaskLog taskLog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskLogResource taskLogResource = new TaskLogResource();
        ReflectionTestUtils.setField(taskLogResource, "taskLogRepository", taskLogRepository);
        this.restTaskLogMockMvc = MockMvcBuilders.standaloneSetup(taskLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskLog = new TaskLog();
        taskLog.setMessage(DEFAULT_MESSAGE);
        taskLog.setCreatedOn(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    public void createTaskLog() throws Exception {
        int databaseSizeBeforeCreate = taskLogRepository.findAll().size();

        // Create the TaskLog

        restTaskLogMockMvc.perform(post("/api/taskLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskLog)))
                .andExpect(status().isCreated());

        // Validate the TaskLog in the database
        List<TaskLog> taskLogs = taskLogRepository.findAll();
        assertThat(taskLogs).hasSize(databaseSizeBeforeCreate + 1);
        TaskLog testTaskLog = taskLogs.get(taskLogs.size() - 1);
        assertThat(testTaskLog.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testTaskLog.getCreatedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskLogRepository.findAll().size();
        // set the field null
        taskLog.setMessage(null);

        // Create the TaskLog, which fails.

        restTaskLogMockMvc.perform(post("/api/taskLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskLog)))
                .andExpect(status().isBadRequest());

        List<TaskLog> taskLogs = taskLogRepository.findAll();
        assertThat(taskLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskLogRepository.findAll().size();
        // set the field null
        taskLog.setCreatedOn(null);

        // Create the TaskLog, which fails.

        restTaskLogMockMvc.perform(post("/api/taskLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskLog)))
                .andExpect(status().isBadRequest());

        List<TaskLog> taskLogs = taskLogRepository.findAll();
        assertThat(taskLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskLogs() throws Exception {
        // Initialize the database
        taskLogRepository.saveAndFlush(taskLog);

        // Get all the taskLogs
        restTaskLogMockMvc.perform(get("/api/taskLogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON_STR)));
    }

    @Test
    @Transactional
    public void getTaskLog() throws Exception {
        // Initialize the database
        taskLogRepository.saveAndFlush(taskLog);

        // Get the taskLog
        restTaskLogMockMvc.perform(get("/api/taskLogs/{id}", taskLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskLog.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTaskLog() throws Exception {
        // Get the taskLog
        restTaskLogMockMvc.perform(get("/api/taskLogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskLog() throws Exception {
        // Initialize the database
        taskLogRepository.saveAndFlush(taskLog);

		int databaseSizeBeforeUpdate = taskLogRepository.findAll().size();

        // Update the taskLog
        taskLog.setMessage(UPDATED_MESSAGE);
        taskLog.setCreatedOn(UPDATED_CREATED_ON);

        restTaskLogMockMvc.perform(put("/api/taskLogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskLog)))
                .andExpect(status().isOk());

        // Validate the TaskLog in the database
        List<TaskLog> taskLogs = taskLogRepository.findAll();
        assertThat(taskLogs).hasSize(databaseSizeBeforeUpdate);
        TaskLog testTaskLog = taskLogs.get(taskLogs.size() - 1);
        assertThat(testTaskLog.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTaskLog.getCreatedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void deleteTaskLog() throws Exception {
        // Initialize the database
        taskLogRepository.saveAndFlush(taskLog);

		int databaseSizeBeforeDelete = taskLogRepository.findAll().size();

        // Get the taskLog
        restTaskLogMockMvc.perform(delete("/api/taskLogs/{id}", taskLog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskLog> taskLogs = taskLogRepository.findAll();
        assertThat(taskLogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
