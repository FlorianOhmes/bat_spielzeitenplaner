package de.bathesis.spielzeitenplaner.domain;

import java.util.List;

public class Formation {

    private final Integer id;
    private final String name;
    private final List<String> positions;

    public Formation(Integer id, String name, List<String> positions) {
        this.id = id;
        this.name = name;
        this.positions = positions;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getPositions() {
        return positions;
    }

    public String getPosition(int index) {
        return positions.get(index);
    }

    @Override
    public String toString() {
        return "Formation [id=" + id + ", name=" + name + "]";
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
        Formation other = (Formation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
