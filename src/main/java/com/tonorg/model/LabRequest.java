package com.tonorg.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A lab request represents a request for analyses associated with a patient.
 * It contains metadata such as the date of the request, the date of the
 * sample collection, and the status of the request. The actual report is
 * stored in object storage (e.g. MinIO) and referenced by the
 * {@code reportObjectKey} field.
 */
@Entity
@Table(name = "lab_requests")
public class LabRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestDate;
    private LocalDateTime sampleDate;

    private String title;
    private String status;

    /**
     * The key of the generated PDF report in MinIO. This value points to
     * the location in object storage where the report is stored. For
     * example: "reports/patient42/request-17.pdf".
     */
    private String reportObjectKey;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "labRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabResult> results = new ArrayList<>();

    public LabRequest() {
    }

    public LabRequest(LocalDateTime requestDate, LocalDateTime sampleDate, String title, String status, Patient patient) {
        this.requestDate = requestDate;
        this.sampleDate = sampleDate;
        this.title = title;
        this.status = status;
        this.patient = patient;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getSampleDate() {
        return sampleDate;
    }

    public void setSampleDate(LocalDateTime sampleDate) {
        this.sampleDate = sampleDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportObjectKey() {
        return reportObjectKey;
    }

    public void setReportObjectKey(String reportObjectKey) {
        this.reportObjectKey = reportObjectKey;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<LabResult> getResults() {
        return results;
    }

    public void setResults(List<LabResult> results) {
        this.results = results;
    }
}