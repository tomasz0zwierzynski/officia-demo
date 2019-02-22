package com.company.application.web.rest;

import com.company.application.IterateApp;

import com.company.application.domain.Available;
import com.company.application.repository.AvailableRepository;
import com.company.application.service.AvailableService;
import com.company.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;


import static com.company.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AvailableResource REST controller.
 *
 * @see AvailableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IterateApp.class)
public class AvailableResourceIntTest {

    private static final LocalDateTime DEFAULT_BEGIN = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_BEGIN = LocalDateTime.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_END = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_END = LocalDateTime.now(ZoneId.systemDefault());

    @Autowired
    private AvailableRepository availableRepository;

    @Autowired
    private AvailableService availableService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAvailableMockMvc;

    private Available available;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AvailableResource availableResource = new AvailableResource(availableService);
        this.restAvailableMockMvc = MockMvcBuilders.standaloneSetup(availableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Available createEntity(EntityManager em) {
        Available available = new Available()
            .begin(DEFAULT_BEGIN)
            .end(DEFAULT_END);
        return available;
    }

    @Before
    public void initTest() {
        available = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvailable() throws Exception {
        int databaseSizeBeforeCreate = availableRepository.findAll().size();

        // Create the Available
        restAvailableMockMvc.perform(post("/api/availables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(available)))
            .andExpect(status().isCreated());

        // Validate the Available in the database
        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeCreate + 1);
        Available testAvailable = availableList.get(availableList.size() - 1);
        assertThat(testAvailable.getBegin()).isEqualTo(DEFAULT_BEGIN);
        assertThat(testAvailable.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    public void createAvailableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = availableRepository.findAll().size();

        // Create the Available with an existing ID
        available.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailableMockMvc.perform(post("/api/availables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(available)))
            .andExpect(status().isBadRequest());

        // Validate the Available in the database
        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBeginIsRequired() throws Exception {
        int databaseSizeBeforeTest = availableRepository.findAll().size();
        // set the field null
        available.setBegin(null);

        // Create the Available, which fails.

        restAvailableMockMvc.perform(post("/api/availables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(available)))
            .andExpect(status().isBadRequest());

        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = availableRepository.findAll().size();
        // set the field null
        available.setEnd(null);

        // Create the Available, which fails.

        restAvailableMockMvc.perform(post("/api/availables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(available)))
            .andExpect(status().isBadRequest());

        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAvailables() throws Exception {
        // Initialize the database
        availableRepository.saveAndFlush(available);

        // Get all the availableList
        restAvailableMockMvc.perform(get("/api/availables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(available.getId().intValue())))
            .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }
    
    @Test
    @Transactional
    public void getAvailable() throws Exception {
        // Initialize the database
        availableRepository.saveAndFlush(available);

        // Get the available
        restAvailableMockMvc.perform(get("/api/availables/{id}", available.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(available.getId().intValue()))
            .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAvailable() throws Exception {
        // Get the available
        restAvailableMockMvc.perform(get("/api/availables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailable() throws Exception {
        // Initialize the database
        availableService.save(available);

        int databaseSizeBeforeUpdate = availableRepository.findAll().size();

        // Update the available
        Available updatedAvailable = availableRepository.findById(available.getId()).get();
        // Disconnect from session so that the updates on updatedAvailable are not directly saved in db
        em.detach(updatedAvailable);
        updatedAvailable
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);

        restAvailableMockMvc.perform(put("/api/availables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvailable)))
            .andExpect(status().isOk());

        // Validate the Available in the database
        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeUpdate);
        Available testAvailable = availableList.get(availableList.size() - 1);
        assertThat(testAvailable.getBegin()).isEqualTo(UPDATED_BEGIN);
        assertThat(testAvailable.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    public void updateNonExistingAvailable() throws Exception {
        int databaseSizeBeforeUpdate = availableRepository.findAll().size();

        // Create the Available

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailableMockMvc.perform(put("/api/availables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(available)))
            .andExpect(status().isBadRequest());

        // Validate the Available in the database
        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAvailable() throws Exception {
        // Initialize the database
        availableService.save(available);

        int databaseSizeBeforeDelete = availableRepository.findAll().size();

        // Delete the available
        restAvailableMockMvc.perform(delete("/api/availables/{id}", available.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Available> availableList = availableRepository.findAll();
        assertThat(availableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Available.class);
        Available available1 = new Available();
        available1.setId(1L);
        Available available2 = new Available();
        available2.setId(available1.getId());
        assertThat(available1).isEqualTo(available2);
        available2.setId(2L);
        assertThat(available1).isNotEqualTo(available2);
        available1.setId(null);
        assertThat(available1).isNotEqualTo(available2);
    }
}
