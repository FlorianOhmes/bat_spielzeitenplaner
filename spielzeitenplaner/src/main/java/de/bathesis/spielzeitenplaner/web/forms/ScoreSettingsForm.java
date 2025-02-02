package de.bathesis.spielzeitenplaner.web.forms;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ScoreSettingsForm {

    @NotNull(message = "Darf nicht leer sein")
    @Min(value = 1, message = "Muss zwischen 1 und 36 Wochen liegen.")
    @Max(value = 36, message = "Muss zwischen 1 und 36 Wochen liegen.")
    private Integer weeksGeneral;

    @NotNull(message = "Darf nicht leer sein")
    @Min(value = 1, message = "Muss zwischen 1 und 12 Wochen liegen.")
    @Max(value = 12, message = "Muss zwischen 1 und 12 Wochen liegen.")
    private Integer weeksShortTerm;

    @NotNull(message = "Darf nicht leer sein")
    @DecimalMin(value = "0.0", message = "Muss zwischen 0 und 1 sein.")
    @DecimalMax(value = "1.0", message = "Muss zwischen 0 und 1 sein.")
    private Double weightShortTerm;

    @NotNull(message = "Darf nicht leer sein")
    @Min(value = 1, message = "Muss zwischen 1 und 36 Wochen liegen.")
    @Max(value = 36, message = "Muss zwischen 1 und 36 Wochen liegen.")
    private Integer weeksLongTerm;

    @NotNull(message = "Darf nicht leer sein")
    @DecimalMin(value = "0.0", message = "Muss zwischen 0 und 1 sein.")
    @DecimalMax(value = "1.0", message = "Muss zwischen 0 und 1 sein.")
    private Double weightLongTerm;


    public Integer getWeeksGeneral() {
        return weeksGeneral;
    }

    public void setWeeksGeneral(Integer weeksGeneral) {
        this.weeksGeneral = weeksGeneral;
    }

    public Integer getWeeksShortTerm() {
        return weeksShortTerm;
    }

    public void setWeeksShortTerm(Integer weeksShortTerm) {
        this.weeksShortTerm = weeksShortTerm;
    }

    public Double getWeightShortTerm() {
        return weightShortTerm;
    }

    public void setWeightShortTerm(Double weightShortTerm) {
        this.weightShortTerm = weightShortTerm;
    }

    public Integer getWeeksLongTerm() {
        return weeksLongTerm;
    }

    public void setWeeksLongTerm(Integer weeksLongTerm) {
        this.weeksLongTerm = weeksLongTerm;
    }

    public Double getWeightLongTerm() {
        return weightLongTerm;
    }

    public void setWeightLongTerm(Double weightLongTerm) {
        this.weightLongTerm = weightLongTerm;
    }

    @Override
    public String toString() {
        return "ScoreSettingsForm [weeksGeneral=" + weeksGeneral + ", weeksShortTerm=" + weeksShortTerm
                + ", weightShortTerm=" + weightShortTerm + ", weeksLongTerm=" + weeksLongTerm + ", weightLongTerm="
                + weightLongTerm + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((weeksGeneral == null) ? 0 : weeksGeneral.hashCode());
        result = prime * result + ((weeksShortTerm == null) ? 0 : weeksShortTerm.hashCode());
        result = prime * result + ((weightShortTerm == null) ? 0 : weightShortTerm.hashCode());
        result = prime * result + ((weeksLongTerm == null) ? 0 : weeksLongTerm.hashCode());
        result = prime * result + ((weightLongTerm == null) ? 0 : weightLongTerm.hashCode());
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
        ScoreSettingsForm other = (ScoreSettingsForm) obj;
        if (weeksGeneral == null) {
            if (other.weeksGeneral != null)
                return false;
        } else if (!weeksGeneral.equals(other.weeksGeneral))
            return false;
        if (weeksShortTerm == null) {
            if (other.weeksShortTerm != null)
                return false;
        } else if (!weeksShortTerm.equals(other.weeksShortTerm))
            return false;
        if (weightShortTerm == null) {
            if (other.weightShortTerm != null)
                return false;
        } else if (!weightShortTerm.equals(other.weightShortTerm))
            return false;
        if (weeksLongTerm == null) {
            if (other.weeksLongTerm != null)
                return false;
        } else if (!weeksLongTerm.equals(other.weeksLongTerm))
            return false;
        if (weightLongTerm == null) {
            if (other.weightLongTerm != null)
                return false;
        } else if (!weightLongTerm.equals(other.weightLongTerm))
            return false;
        return true;
    }

}
