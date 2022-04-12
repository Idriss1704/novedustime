package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.WeekDay;

@Repository
public interface WeekDayRepository extends CrudRepository<WeekDay, Long> {

	WeekDay findById(long id);

	WeekDay findByNumber(int number);

}
