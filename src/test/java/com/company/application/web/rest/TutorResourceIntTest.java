package com.company.application.web.rest;

import com.company.application.IterateApp;

import com.company.application.domain.Tutor;
import com.company.application.repository.TutorRepository;
import com.company.application.service.TutorService;
import com.company.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.company.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TutorResource REST controller.
 *
 * @see TutorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IterateApp.class)
public class TutorResourceIntTest {

    private static final String DEFAULT_FULLNAME = "AAAAAAAAAA";
    private static final String UPDATED_FULLNAME = "BBBBBBBBBB";

    @Autowired
    private TutorRepository tutorRepository;

    @Mock
    private TutorRepository tutorRepositoryMock;

    @Mock
    private TutorService tutorServiceMock;

    @Autowired
    private TutorService tutorService;

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

    private MockMvc restTutorMockMvc;

    private Tutor tutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TutorResource tutorResource = new TutorResource(tutorService);
        this.restTutorMockMvc = MockMvcBuilders.standaloneSetup(tutorResource)
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
    public static Tutor createEntity(EntityManager em) {
        Tutor tutor = new Tutor()
            .fullname(DEFAULT_FULLNAME);
        return tutor;
    }

    @Before
    public void initTest() {
        tutor = createEntity(em);
    }

    @Test
    @Transactional
    public void createTutor() throws Exception {
        int databaseSizeBeforeCreate = tutorRepository.findAll().size();

        // Create the Tutor
        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isCreated());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate + 1);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getFullname()).isEqualTo(DEFAULT_FULLNAME);
    }

    @Test
    @Transactional
    public void createTutorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tutorRepository.findAll().size();

        // Create the Tutor with an existing ID
        tutor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFullnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setFullname(null);

        // Create the Tutor, which fails.

        restTutorMockMvc.perform(post("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTutors() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList
        restTutorMockMvc.perform(get("/api/tutors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tutor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTutorsWithEagerRelationshipsIsEnabled() throws Exception {
        TutorResource tutorResource = new TutorResource(tutorServiceMock);
        when(tutorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTutorMockMvc = MockMvcBuilders.standaloneSetup(tutorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTutorMockMvc.perform(get("/api/tutors?eagerload=true"))
        .andExpect(status().isOk());

        verify(tutorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTutorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        TutorResource tutorResource = new TutorResource(tutorServiceMock);
            when(tutorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTutorMockMvc = MockMvcBuilders.standaloneSetup(tutorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTutorMockMvc.perform(get("/api/tutors?eagerload=true"))
        .andExpect(status().isOk());

            verify(tutorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get the tutor
        restTutorMockMvc.perform(get("/api/tutors/{id}", tutor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tutor.getId().intValue()))
            .andExpect(jsonPath("$.fullname").value(DEFAULT_FULLNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTutor() throws Exception {
        // Get the tutor
        restTutorMockMvc.perform(get("/api/tutors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTutor() throws Exception {
        // Initialize the database
        tutorService.save(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor
        Tutor updatedTutor = tutorRepository.findById(tutor.getId()).get();
        // Disconnect from session so that the updates on updatedTutor are not directly saved in db
        em.detach(updatedTutor);
        updatedTutor
            .fullname(UPDATED_FULLNAME);

        restTutorMockMvc.perform(put("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTutor)))
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getFullname()).isEqualTo(UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Create the Tutor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTutorMockMvc.perform(put("/api/tutors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTutor() throws Exception {
        // Initialize the database
        tutorService.save(tutor);

        int databaseSizeBeforeDelete = tutorRepository.findAll().size();

        // Delete the tutor
        restTutorMockMvc.perform(delete("/api/tutors/{id}", tutor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tutor.class);
        Tutor tutor1 = new Tutor();
        tutor1.setId(1L);
        Tutor tutor2 = new Tutor();
        tutor2.setId(tutor1.getId());
        assertThat(tutor1).isEqualTo(tutor2);
        tutor2.setId(2L);
        assertThat(tutor1).isNotEqualTo(tutor2);
        tutor1.setId(null);
        assertThat(tutor1).isNotEqualTo(tutor2);
    }
}
