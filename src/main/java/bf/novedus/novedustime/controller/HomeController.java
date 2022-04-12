package bf.novedus.novedustime.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bf.novedus.novedustime.enums.ApproveMethod;
import bf.novedus.novedustime.enums.RequestApprover;
import bf.novedus.novedustime.model.AbsenceType;
import bf.novedus.novedustime.model.AdminPreferences;
import bf.novedus.novedustime.model.WeekDay;
import bf.novedus.novedustime.repository.AbsenceDayRepository;
import bf.novedus.novedustime.repository.AbsenceRepository;
import bf.novedus.novedustime.repository.AbsenceTypeRepository;
import bf.novedus.novedustime.repository.VacationRepository;
import bf.novedus.novedustime.repository.WeekDayRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class HomeController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	AbsenceRepository absenceRepository;

	@Autowired
	AbsenceDayRepository absenceDayRepository;

	@Autowired
	VacationRepository vacationRepository;

	@Autowired
	AbsenceTypeRepository absenceTypeRepository;

	@Autowired
	WeekDayRepository weekDayRepository;

	@GetMapping("/")
	public String root(ModelMap model) {
		createVacationType();
		createWeekDays();
		ctrlUtil.updatePlannedVacation(ctrlUtil.getLoggedInUser());
		model = ctrlUtil.createModelMap(model);
		return "/common/index";
	}

	@GetMapping("/login")
	public String login(ModelMap model) {
		model.addAttribute("basicdata", ctrlUtil.getAdminPreferences());
		return "login";
	}

	private void createWeekDays() {
		List<WeekDay> days = (List<WeekDay>) weekDayRepository.findAll();
		if (days == null || days.isEmpty()) {
			for (int i = 1; i <= 7; i++) {
				WeekDay d = new WeekDay();
				d.setNumber(i);
				d.setName("daysofweek.day" + i);
				weekDayRepository.save(d);
			}
		}

	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "/error/403";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping("/viewimage/{fileName}")
	@ResponseBody
	public byte[] getImage(@PathVariable String fileName) {
		String imgFullPath = Util.EXT_IMG_RESOURCES_DIR + File.separator + fileName;
		byte[] finalImg = null;
		try {
			finalImg = Files.readAllBytes(Paths.get(imgFullPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return finalImg;
	}

	private void createVacationType() {
		AbsenceType at = absenceTypeRepository.findByVacation(true);
		if (at == null) {
			AdminPreferences bd = ctrlUtil.getAdminPreferences();
			at = new AbsenceType();
			at.setActive(true);
			at.setApproveMethod(ApproveMethod.ONE_OF_BOTH);
			at.setApprover1(RequestApprover.TEAM_LEADER);
			at.setColor("#E8A85F");
			at.setHours(bd.getDefaultDayHours());
			at.setInUse(true);
			at.setName("general.vacationname");
			at.setUserCanChange(false);
			at.setUserCanRequest(true);
			at.setVacation(true);
			absenceTypeRepository.save(at);
		}

	}

}
