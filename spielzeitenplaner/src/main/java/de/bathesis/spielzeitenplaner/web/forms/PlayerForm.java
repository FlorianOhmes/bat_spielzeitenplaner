package de.bathesis.spielzeitenplaner.web.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class PlayerForm {

    private Integer id;

    @NotBlank(message = "Vorname darf nicht leer sein.")
    private String firstName;

    @NotBlank(message = "Nachname darf nicht leer sein.")
    private String lastName;

    @NotBlank(message = "Position darf nicht leer sein.")
    @Size(min = 2, max = 5, message = "Position muss zwischen 2 und 5 Zeichen lang sein.")
    private String position;

    @NotNull(message = "Trikotnummer darf nicht leer sein.")
    @Min(value = 1, message = "Trikotnummer muss zwischen 1 und 99 sein.")
    @Max(value = 99, message = "Trikotnummer muss zwischen 1 und 99 sein.")
    private Integer jerseyNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    @Override
    public String toString() {
        return "PlayerForm [firstName=" + firstName + ", LastName=" + lastName + ", position=" + position
                + ", jerseyNumber=" + jerseyNumber + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((jerseyNumber == null) ? 0 : jerseyNumber.hashCode());
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
        PlayerForm other = (PlayerForm) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (jerseyNumber == null) {
            if (other.jerseyNumber != null)
                return false;
        } else if (!jerseyNumber.equals(other.jerseyNumber))
            return false;
        return true;
    }

}
