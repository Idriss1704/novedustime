package bf.novedus.novedustime.controller;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import bf.novedus.novedustime.enums.ErrorType;
import bf.novedus.novedustime.model.AdminPreferences;
import bf.novedus.novedustime.model.MinimalPause;
import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.model.UserPreferences;
import bf.novedus.novedustime.model.WorkingDay;
import bf.novedus.novedustime.model.WorkingMonth;
import bf.novedus.novedustime.repository.AbsenceDayRepository;
import bf.novedus.novedustime.repository.AdminPreferencesRepository;
import bf.novedus.novedustime.repository.ProjectRepository;
import bf.novedus.novedustime.repository.RoleRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserPreferencesRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.repository.WorkingDayRepository;
import bf.novedus.novedustime.repository.WorkingMonthRepository;
import bf.novedus.novedustime.util.Util;
import bf.novedus.novedustime.util.WorkingTimeFilter;

/**
 * Controller class for Workingtime
 * 
 * @author Idrissa Ganemtore
 *
 */
@Controller
public class WorkingTimeController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	WorkingDayRepository workingDayRepository;

	@Autowired
	WorkingMonthRepository workingMonthRepository;

	@Autowired
	UserPreferencesRepository userPreferencesRepository;

	@Autowired
	AdminPreferencesRepository adminPreferencesRepository;

	@Autowired
	AbsenceDayRepository absenceDayRepository;

	@GetMapping("/user/workingtime")
	public String workingTime(ModelMap model) {
		User user = userRepository.findByUserName(ctrlUtil.getLoggedInUserName());
		if (user == null) {
			return "redirect:/login";
		}
		model = ctrlUtil.createModelMap(model);
		model = createWorkingTimeModelMap(model, user, LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
				null);
		return "user/workingtime";

	}

	@GetMapping("/user/workingtime/{year}/{month}")
	public String workingTime2(ModelMap model, @PathVariable int year, @PathVariable int month) {

		User user = userRepository.findByUserName(ctrlUtil.getLoggedInUserName());
		if (user == null) {
			return "redirect:/login";
		}
		model = ctrlUtil.createModelMap(model);
		model = createWorkingTimeModelMap(model, user, year, month, null);
		return "user/workingtime";

	}

	@PostMapping("/user/filterworkingtime")
	public String filterWorkingTime(@ModelAttribute(value = "workingtimefilter") WorkingTimeFilter filter,
			BindingResult result, ModelMap model) {
		return "redirect:/user/workingtime/" + filter.getYear() + "/" + filter.getMonth();
	}

	@PostMapping("/user/addworkingday")
	public String addWorkingDay(@ModelAttribute(value = "workingDay") WorkingDay workingDay, BindingResult result,
			ModelMap model) {

		MinimalPause minPause1 = null;
		MinimalPause minPause2 = null;
		Duration pause = Util.LocalTimeToDuration(workingDay.getPauseTime());
		Duration totalHours = Duration.between(workingDay.getStart(), workingDay.getEnd());

		WorkingMonth workingMonth = workingMonthRepository.findById(workingDay.getWorkingMonthId());

		pause = calculatePause(ctrlUtil.getLoggedInUser(), workingDay, totalHours, pause, minPause1, minPause2);

		ErrorType error = checkWorkingDay(ctrlUtil.getLoggedInUser(), workingDay, totalHours, pause);
		if (error != ErrorType.NO_ERROR) {
			model = ctrlUtil.createModelMap(model);
			model = createWorkingTimeModelMap(model, ctrlUtil.getLoggedInUser(), workingMonth.getYear(),
					workingMonth.getMonth(), workingDay);
			model.addAttribute("error", true);
			model.addAttribute("errormessage", error.label);
			return "user/workingtime";
		}

		Duration workingHours = Duration.between(workingDay.getStart(), workingDay.getEnd()).minus(pause);

		workingDay.setPause(pause);
		workingDay.setProject(projectRepository.findById(workingDay.getProjectId()));
		workingDay.setUser(userRepository.findByUserName(ctrlUtil.getLoggedInUserName()));
		workingDay.setDate(Date.valueOf(Util.longStringTolocalDate(workingDay.getStringDate())));
		workingDay.setHours(Util.roundTo2Decimal((double) (workingHours.toMinutes()) / 60.0));

		workingDay.setWorkingMonth(workingMonth);

		workingDayRepository.save(workingDay);
		ctrlUtil.calculateAndSaveMonthHours(workingMonth);

		return "redirect:/user/workingtime/" + workingDay.getWorkingMonth().getYear() + "/"
				+ workingDay.getWorkingMonth().getMonth();
	}

	private Duration calculatePause(User user, WorkingDay workingDay, Duration totalHours, Duration pause,
			MinimalPause minPause1, MinimalPause minPause2) {

		AdminPreferences pref = ctrlUtil.getAdminPreferences();

		if (pref.getMinPause1() != null && pref.getMinPause1().getTime().equals(LocalTime.of(0, 0, 0)) == false
				&& pref.getMinPause2().getTime().equals(LocalTime.of(0, 0, 0)) == false && pref.getMinPause2() != null
				&& pref.getMinPause1().getTime().compareTo(pref.getMinPause2().getTime()) > 0) {
			minPause1 = pref.getMinPause2();
			minPause2 = pref.getMinPause1();
		} else {
			minPause1 = pref.getMinPause1();
			minPause2 = pref.getMinPause2();
		}

		LocalDate date = Util.longStringTolocalDate(workingDay.getStringDate());
		List<WorkingDay> otherBookings = workingDayRepository.findByUser_IdAndWorkingMonth_IdAndDate(user.getId(),
				workingDay.getWorkingMonthId(), Util.LocalDateToDate(date));
		WorkingDay current = workingDayRepository.findById(workingDay.getId());
		if (current == null) {
			otherBookings.add(workingDay);
		} else {
			remove(otherBookings, current);
			otherBookings.add(workingDay);
		}
		Duration otherPause = Duration.ZERO;
		Duration otherHours = Duration.ZERO;
		if (otherBookings != null && otherBookings.isEmpty() == false) {

			Collections.sort(otherBookings, new Comparator<WorkingDay>() {

				@Override
				public int compare(WorkingDay o1, WorkingDay o2) {
					if (o1.getStart().isAfter(o2.getStart()))
						return 1;
					else if (o1.getStart().isBefore(o2.getStart()))
						return -1;
					return 0;
				}
			});

			otherHours = otherHours.plus(Util.hoursToDuration(otherBookings.get(0).getHours())
					.plus(otherBookings.get(0).getPause() != null ? otherBookings.get(0).getPause() : Duration.ZERO));
			otherPause = otherPause
					.plus(otherBookings.get(0).getPause() != null ? otherBookings.get(0).getPause() : Duration.ZERO);

			if (otherBookings.size() > 1) {
				for (int i = 0; i < otherBookings.size() - 1; i++) {
					otherHours = otherHours.plus(Util.hoursToDuration(otherBookings.get(i + 1).getHours()))
							.plus(otherBookings.get(i + 1).getPause() != null ? otherBookings.get(i + 1).getPause()
									: Duration.ZERO);
					otherPause = otherPause
							.plus(Util.LocalTimeToDuration(otherBookings.get(i + 1).getStart())
									.minus(Util.LocalTimeToDuration(otherBookings.get(i).getEnd())))
							.plus(otherBookings.get(i + 1).getPause() != null ? otherBookings.get(i + 1).getPause()
									: Duration.ZERO);
				}
			}

		}
		if (!totalHours.isNegative()) {
			if (minPause1 != null && pref.getMinPause1().getTime().equals(LocalTime.of(0, 0, 0)) == false) {
				if (totalHours.plus(otherHours).compareTo(Util.LocalTimeToDuration(minPause1.getTime())) > 0
						&& pause.plus(otherPause).compareTo(Util.LocalTimeToDuration(minPause1.getMinPause())) < 0) {
					if (Util.LocalTimeToDuration(minPause1.getMinPause().minus(otherPause)).isNegative() == false)
						pause = Util.LocalTimeToDuration(minPause1.getMinPause().minus(otherPause));
				}
			}
			if (minPause2 != null && pref.getMinPause2().getTime().equals(LocalTime.of(0, 0, 0)) == false) {
				if (totalHours.plus(otherHours).compareTo(Util.LocalTimeToDuration(minPause2.getTime())) > 0
						&& pause.plus(otherPause).compareTo(Util.LocalTimeToDuration(minPause2.getMinPause())) < 0) {
					if (Util.LocalTimeToDuration(minPause2.getMinPause().minus(otherPause)).isNegative() == false)
						pause = Util.LocalTimeToDuration(minPause2.getMinPause().minus(otherPause));
				}

			}
		}
		return pause;

	}

	private void remove(List<WorkingDay> workingDays, WorkingDay workingDay) {
		for (WorkingDay day : workingDays) {
			if (day.getId() == workingDay.getId()) {
				workingDays.remove(day);
				return;
			}
		}

	}

	private ErrorType checkWorkingDay(User user, WorkingDay workingDay, Duration totalHours, Duration pause) {

		// Check time Range
		if (totalHours.isNegative()) {
			return ErrorType.NEGATIVE_TIME;
		}
		// Check max working hours
		LocalDate date = Util.longStringTolocalDate(workingDay.getStringDate());
		List<WorkingDay> otherBookings = workingDayRepository.findByUser_IdAndWorkingMonth_IdAndDate(user.getId(),
				workingDay.getWorkingMonthId(), Util.LocalDateToDate(date));
		Duration otherHours = Duration.ZERO;
		if (otherBookings != null) {
			for (WorkingDay day : otherBookings) {
				// Exclude working day that is currently edited
				if (day.getId() != workingDay.getId()) {
					otherHours = otherHours.plus(Util.hoursToDuration(day.getHours()));
					if (workingDay.getStart().isBefore(day.getEnd())) {
						if (workingDay.getEnd().isAfter(day.getStart())) {
							return ErrorType.OVERLAPING_TIMERANGE;
						}
					} else if (workingDay.getEnd().isAfter(day.getStart())) {
						if (workingDay.getStart().isBefore(day.getEnd())) {
							return ErrorType.OVERLAPING_TIMERANGE;
						}
					}
				}
			}

		}

		Double maxHours = ctrlUtil.getAdminPreferences().getMaxDailyHours();
		Double hours = Util.durationToHours(totalHours.plus(otherHours).minus(pause));
		if (hours > maxHours) {
			return ErrorType.MAX_TIME_EXEED;
		}

		return ErrorType.NO_ERROR;
	}

	@GetMapping("/user/deleteWorkingDay/{workingDayId}")
	public String deleteWorkingDay(ModelMap model, @PathVariable int workingDayId) {
		WorkingDay workingDay = workingDayRepository.findById(workingDayId);
		WorkingMonth workingMonth = workingDay.getWorkingMonth();

		if (workingDay.getUser().getUserName().equals(ctrlUtil.getLoggedInUserName())) {

			workingDayRepository.delete(workingDay);

			ctrlUtil.calculateAndSaveMonthHours(workingMonth);

		}

		return "redirect:/user/workingtime/" + workingMonth.getYear() + "/" + workingMonth.getMonth();

	}

	@GetMapping("/user/editWorkingDay/{workingDayId}")
	public String editWorkingDay(ModelMap model, @PathVariable int workingDayId) {
		WorkingDay workingDay = workingDayRepository.findById(workingDayId);
		WorkingMonth workingMonth = workingDay.getWorkingMonth();

		workingDay.setPauseTime(Util.durationToLocalTime(workingDay.getPause()));
		workingDay.setStringDate(Util
				.localDateToLongString(workingDay.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
		workingDay.setWorkingMonthId(workingMonth.getId());
		workingDay.setProjectId(workingDay.getProject().getId());
		workingDay.setUserId(userRepository.findByUserName(ctrlUtil.getLoggedInUserName()).getId());

		model = ctrlUtil.createModelMap(model);

		if (workingDay.getUser().getUserName().equals(ctrlUtil.getLoggedInUserName())) {
			model.addAttribute("workingtimefilter",
					new WorkingTimeFilter(workingMonth.getMonth(), workingMonth.getYear()));
			model.addAttribute("workingmonth", workingMonth);
			model.addAttribute("workingday", workingDay);
			model.addAttribute("edit", true);
			model.addAttribute("workingdays", workingDayRepository.findByWorkingMonth_Id(workingMonth.getId()));
			model.addAttribute("projects", projectRepository.findAll());
			model.addAttribute("opendays", ctrlUtil.getOpenWorkingDays(workingMonth));
			model.addAttribute("user", userRepository.findByUserName(ctrlUtil.getLoggedInUserName()));

			return "user/workingtime";

		}

		return "redirect:/user/workingtime/" + workingMonth.getYear() + "/" + workingMonth.getMonth();

	}

	@PostMapping("/user/finishmonth")
	public String finishMonth(@ModelAttribute(value = "workingmonthId") long workingmonthId) {
		WorkingMonth workingMonth = workingMonthRepository.findById(workingmonthId);
		workingMonth.setClosed(true);
		workingMonthRepository.save(workingMonth);
		return "redirect:/user/workingtime/" + workingMonth.getYear() + "/" + workingMonth.getMonth();
	}

	@PostMapping("/user/openmonth")
	public String openMonth(@ModelAttribute(value = "workingmonthId") long workingmonthId) {
		WorkingMonth workingMonth = workingMonthRepository.findById(workingmonthId);
		workingMonth.setClosed(false);
		workingMonthRepository.save(workingMonth);
		// TODO
		return "redirect:/user/workingtime/" + workingMonth.getYear() + "/" + workingMonth.getMonth();
	}

	private ModelMap createWorkingTimeModelMap(ModelMap model, User user, int year, int month, WorkingDay day) {
		UserPreferences preferences = userPreferencesRepository.findByUser_Id(user.getId());
		WorkingMonth workingMonth = ctrlUtil.getWorkingMonth(year, month);

		WorkingDay workingDay = day;
		if (workingDay == null) {
			workingDay = new WorkingDay();
			workingDay.setWorkingMonthId(workingMonth.getId());

			if (user != null)
				workingDay.setUserId(user.getId());

			if (preferences != null) {
				workingDay.setStart(preferences.getDayStart());
				workingDay.setEnd(preferences.getDayEnd());
				workingDay.setPauseTime(preferences.getDayPause());
			}
		}
		model.addAttribute("workingtimefilter", new WorkingTimeFilter(month, year));
		model.addAttribute("workingmonth", workingMonth);
		model.addAttribute("workingday", workingDay);
		model.addAttribute("workingdays", workingDayRepository.findByWorkingMonth_Id(workingMonth.getId()));
		model.addAttribute("absencedays",
				absenceDayRepository.findByUser_IdAndWorkingMonth_Id(user.getId(), workingMonth.getId()));
		model.addAttribute("projects", projectRepository.findAll());
		model.addAttribute("opendays", ctrlUtil.getOpenWorkingDays(workingMonth));
		model.addAttribute("user", user);
		return model;
	}

}
