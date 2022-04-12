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

import bf.novedus.novedustime.model.AbsenceType;
import bf.novedus.novedustime.repository.AbsenceTypeRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class AbsenceTypeController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	UserRepository UserRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	AbsenceTypeRepository absenceTypeRepository;

	@GetMapping("/hr/newabsencetype")
	public String newAbsenceType(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		AbsenceType absencetype = new AbsenceType();
		model.addAttribute("absencetype", absencetype);
		return "hr/newabsencetype";
	}

	@PostMapping("/hr/newabsencetype")
	public String newAbsenceType(@ModelAttribute(value = "absencetype") AbsenceType absencetype, BindingResult result,
			ModelMap model) {

		AbsenceType foundAbsenceType = absenceTypeRepository.findByName(absencetype.getName());

		if (foundAbsenceType != null && foundAbsenceType.getId() != absencetype.getId()) {
			FieldError error = new FieldError("absencetype", "name",
					"[" + absencetype.getName() + "] " + Util.LanguageBundle().getString("error.namealreadyexists"));
			result.addError(error);
			model = ctrlUtil.createModelMap(model);
			model.addAttribute("absencetype", absencetype);
			return "hr/newabsencetype";
		}

		if (absencetype.isVacation()) {
			AbsenceType found = absenceTypeRepository.findByVacation(true);
			if (found != null) {
				found.setVacation(false);
				absenceTypeRepository.save(found);
			}

		}

		absenceTypeRepository.save(absencetype);
		return "redirect:/hr/absencetypelist";

	}

	@PostMapping("/hr/deleteabsencetype")
	public String deleteAbsenceType(@ModelAttribute(value = "absencetypeId") long absencetypeId) {
		AbsenceType absencetype = absenceTypeRepository.findById(absencetypeId);
		
		//Vacation cannot be deleted
		if(absencetype.isVacation()) {
			return "redirect:/hr/absencetypelist";
		}
		
		absenceTypeRepository.delete(absencetype);

		return "redirect:/hr/absencetypelist";
	}

	@PostMapping("/hr/enableabsencetype")
	public String enableabsencetype(@ModelAttribute(value = "absencetypeId") long absencetypeId) {
		AbsenceType absencetype = absenceTypeRepository.findById(absencetypeId);
		absencetype.setActive(true);
		absenceTypeRepository.save(absencetype);

		return "redirect:/hr/absencetypelist";
	}

	@PostMapping("/hr/disableabsencetype")
	public String disableabsencetype(@ModelAttribute(value = "absencetypeId") long absencetypeId) {
		AbsenceType absencetype = absenceTypeRepository.findById(absencetypeId);
		absencetype.setActive(false);
		absenceTypeRepository.save(absencetype);

		return "redirect:/hr/absencetypelist";
	}

	@GetMapping("/hr/absencetypelist")
	public String absencetypeList(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		List<AbsenceType> absencetypes = (List<AbsenceType>) absenceTypeRepository.findAll();
		model.addAttribute("absencetypes", absencetypes);
		return "hr/absencetypelist";
	}

	@GetMapping("/hr/editabsencetype/{absencetypeId}")
	public String editAbsenceType(ModelMap model, @PathVariable String absencetypeId) {
		model = ctrlUtil.createModelMap(model);
		AbsenceType absencetype = absenceTypeRepository.findById(Long.parseLong(absencetypeId));

		model.addAttribute("absencetype", absencetype);
		return "hr/newabsencetype";
	}

}
