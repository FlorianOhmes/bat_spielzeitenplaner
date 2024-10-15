package de.bathesis.spielzeitenplaner.web.forms;


public class PlayerForm {

    private Integer id;
    private String firstName;
    private String LastName;
    private String position;
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
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
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
        return "PlayerForm [firstName=" + firstName + ", LastName=" + LastName + ", position=" + position
                + ", jerseyNumber=" + jerseyNumber + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((LastName == null) ? 0 : LastName.hashCode());
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
        if (LastName == null) {
            if (other.LastName != null)
                return false;
        } else if (!LastName.equals(other.LastName))
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
