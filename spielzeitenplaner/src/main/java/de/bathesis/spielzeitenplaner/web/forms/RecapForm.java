package de.bathesis.spielzeitenplaner.web.forms;

import java.util.List;


public class RecapForm {

    private List<FormAssessment> assessments;


    // Standard-Konstruktor für Thymeleaf 
    public RecapForm() {}

    public RecapForm(List<FormAssessment> assessments) {
        this.assessments = assessments;
    }

    public List<FormAssessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<FormAssessment> assessments) {
        this.assessments = assessments;
    }

    public void add(FormAssessment assessment) {
        assessments.add(assessment);
    }

    @Override
    public String toString() {
        return "RecapForm [assessments=" + assessments + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (assessments == null) {
            if (other.assessments != null)
                return false;
        } else if (!assessments.equals(other.assessments))
            return false;
        return true;
    }

}
