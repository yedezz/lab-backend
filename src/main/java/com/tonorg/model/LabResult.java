package com.tonorg.model;

import jakarta.persistence.*;

/**
 * A lab result represents a single test result belonging to a {@link LabRequest}.
 * Each test has a name, a measured value, a unit, and optional reference
 * ranges. The reference ranges are stored as strings here to allow
 * different units or formats (e.g. "4.0–5.6 mmol/L").
 */
@Entity
@Table(name = "lab_results")
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;
    private String value;
    private String unit;
    private String referenceMin;
    private String referenceMax;

    @ManyToOne
    @JoinColumn(name = "lab_request_id")
    private LabRequest labRequest;

    public LabResult() {
    }

    public LabResult(String testName, String value, String unit, String referenceMin, String referenceMax, LabRequest labRequest) {
        this.testName = testName;
        this.value = value;
        this.unit = unit;
        this.referenceMin = referenceMin;
        this.referenceMax = referenceMax;
        this.labRequest = labRequest;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReferenceMin() {
        return referenceMin;
    }

    public void setReferenceMin(String referenceMin) {
        this.referenceMin = referenceMin;
    }

    public String getReferenceMax() {
        return referenceMax;
    }

    public void setReferenceMax(String referenceMax) {
        this.referenceMax = referenceMax;
    }

    public LabRequest getLabRequest() {
        return labRequest;
    }

    public void setLabRequest(LabRequest labRequest) {
        this.labRequest = labRequest;
    }
}