package bf.novedus.novedustime.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import bf.novedus.novedustime.model.AdminPreferences;
import bf.novedus.novedustime.model.MinimalPause;
import bf.novedus.novedustime.model.WeekDay;
import bf.novedus.novedustime.repository.AdminPreferencesRepository;
import bf.novedus.novedustime.repository.MinimalPauseRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.repository.WeekDayRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class AdminPreferencesController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	UserRepository UserRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	AdminPreferencesRepository adminPreferencesRepository;

	@Autowired
	WeekDayRepository weekDayRepository;

	@Autowired
	MinimalPauseRepository minimalPauseRepository;

	@GetMapping("/admin/preferences")
	public String newPreferences(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		AdminPreferences preferences = ctrlUtil.getAdminPreferences();

		// week days
		List<WeekDay> weekDays = preferences.getDaysOfWeek();
		if (weekDays == null || weekDays.isEmpty()) {
			weekDays = new ArrayList<WeekDay>();
			List<WeekDay> temp = (List<WeekDay>) weekDayRepository.findAll();
			for (WeekDay d : temp) {
				if (d.getNumber() < 6) {
					weekDays.add(d);
				}
			}
			preferences.setDaysOfWeek(weekDays);
			preferences = adminPreferencesRepository.save(preferences);
		}

		long[] ids = new long[weekDays.size()];
		for (int i = 0; i < weekDays.size(); i++) {
			ids[i] = weekDays.get(i).getNumber();
		}
		preferences.setDayOfWeekIds(ids);
		preferences.setDaysOfWeek(weekDays);

		// Min Pause
		if (preferences.getMinPause1() == null) {
			MinimalPause pause = new MinimalPause(LocalTime.of(0, 0), LocalTime.of(0, 0));
			pause = minimalPauseRepository.save(pause);
			preferences.setMinPause1(pause);
		}
		if (preferences.getMinPause2() == null) {
			MinimalPause pause2 = new MinimalPause(LocalTime.of(0, 0), LocalTime.of(0, 0));
			pause2 = minimalPauseRepository.save(pause2);
			preferences.setMinPause2(pause2);
		}
		preferences = adminPreferencesRepository.save(preferences);

		// Expiration date
		LocalDate expDate = Util.dateToLocalDate(preferences.getVacationExpirationDate());
		preferences.setExpDateString(expDate.getDayOfMonth() + "." + expDate.getMonth().getValue());

		model.addAttribute("adminPreferences", preferences);
		return "admin/preferences";
	}

	@PostMapping("/admin/preferences")
	public String newPreferences(@ModelAttribute(value = "adminPreferences") AdminPreferences adminPreferences,
			BindingResult result, ModelMap model) {

		MultipartFile logo = adminPreferences.getCompanyLogo();
		MultipartFile bgImage = adminPreferences.getBgImage();

		// Weekdays
		List<WeekDay> wds = new ArrayList<WeekDay>();
		for (long dayId : adminPreferences.getDayOfWeekIds()) {
			WeekDay wd = weekDayRepository.findByNumber(Long.valueOf(dayId).intValue());
			wds.add(wd);
		}
		adminPreferences.setDaysOfWeek(wds);

		// Expiration date
		String dateString = adminPreferences.getExpDateString();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
		try {
			adminPreferences.setVacationExpirationDate(sdf.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Min Pause (save transients)
		minimalPauseRepository.save(adminPreferences.getMinPause1());
		minimalPauseRepository.save(adminPreferences.getMinPause2());

		// Handle files
		if (logo != null && logo.getOriginalFilename() != null
				&& FilenameUtils.getExtension(logo.getOriginalFilename()).equals("") == false) {
			String ext = FilenameUtils.getExtension(logo.getOriginalFilename());
			Util.saveImage(logo, "companyLogo_" + adminPreferences.getId() + "." + ext);
			adminPreferences.setCompanyLogoPath("companyLogo_" + adminPreferences.getId() + "." + ext);
		} else if (adminPreferences.getCompanyLogoPath() != null
				&& adminPreferences.getCompanyLogoPath().equals("") == false) {
			// do nothing
		} else {
			//adminPreferences.setCompanyLogoPath("companyLogo_default.jpg");
		}

		if (bgImage != null && bgImage.getOriginalFilename() != null
				&& FilenameUtils.getExtension(bgImage.getOriginalFilename()).equals("") == false) {
			String ext = FilenameUtils.getExtension(bgImage.getOriginalFilename());
			Util.saveImage(bgImage, "bgImage_" + adminPreferences.getId() + "." + ext);
			adminPreferences.setBgImagePath("bgImage_" + adminPreferences.getId() + "." + ext);
		} else if (adminPreferences.getBgImagePath() != null && adminPreferences.getBgImagePath().equals("") == false) {
			// do nothing
		} else {
			//adminPreferences.setBgImagePath("bgImage_default.jpg");
		}

		adminPreferencesRepository.save(adminPreferences);

		return "redirect:/admin/preferences";

	}

}
