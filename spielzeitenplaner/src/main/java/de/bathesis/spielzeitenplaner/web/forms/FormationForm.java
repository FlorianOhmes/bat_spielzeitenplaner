package de.bathesis.spielzeitenplaner.web.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;


public class FormationForm {

    @NotBlank(message = "Der Name der Formation darf nicht blank sein.")
    private String name;

    private List<@NotEmpty(message = "Position darf nicht leer sein.") 
                 String> positions;

    public FormationForm(String name, List<String> positions) {
        this.name = name;
        this.positions = positions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "FormationForm [name=" + name + ", positions=" + positions + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((positions == null) ? 0 : positions.hashCode());
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
        FormationForm other = (FormationForm) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (positions == null) {
            if (other.positions != null)
                return false;
        } else if (!positions.equals(other.positions))
            return false;
        return true;
    }

}
