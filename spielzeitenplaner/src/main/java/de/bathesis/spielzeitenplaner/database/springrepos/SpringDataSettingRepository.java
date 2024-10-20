package de.bathesis.spielzeitenplaner.database.springrepos;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Setting;


public interface SpringDataSettingRepository extends CrudRepository<Setting, Integer> {

    Setting save(Setting setting);
    Optional<Setting> findById(Integer id);

}
