package de.bathesis.spielzeitenplaner.domain;


public class Criterion {

    private final Integer id;
    private final String name;
    private final String abbrev;
    private final Double weight;

    public Criterion(Integer id, String name, String abbrev, Double weight) {
        this.id = id;
        this.name = name;
        this.abbrev = abbrev;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public Double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Criterion [id=" + id + ", name=" + name + ", abbrev=" + abbrev + ", weight=" + weight + "]";
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
        Criterion other = (Criterion) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
