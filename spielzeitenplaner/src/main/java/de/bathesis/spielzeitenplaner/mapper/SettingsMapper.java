package de.bathesis.spielzeitenplaner.mapper;

import java.util.List;
import de.bathesis.spielzeitenplaner.web.forms.ScoreSettingsForm;
import de.bathesis.spielzeitenplaner.domain.Setting;


public class SettingsMapper {

    public static ScoreSettingsForm toScoreSettingsForm(List<Setting> settings) {
        ScoreSettingsForm scoreSettingsForm = new ScoreSettingsForm();
        scoreSettingsForm.setWeeksGeneral(settings.get(0).getValue().intValue());
        scoreSettingsForm.setWeeksShortTerm(settings.get(1).getValue().intValue());
        scoreSettingsForm.setWeightShortTerm(settings.get(2).getValue());
        scoreSettingsForm.setWeeksLongTerm(settings.get(3).getValue().intValue());
        scoreSettingsForm.setWeightLongTerm(settings.get(4).getValue());
        return scoreSettingsForm;
    }

}
