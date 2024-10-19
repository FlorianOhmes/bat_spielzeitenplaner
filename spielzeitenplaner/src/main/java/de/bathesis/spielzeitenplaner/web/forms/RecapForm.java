package de.bathesis.spielzeitenplaner.web.forms;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;


public class RecapForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<FormAssessment> assessments;


    // Standard-Konstruktor für Thymeleaf 
    public RecapForm() {}

    public RecapForm(LocalDate date, List<FormAssessment> assessments) {
        this.date = date;
        this.assessments = assessments;
    }

    public List<FormAssessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<FormAssessment> assessments) {
        this.assessments = assessments;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void add(FormAssessment assessment) {
        assessments.add(assessment);
    }

    @Override
    public String toString() {
        return "RecapForm [date=" + date + ", assessments=" + assessments + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((assessments == null) ? 0 : assessments.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecapForm other = (RecapForm) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (assessments == null) {
            if (other.assessments != null)
                return false;
        } else if (!assessments.equals(other.assessments))
            return false;
        return true;
    }

}
