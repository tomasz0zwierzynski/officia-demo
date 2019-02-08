package com.company.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Available.
 */
@Entity
@Table(name = "available")
public class Available implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_begin", nullable = false)
    private LocalDate begin;

    @NotNull
    @Column(name = "jhi_end", nullable = false)
    private LocalDate end;

    @ManyToOne
    @JsonIgnoreProperties("availables")
    private Tutor tutor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public Available begin(LocalDate begin) {
        this.begin = begin;
        return this;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public Available end(LocalDate end) {
        this.end = end;
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Available tutor(Tutor tutor) {
        this.tutor = tutor;
        return this;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
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
        Available available = (Available) o;
        if (available.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), available.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Available{" +
            "id=" + getId() +
            ", begin='" + getBegin() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
