package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Optional;

import de.bathesis.spielzeitenplaner.domain.Setting;


public interface SettingRepository {

    Setting save(Setting setting);
    Optional<Setting> findById(Integer id);

}
