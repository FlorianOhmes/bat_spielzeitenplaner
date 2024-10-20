package de.bathesis.spielzeitenplaner.database.repoimpl;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataSettingRepository;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.mapper.SettingsMapper;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;


@Repository
public class SettingRepositoryImpl implements SettingRepository {

    private final SpringDataSettingRepository springRepository;

    public SettingRepositoryImpl(SpringDataSettingRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Setting save(Setting setting) {
        de.bathesis.spielzeitenplaner.database.entities.Setting databaseSetting = 
            SettingsMapper.toDatabaseSetting(setting);
        de.bathesis.spielzeitenplaner.database.entities.Setting saved = springRepository.save(databaseSetting);
        return SettingsMapper.toDomainSetting(saved);
    }

    @Override
    public Optional<Setting> findById(Integer id) {
        Optional<de.bathesis.spielzeitenplaner.database.entities.Setting> foundById = springRepository.findById(id);
        return foundById.map(SettingsMapper::toDomainSetting);
    }

}
