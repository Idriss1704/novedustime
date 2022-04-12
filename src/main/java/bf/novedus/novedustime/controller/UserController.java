package bf.novedus.novedustime.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.model.WeekDay;
import bf.novedus.novedustime.model.AdminPreferences;
import bf.novedus.novedustime.model.Role;
import bf.novedus.novedustime.model.Team;
import bf.novedus.novedustime.repository.AdminPreferencesRepository;
import bf.novedus.novedustime.repository.RoleRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.repository.WeekDayRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class UserController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	WeekDayRepository weekDayRepository;

	@Autowired
	AdminPreferencesRepository basicDataRepository;

	@GetMapping("/hr/newuser")
	public String newUser(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		AdminPreferences basicData = ctrlUtil.getAdminPreferences();

		User user = new User();
		user.setActive(true);
		user.setDayHours(basicData.getDefaultDayHours());
		user.setVacationCount(basicData.getVacationsPerYear());
		//TODO move to basicDataController
		List<WeekDay> weekDays = basicData.getDaysOfWeek();
		if (weekDays == null || weekDays.isEmpty()) {
			weekDays = new ArrayList<WeekDay>();
			List<WeekDay> temp = (List<WeekDay>) weekDayRepository.findAll();
			for (WeekDay d : temp) {
				if (d.getNumber() < 6) {
					weekDays.add(d);
				}
			}
			basicData.setDaysOfWeek(weekDays);
			basicData = basicDataRepository.save(basicData);
		}
		

		long[] ids = new long[weekDays.size()];
		for (int i = 0; i < weekDays.size(); i++) {
			ids[i] = weekDays.get(i).getNumber();
		}
		user.setDayOfWeekIds(ids);
		user.setDaysOfWeek(weekDays);

		model.addAttribute("user", user);

		return "hr/newuser";

	}

	@PostMapping("/hr/newuser")
	public String newUser(@ModelAttribute(value = "user") User user, BindingResult result, ModelMap model) {

		User savedUser = userRepository.findById(user.getId());
		MultipartFile image = user.getImage();

		/*
		 * START ERROR CHECK SECTION
		 */
		User foundUser = userRepository.findByUserName(user.getUserName());
		if (foundUser != null && foundUser.getId() != user.getId()) {
			FieldError error = new FieldError("user", "userName",
					"[" + user.getUserName() + "] " + Util.LanguageBundle().getString("error.namealreadyexists"));
			result.addError(error);
			model = ctrlUtil.createModelMap(model);
			model.addAttribute("user", user);
			return "hr/newuser";
		}

		if (image != null && image.getOriginalFilename() != null
				&& FilenameUtils.getExtension(image.getOriginalFilename()).equals("") == false) {
			if (user.getImage().getSize() > (5 * 1024 * 1024)) {
				FieldError error = new FieldError("user", "image",
						"[" + image.getOriginalFilename() + "] " + Util.LanguageBundle().getString("error.filetoobig"));
				result.addError(error);
				model = ctrlUtil.createModelMap(model);
				model.addAttribute("user", user);
				return "hr/newuser";
			}
		}

		// Check if there is a try to change role of the last administrator
		if (savedUser.getRole().getRoleName().equals(Role.ROLE_ADMIN)
				&& user.getRoleName().equals(Role.ROLE_ADMIN) == false) {
			List<User> adminUsers = userRepository.findByRoleRoleName(Role.ROLE_ADMIN);
			if (adminUsers.size() <= 1 && adminUsers.get(0).getId() == user.getId()) {
				FieldError error = new FieldError("user", "roleName", "");
				result.addError(error);
				model = ctrlUtil.createModelMap(model);
				model.addAttribute("user", user);
				return "hr/newuser";
			}

		}

		/*
		 * END ERROR CHECK SECTION
		 */

		List<WeekDay> wds = new ArrayList<WeekDay>();
		for (long dayId : user.getDayOfWeekIds()) {
			WeekDay wd = weekDayRepository.findByNumber(Long.valueOf(dayId).intValue());
			wds.add(wd);
		}
		user.setDaysOfWeek(wds);

		if (user.getTeamId() >= 0) {
			Team team = teamRepository.findById(user.getTeamId());
			user.setTeam(team);
		}

		if (user.getRoleName().equals("") == false) {
			Role role = roleRepository.findByRoleName(user.getRoleName());
			user.setRole(role);
		}

		user = userRepository.save(user);

		// Image handling after entity save because the entity id is not available
		// before
		// but is needed for
		// the file name
		if (image != null && image.getOriginalFilename() != null
				&& FilenameUtils.getExtension(image.getOriginalFilename()).equals("") == false) {
			String ext = FilenameUtils.getExtension(image.getOriginalFilename());
			Util.saveImage(image, "userimage_" + user.getId() + "." + ext);
			user.setImgPath("userimage_" + user.getId() + "." + ext);
		} else if (user.getImgPath() != null && user.getImgPath().equals("") == false) {
			// do nothing
		} else {
			user.setImgPath("default_user.jpg");
		}

		userRepository.save(user);
		return "redirect:/hr/userlist";

	}

	@PostMapping("/hr/deleteuser")
	public String deleteUser(@ModelAttribute(value = "userId") long userId, BindingResult result, ModelMap model) {
		User user = userRepository.findById(userId);

		if (user.isAlreadyUsed()) {
			FieldError error = new FieldError("user", "",
					"[" + user.getUserName() + "] " + Util.LanguageBundle().getString("error.cannotbedeleted"));
			result.addError(error);
			model = ctrlUtil.createModelMap(model);
			model.addAttribute("user", user);
			return "hr/userlist";
		}

		userRepository.delete(user);
		return "redirect:/hr/userlist";
	}

	@PostMapping("/hr/enableuser")
	public String enableUser(@ModelAttribute(value = "userId") long userId) {
		User user = userRepository.findById(userId);
		user.setActive(true);
		userRepository.save(user);

		return "redirect:/hr/userlist";
	}

	@PostMapping("/hr/disableuser")
	public String disableUser(@ModelAttribute(value = "userId") long userId) {
		User user = userRepository.findById(userId);
		user.setActive(false);
		userRepository.save(user);

		return "redirect:/hr/userlist";
	}

	@GetMapping("/hr/userlist")
	public String userList(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		List<User> users = (List<User>) userRepository.findAll();
		model.addAttribute("users", users);
		return "hr/userlist";
	}

	@GetMapping("/hr/edituser/{userId}")
	public String editUser(ModelMap model, @PathVariable String userId) {
		model = ctrlUtil.createModelMap(model);
		User user = userRepository.findById(Long.parseLong(userId));
		if (user.getTeam() != null) {
			user.setTeamId(user.getTeam().getId());
		}
		if (user.getRole() != null) {
			user.setRoleName(user.getRole().getRoleName());
		}

		List<WeekDay> weekDays = user.getDaysOfWeek();
		if (weekDays != null) {
			long[] ids = new long[weekDays.size()];
			for (int i = 0; i < weekDays.size(); i++) {
				ids[i] = weekDays.get(i).getNumber();
			}
			user.setDayOfWeekIds(ids);
		}

		model.addAttribute("user", user);
		return "hr/newuser";
	}

	@GetMapping("/user/editprofile")
	public String editProfile(ModelMap model) {
		long userId = userRepository.findByUserName(ctrlUtil.getLoggedInUserName()).getId();
		model = ctrlUtil.createModelMap(model);
		User user = userRepository.findById(userId);
		if (user.getTeam() != null) {
			user.setTeamId(user.getTeam().getId());
		}
		if (user.getRole() != null) {
			user.setRoleName(user.getRole().getRoleName());
		}
		List<WeekDay> weekDays = user.getDaysOfWeek();
		if (weekDays != null) {
			long[] ids = new long[weekDays.size()];
			for (int i = 0; i < weekDays.size(); i++) {
				ids[i] = weekDays.get(i).getNumber();
			}
			user.setDayOfWeekIds(ids);
		}

		model.addAttribute("user", user);
		return "user/edituser";
	}

	@PostMapping("/user/editprofile")
	public String editProfile(@ModelAttribute(value = "user") User user, ModelMap model, BindingResult result) {
		MultipartFile image = user.getImage();

		if (image != null && image.getOriginalFilename() != null
				&& FilenameUtils.getExtension(image.getOriginalFilename()).equals("") == false) {
			if (user.getImage().getSize() > (5 * 1024 * 1024)) {
				FieldError error = new FieldError("user", "image", "");
				result.addError(error);
				model = ctrlUtil.createModelMap(model);
				model.addAttribute("user", user);
				return "hr/newuser";
			}
		}

		if (user.getTeamId() >= 0) {
			Team team = teamRepository.findById(user.getTeamId());
			user.setTeam(team);
		}

		if (user.getRoleName().equals("") == false) {
			Role role = roleRepository.findByRoleName(user.getRoleName());
			user.setRole(role);
		}

		user = userRepository.save(user);

		// Image handling after entity save because the entity id is not available
		// before
		// but is needed for
		// the file name
		if (image != null && image.getOriginalFilename() != null
				&& FilenameUtils.getExtension(image.getOriginalFilename()).equals("") == false) {
			String ext = FilenameUtils.getExtension(image.getOriginalFilename());
			Util.saveImage(image, "userimage_" + user.getId() + "." + ext);
			user.setImgPath("userimage_" + user.getId() + "." + ext);
		} else if (user.getImgPath() != null && user.getImgPath().equals("") == false) {
			// do nothing
		} else {
			user.setImgPath("default_user.jpg");
		}

		userRepository.save(user);
		return "redirect:/user/myprofile";
	}

	@GetMapping("/hr/userprofile/{userId}")
	public String userProfile(ModelMap model, @PathVariable String userId) {
		model = ctrlUtil.createModelMap(model);
		User user = userRepository.findById(Long.parseLong(userId));
		model.addAttribute("user", user);
		return "hr/userprofile";
	}

	@GetMapping("/user/myprofile")
	public String myProfile(ModelMap model) {
		long userId = userRepository.findByUserName(ctrlUtil.getLoggedInUserName()).getId();
		model = ctrlUtil.createModelMap(model);
		User user = userRepository.findById(userId);
		model.addAttribute("user", user);
		return "user/myprofile";
	}

}
