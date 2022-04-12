package bf.novedus.novedustime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bf.novedus.novedustime.model.Project;
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	Project findById(long id);
	
	Project findByProjectName(String projectName);

}
