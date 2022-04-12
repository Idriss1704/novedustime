package bf.novedus.novedustime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.WorkingMonth;
@Repository
public interface WorkingMonthRepository extends CrudRepository<WorkingMonth, Long> {

	WorkingMonth findById(long id);
	
	WorkingMonth findByUserIdAndMonthAndYear(long id, int month, int year);
	
	List<WorkingMonth> findByUser_Id(long userId);

}
