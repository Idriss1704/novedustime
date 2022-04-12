package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByRoleName(String roleName);

	Role findById(long id);

}
