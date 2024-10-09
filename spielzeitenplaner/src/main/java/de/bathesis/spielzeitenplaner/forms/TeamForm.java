package de.bathesis.spielzeitenplaner.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class TeamForm {

    @NotBlank(message = "Der Teamname darf nicht leer sein.")
    @Size(max = 100, message = "Der Teamname darf maximal 100 Zeichen lang sein.")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TeamForm [name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        TeamForm other = (TeamForm) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
