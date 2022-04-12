package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.AdminPreferences;
@Repository
public interface AdminPreferencesRepository extends CrudRepository<AdminPreferences, Long> {

	AdminPreferences findById(long id);

}
