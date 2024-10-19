package de.bathesis.spielzeitenplaner.domain;

import java.time.LocalDate;


public class Assessment {

    private final Integer id;
    private final LocalDate date;
    private final Integer criterionId;
    private final Integer playerId;
    private final Double value;

    public Assessment(Integer id, LocalDate date, Integer criterionId, Integer playerId, Double value) {
        this.id = id;
        this.date = date;
        this.criterionId = criterionId;
        this.playerId = playerId;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getCriterionId() {
        return criterionId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Assessment [id=" + id + ", date=" + date + ", criterionId=" + criterionId + ", playerId=" + playerId
                + ", value=" + value + "]";
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
        Assessment other = (Assessment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
