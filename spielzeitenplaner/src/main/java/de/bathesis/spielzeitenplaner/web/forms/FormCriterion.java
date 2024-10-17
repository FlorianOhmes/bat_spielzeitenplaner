package de.bathesis.spielzeitenplaner.web.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FormCriterion {

    private Integer id;

    @NotBlank(message = "Name des Kriteriums darf nicht leer sein.")
    private String name;

    @NotBlank(message = "Abkürzung des Kriteriums darf nicht leer sein.")
    @Size(min = 1, max = 2, message = "Abkürzung muss zwischen 1 und 2 Zeichen lang sein.")
    private String abbrev;

    @NotNull(message = "Gewicht darf nicht leer sein.")
    @Min(value = 0, message = "Gewicht muss zwischen 0 und 1 sein.")
    @Max(value = 1, message = "Gewicht muss zwischen 0 und 1 sein.")
    private Double weight;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "CriterionForm [id=" + id + ", name=" + name + ", abbrev=" + abbrev + ", weight=" + weight + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((abbrev == null) ? 0 : abbrev.hashCode());
        result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
        FormCriterion other = (FormCriterion) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (abbrev == null) {
            if (other.abbrev != null)
                return false;
        } else if (!abbrev.equals(other.abbrev))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        return true;
    }

}
