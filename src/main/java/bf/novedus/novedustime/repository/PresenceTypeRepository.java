package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.PresenceType;
@Repository
public interface PresenceTypeRepository extends CrudRepository<PresenceType, Long> {
	
	PresenceType findById(long id);
	
	PresenceType findByName(String name);

}
