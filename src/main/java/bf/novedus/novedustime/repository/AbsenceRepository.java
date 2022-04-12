package bf.novedus.novedustime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Absence;

@Repository
public interface AbsenceRepository extends CrudRepository<Absence, Long> {

	Absence findById(long id);

	List<Absence> findByUser_Id(long userId);
	List<Absence> findByUser_UserName(String userName);
	List<Absence> findByUser_UserNameAndPlanned(String userName, boolean planned);
	List<Absence> findByUser_UserNameAndType_VacationAndPlanned(String userName, boolean isVacation, boolean isPlanned);

}
