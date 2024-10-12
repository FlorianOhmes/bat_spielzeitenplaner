package de.bathesis.spielzeitenplaner.web.forms;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Size;
import java.util.List;


public class FormationForm {

    // @NotBlank(message = "Der Name der Formation darf nicht blank sein.")
    private String name;

    // @Size(min = 11, max = 11, message = "Es müssen genau 11 Positionen angegeben werden.")
    private List<
        // @NotEmpty(message = "Position darf nicht leer sein.") 
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

}
