package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.UserPreferences;

@Repository
public interface UserPreferencesRepository extends CrudRepository<UserPreferences, Long> {

	UserPreferences findById(long id);

	UserPreferences findByUser_Id(long userId);

}
