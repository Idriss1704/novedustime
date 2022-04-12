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

import bf.novedus.novedustime.model.PresenceType;
import bf.novedus.novedustime.repository.PresenceTypeRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class PresenceTypeController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	UserRepository UserRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	PresenceTypeRepository presenceTypeRepository;

	@GetMapping("/hr/newpresencetype")
	public String newPresenceType(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		PresenceType presencetype = new PresenceType();
		model.addAttribute("presencetype", presencetype);
		return "hr/newpresencetype";
	}

	@PostMapping("/hr/newpresencetype")
	public String newPresenceType(@ModelAttribute(value = "presencetype") PresenceType presencetype,
			BindingResult result, ModelMap model) {

		PresenceType foundPresenceType = presenceTypeRepository.findByName(presencetype.getName());

		if (foundPresenceType != null && foundPresenceType.getId() != presencetype.getId()) {
			FieldError error = new FieldError("presencetype", "name",
					"[" + presencetype.getName() + "] " + Util.LanguageBundle().getString("error.namealreadyexists"));
			result.addError(error);
			model = ctrlUtil.createModelMap(model);
			model.addAttribute("presencetype", presencetype);
			return "hr/newpresencetype";
		}

		presenceTypeRepository.save(presencetype);
		return "redirect:/hr/presencetypelist";

	}

	@PostMapping("/hr/deletepresencetype")
	public String deletePresenceType(@ModelAttribute(value = "presencetypeId") long presencetypeId) {
		PresenceType presencetype = presenceTypeRepository.findById(presencetypeId);
		presenceTypeRepository.delete(presencetype);

		return "redirect:/hr/presencetypelist";
	}

	@PostMapping("/hr/enablepresencetype")
	public String enablepresencetype(@ModelAttribute(value = "presencetypeId") long presencetypeId) {
		PresenceType presencetype = presenceTypeRepository.findById(presencetypeId);
		presencetype.setActive(true);
		presenceTypeRepository.save(presencetype);

		return "redirect:/hr/presencetypelist";
	}

	@PostMapping("/hr/disablepresencetype")
	public String disablepresencetype(@ModelAttribute(value = "presencetypeId") long presencetypeId) {
		PresenceType presencetype = presenceTypeRepository.findById(presencetypeId);
		presencetype.setActive(false);
		presenceTypeRepository.save(presencetype);

		return "redirect:/hr/presencetypelist";
	}

	@GetMapping("/hr/presencetypelist")
	public String presencetypeList(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		List<PresenceType> presencetypes = (List<PresenceType>) presenceTypeRepository.findAll();
		model.addAttribute("presencetypes", presencetypes);
		return "hr/presencetypelist";
	}

	@GetMapping("/hr/editpresencetype/{presencetypeId}")
	public String editPresenceType(ModelMap model, @PathVariable String presencetypeId) {
		model = ctrlUtil.createModelMap(model);
		PresenceType presencetype = presenceTypeRepository.findById(Long.parseLong(presencetypeId));

		model.addAttribute("presencetype", presencetype);
		return "hr/newpresencetype";
	}

}
