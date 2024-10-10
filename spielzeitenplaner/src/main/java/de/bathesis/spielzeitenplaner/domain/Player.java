package de.bathesis.spielzeitenplaner.domain;


public class Player {

    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String position;
    private final Integer jerseyNumber;

    public Player(Integer id, String firstName, String lastName, String position, Integer jerseyNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    @Override
    public String toString() {
        return "Player [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", position=" + position
                + ", jerseyNumber=" + jerseyNumber + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Player other = (Player) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
