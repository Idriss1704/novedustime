package bf.novedus.novedustime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import bf.novedus.novedustime.model.Project;
import bf.novedus.novedustime.repository.ProjectRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class ProjectController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	ProjectRepository projectRepository;

	@GetMapping("/hr/newproject")
	public String newProject(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		Project project = new Project();
		model.addAttribute("project", project);
		return "hr/newproject";

	}

	@PostMapping("/hr/newproject")
	public String newProject(@ModelAttribute(value = "project") Project project, BindingResult result, ModelMap model) {

		Project foundProject = projectRepository.findByProjectName(project.getProjectName());

		if (foundProject != null && foundProject.getId() != project.getId()) {
			FieldError error = new FieldError("project", "projectName",
					"["+ project.getProjectName()+ "] "+ Util.LanguageBundle().getString("error.namealreadyexists"));
			result.addError(error);
			model = ctrlUtil.createModelMap(model);
			model.addAttribute("project", project);
			return "hr/newproject";
		}

		projectRepository.save(project);
		return "redirect:/hr/projectlist";

	}

	@PostMapping("/hr/deleteproject")
	public String deleteProject(@ModelAttribute(value = "projectId") long projectId) {
		Project project = projectRepository.findById(projectId);
		projectRepository.delete(project);

		return "redirect:/hr/projectlist";
	}

	@PostMapping("/hr/enableproject")
	public String enableproject(@ModelAttribute(value = "projectId") long projectId) {
		Project project = projectRepository.findById(projectId);
		project.setEnabled(true);
		projectRepository.save(project);

		return "redirect:/hr/projectlist";
	}

	@PostMapping("/hr/disableproject")
	public String disableproject(@ModelAttribute(value = "projectId") long projectId) {
		Project project = projectRepository.findById(projectId);
		project.setEnabled(false);
		projectRepository.save(project);

		return "redirect:/hr/projectlist";
	}

	@GetMapping("/hr/projectlist")
	public String projectList(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		List<Project> projects = (List<Project>) projectRepository.findAll();
		model.addAttribute("projects", projects);
		return "hr/projectlist";
	}

	@GetMapping("/hr/editproject/{projectId}")
	public String editProject(ModelMap model, @PathVariable String projectId) {
		model = ctrlUtil.createModelMap(model);
		Project project = projectRepository.findById(Long.parseLong(projectId));

		model.addAttribute("project", project);
		return "hr/newproject";
	}

}
