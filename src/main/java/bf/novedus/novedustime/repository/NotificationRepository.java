package bf.novedus.novedustime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Notification;
@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

	Notification findById(long id);

	List<Notification> findByUserUserNameAndIsRead(String userName, boolean read);

}
