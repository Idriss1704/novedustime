package bf.novedus.novedustime.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.WorkingDay;

@Repository
public interface WorkingDayRepository extends CrudRepository<WorkingDay, Long> {

	WorkingDay findById(long id);

	List<WorkingDay> findByWorkingMonth_Id(long workingMonthId);

	List<WorkingDay> findByUser_IdAndDateBetween(long userId, Date startDate, Date endDate);

	List<WorkingDay> findByUser_IdAndWorkingMonth_IdAndDate(long userId, long workingMonthId, Date startDate);

}
