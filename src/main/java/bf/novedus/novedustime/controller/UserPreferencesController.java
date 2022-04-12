package bf.novedus.novedustime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.model.UserPreferences;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserPreferencesRepository;
import bf.novedus.novedustime.repository.UserRepository;

@Controller
public class UserPreferencesController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	UserRepository UserRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	UserPreferencesRepository userPreferencesRepository;

	@GetMapping("/user/preferences")
	public String newPresenceType(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		User user = UserRepository.findByUserName(ctrlUtil.getLoggedInUserName());
		UserPreferences preferences = userPreferencesRepository.findByUser_Id(user.getId());
		if (preferences == null) {
			preferences = new UserPreferences();
			preferences.setUser(user);
		}
		model.addAttribute("userPreferences", preferences);
		return "user/preferences";
	}

	@PostMapping("/user/preferences")
	public String newPresenceType(@ModelAttribute(value = "userPreferences") UserPreferences userPreferences,
			BindingResult result, ModelMap model) {
		User user = UserRepository.findByUserName(ctrlUtil.getLoggedInUserName());

		userPreferences.setUser(user);

		userPreferencesRepository.save(userPreferences);

		return "redirect:/user/preferences";

	}

}
