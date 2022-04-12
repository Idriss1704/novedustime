package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.MinimalPause;

@Repository
public interface MinimalPauseRepository extends CrudRepository<MinimalPause, Long> {

	MinimalPause findById(long id);

}
