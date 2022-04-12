package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.AbsenceType;
@Repository
public interface AbsenceTypeRepository extends CrudRepository<AbsenceType, Long> {

	AbsenceType findById(long id);
	
	AbsenceType findByName(String name);
	
	AbsenceType findByVacation(boolean isVacation);

}
