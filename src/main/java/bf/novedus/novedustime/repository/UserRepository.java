package bf.novedus.novedustime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUserName(String userName);

	User findById(long id);

	List<User> findByRoleRoleName(String roleName);

}
