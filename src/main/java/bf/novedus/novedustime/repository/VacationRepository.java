package bf.novedus.novedustime.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Vacation;

@Repository
public interface VacationRepository extends CrudRepository<Vacation, Long> {

	Vacation findById(long id);

	Vacation findByUser_IdAndYear(long userId, int year);

	List<Vacation> findByUser_IdAndExpirationDateBefore(long userId, Date date);
	
	List<Vacation> findByUser_Id(long userId);

}
