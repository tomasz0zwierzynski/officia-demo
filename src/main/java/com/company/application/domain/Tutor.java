package com.company.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tutor.
 */
@Entity
@Table(name = "tutor")
public class Tutor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @ManyToOne
    @JsonIgnoreProperties("tutors")
    private User user;

    @ManyToMany
    @JoinTable(name = "tutor_subject",
               joinColumns = @JoinColumn(name = "tutor_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private Set<Subject> subjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public Tutor fullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public User getUser() {
        return user;
    }

    public Tutor user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public Tutor subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public Tutor addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getTutors().add(this);
        return this;
    }

    public Tutor removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getTutors().remove(this);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tutor tutor = (Tutor) o;
        if (tutor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tutor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tutor{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            "}";
    }
}
