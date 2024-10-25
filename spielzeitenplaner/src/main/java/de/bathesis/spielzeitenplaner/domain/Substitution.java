package de.bathesis.spielzeitenplaner.domain;


public class Substitution {

    private final Integer id;
    private final Integer minute;
    private final String playerIn;
    private final String playerOut;

    public Substitution(Integer id, Integer minute, String playerIn, String playerOut) {
        this.id = id;
        this.minute = minute;
        this.playerIn = playerIn;
        this.playerOut = playerOut;
    }

    public Integer getId() {
        return id;
    }

    public Integer getMinute() {
        return minute;
    }

    public String getPlayerIn() {
        return playerIn;
    }

    public String getPlayerOut() {
        return playerOut;
    }

    @Override
    public String toString() {
        return "Substitution [id=" + id + ", minute=" + minute + ", playerIn=" + playerIn + ", playerOut=" + playerOut
                + "]";
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
        Substitution other = (Substitution) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
