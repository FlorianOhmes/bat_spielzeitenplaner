package de.bathesis.spielzeitenplaner.mapper;

import java.util.List;
import java.util.ArrayList;
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

    public static List<Setting> toDomainSettings(ScoreSettingsForm settingsForm) {
        List<Setting> settings = new ArrayList<>();
        settings.add(new Setting(1195, "weeksGeneral", Double.valueOf(settingsForm.getWeeksGeneral())));
        settings.add(new Setting(1196, "weeksShortTerm", Double.valueOf(settingsForm.getWeeksShortTerm())));
        settings.add(new Setting(1197, "weightShortTerm", settingsForm.getWeightShortTerm()));
        settings.add(new Setting(1198, "weeksLongTerm", Double.valueOf(settingsForm.getWeeksLongTerm())));
        settings.add(new Setting(1199, "weightLongTerm", settingsForm.getWeightLongTerm()));
        return settings;
    }

    public static de.bathesis.spielzeitenplaner.database.entities.Setting toDatabaseSetting(Setting domainSetting) {
        return new de.bathesis.spielzeitenplaner.database.entities.Setting(
            domainSetting.getId(), domainSetting.getName(), domainSetting.getValue()
        );
    }

    public static Setting toDomainSetting(de.bathesis.spielzeitenplaner.database.entities.Setting databaseSetting) {
        return new Setting(
            databaseSetting.id(), databaseSetting.name(), databaseSetting.value()
        );
    }

}
