package bf.novedus.novedustime.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.AbsenceDay;

@Repository
public interface AbsenceDayRepository extends CrudRepository<AbsenceDay, Long> {

	List<AbsenceDay> findByUser_IdAndWorkingMonth_Id(long userId, long workingMonthId);

	AbsenceDay findById(long id);
	
	List<AbsenceDay> findByAbsence_Id(long absenceId);
	
	List<AbsenceDay> findByUser_IdAndDateBetween(long userId, Date startDate, Date endDate);

}
