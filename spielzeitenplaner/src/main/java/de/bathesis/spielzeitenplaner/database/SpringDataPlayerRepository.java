package de.bathesis.spielzeitenplaner.database;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface SpringDataPlayerRepository extends CrudRepository<Player, Integer> {

    Optional<Player> findById(Integer id);
    void deleteById(Integer id);

}
