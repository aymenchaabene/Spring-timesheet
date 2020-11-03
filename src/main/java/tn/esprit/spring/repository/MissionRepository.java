package tn.esprit.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import tn.esprit.spring.entities.Mission;


public interface MissionRepository extends CrudRepository<Mission, Integer> {

	void save(Optional<Mission> mission);

}
