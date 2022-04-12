package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Presence;
@Repository
public interface PresenceRepository extends CrudRepository<Presence, Long> {

	Presence findById(long id);

}
