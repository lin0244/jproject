package com.enbiso.proj.jproject.web.rest;

import com.enbiso.proj.jproject.Application;
import com.enbiso.proj.jproject.domain.Iteration;
import com.enbiso.proj.jproject.repository.IterationRepository;

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
 * Test class for the IterationResource REST controller.
 *
 * @see IterationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IterationResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final DateTime DEFAULT_START_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_START_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.print(DEFAULT_START_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    @Inject
    private IterationRepository iterationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIterationMockMvc;

    private Iteration iteration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IterationResource iterationResource = new IterationResource();
        ReflectionTestUtils.setField(iterationResource, "iterationRepository", iterationRepository);
        this.restIterationMockMvc = MockMvcBuilders.standaloneSetup(iterationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        iteration = new Iteration();
        iteration.setTitle(DEFAULT_TITLE);
        iteration.setStartDate(DEFAULT_START_DATE);
        iteration.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createIteration() throws Exception {
        int databaseSizeBeforeCreate = iterationRepository.findAll().size();

        // Create the Iteration

        restIterationMockMvc.perform(post("/api/iterations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(iteration)))
                .andExpect(status().isCreated());

        // Validate the Iteration in the database
        List<Iteration> iterations = iterationRepository.findAll();
        assertThat(iterations).hasSize(databaseSizeBeforeCreate + 1);
        Iteration testIteration = iterations.get(iterations.size() - 1);
        assertThat(testIteration.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIteration.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
        assertThat(testIteration.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = iterationRepository.findAll().size();
        // set the field null
        iteration.setTitle(null);

        // Create the Iteration, which fails.

        restIterationMockMvc.perform(post("/api/iterations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(iteration)))
                .andExpect(status().isBadRequest());

        List<Iteration> iterations = iterationRepository.findAll();
        assertThat(iterations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = iterationRepository.findAll().size();
        // set the field null
        iteration.setStartDate(null);

        // Create the Iteration, which fails.

        restIterationMockMvc.perform(post("/api/iterations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(iteration)))
                .andExpect(status().isBadRequest());

        List<Iteration> iterations = iterationRepository.findAll();
        assertThat(iterations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = iterationRepository.findAll().size();
        // set the field null
        iteration.setEndDate(null);

        // Create the Iteration, which fails.

        restIterationMockMvc.perform(post("/api/iterations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(iteration)))
                .andExpect(status().isBadRequest());

        List<Iteration> iterations = iterationRepository.findAll();
        assertThat(iterations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIterations() throws Exception {
        // Initialize the database
        iterationRepository.saveAndFlush(iteration);

        // Get all the iterations
        restIterationMockMvc.perform(get("/api/iterations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(iteration.getId().getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getIteration() throws Exception {
        // Initialize the database
        iterationRepository.saveAndFlush(iteration);

        // Get the iteration
        restIterationMockMvc.perform(get("/api/iterations/{id}", iteration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(iteration.getId().getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingIteration() throws Exception {
        // Get the iteration
        restIterationMockMvc.perform(get("/api/iterations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIteration() throws Exception {
        // Initialize the database
        iterationRepository.saveAndFlush(iteration);

		int databaseSizeBeforeUpdate = iterationRepository.findAll().size();

        // Update the iteration
        iteration.setTitle(UPDATED_TITLE);
        iteration.setStartDate(UPDATED_START_DATE);
        iteration.setEndDate(UPDATED_END_DATE);

        restIterationMockMvc.perform(put("/api/iterations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(iteration)))
                .andExpect(status().isOk());

        // Validate the Iteration in the database
        List<Iteration> iterations = iterationRepository.findAll();
        assertThat(iterations).hasSize(databaseSizeBeforeUpdate);
        Iteration testIteration = iterations.get(iterations.size() - 1);
        assertThat(testIteration.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIteration.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_DATE);
        assertThat(testIteration.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteIteration() throws Exception {
        // Initialize the database
        iterationRepository.saveAndFlush(iteration);

		int databaseSizeBeforeDelete = iterationRepository.findAll().size();

        // Get the iteration
        restIterationMockMvc.perform(delete("/api/iterations/{id}", iteration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Iteration> iterations = iterationRepository.findAll();
        assertThat(iterations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
