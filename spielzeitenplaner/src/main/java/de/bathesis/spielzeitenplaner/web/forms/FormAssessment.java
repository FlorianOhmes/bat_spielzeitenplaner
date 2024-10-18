package de.bathesis.spielzeitenplaner.web.forms;


public class FormAssessment {

    private Integer criterionId;
    private String criterionName;
    private Integer playerId;
    private String playerName;
    private Double value;


    // Standard-Konstruktor für Thymeleaf 
    public FormAssessment() {}

    public FormAssessment(Integer criterionId, String criterionName, Integer playerId, String playerName, Double value) {
        this.criterionId = criterionId;
        this.criterionName = criterionName;
        this.playerId = playerId;
        this.playerName = playerName;
        this.value = value;
    }

    public Integer getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Integer criterionId) {
        this.criterionId = criterionId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCriterionName() {
        return criterionName;
    }

    public void setCriterionName(String criterionName) {
        this.criterionName = criterionName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((criterionId == null) ? 0 : criterionId.hashCode());
        result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        FormAssessment other = (FormAssessment) obj;
        if (criterionId == null) {
            if (other.criterionId != null)
                return false;
        } else if (!criterionId.equals(other.criterionId))
            return false;
        if (playerId == null) {
            if (other.playerId != null)
                return false;
        } else if (!playerId.equals(other.playerId))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FormAssessment [criterionId=" + criterionId + ", criterionName=" + criterionName + ", playerId="
                + playerId + ", playerName=" + playerName + ", value=" + value + "]";
    }

}
