package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Team;
@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

	Team findById(long id);
	
	Team findByName(String teamName);

}
