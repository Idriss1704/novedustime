package bf.novedus.novedustime.controller;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import bf.novedus.novedustime.enums.ErrorType;
import bf.novedus.novedustime.enums.VacationExpirationYear;
import bf.novedus.novedustime.enums.CountryAndState;
import bf.novedus.novedustime.model.Absence;
import bf.novedus.novedustime.model.AbsenceDay;
import bf.novedus.novedustime.model.AdminPreferences;
import bf.novedus.novedustime.model.MinimalPause;
import bf.novedus.novedustime.model.Presence;
import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.model.Vacation;
import bf.novedus.novedustime.model.WeekDay;
import bf.novedus.novedustime.model.WorkingDay;
import bf.novedus.novedustime.model.WorkingMonth;
import bf.novedus.novedustime.repository.AbsenceDayRepository;
import bf.novedus.novedustime.repository.AbsenceRepository;
import bf.novedus.novedustime.repository.AbsenceTypeRepository;
import bf.novedus.novedustime.repository.AdminPreferencesRepository;
import bf.novedus.novedustime.repository.MinimalPauseRepository;
import bf.novedus.novedustime.repository.NotificationRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.repository.VacationRepository;
import bf.novedus.novedustime.repository.WeekDayRepository;
import bf.novedus.novedustime.repository.WorkingDayRepository;
import bf.novedus.novedustime.repository.WorkingMonthRepository;
import bf.novedus.novedustime.util.DateRange;
import bf.novedus.novedustime.util.HolidayDays;
import bf.novedus.novedustime.util.Util;

@Controller
public class ControllerUtil {

	@Autowired
	UserRepository userDao;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	WorkingMonthRepository workingMonthRepository;

	@Autowired
	AdminPreferencesRepository adminPreferencesRepository;

	@Autowired
	AbsenceDayRepository absenceDayRepository;

	@Autowired
	AbsenceRepository absenceRepository;

	@Autowired
	AbsenceTypeRepository absenceTypeRepository;

	@Autowired
	WorkingDayRepository workingDayRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	VacationRepository vacationRepository;

	@Autowired
	WeekDayRepository weekDayRepository;

	@Autowired
	MinimalPauseRepository minimalPauseRepository;

	/**
	 * Return a default {@link ModelMap} for all controller methods. This
	 * {@link ModelMap} holds all parameters that are needed on every view(i.e
	 * authenticated user name)
	 * 
	 * @return {@link ModelMap}
	 * @author Idrissa Ganemtore
	 */
	public ModelMap createModelMap(ModelMap model) {
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(LocaleContextHolder.getLocale());
		User user = getLoggedInUser();

		model.addAttribute("loggedinusername", user.getUserName());
		model.addAttribute("dateFormatSymbols", dateFormatSymbols);
		model.addAttribute("loggedinuser", user);
		model.addAttribute("currentworkingmonth", getCurrentWorkingMonth());
		model.addAttribute("loggedinuserrole", user.getRole());
		model.addAttribute("loggedinuserimage", user.getImgPath());
		model.addAttribute("loggedinuserfullname", user.getFullName());
		model.addAttribute("ctrlUtil", this);
		model.addAttribute("util", new Util());
		model.addAttribute("edit", false);
		model.addAttribute("error", false);
		model.addAttribute("currentyear", LocalDate.now().getYear());
		model.addAttribute("restVacation", getAvailableVacationCount(user, Year.now().getValue()));
		model.addAttribute("plannedVacation", getPlannedVacationCount(user, Year.now().getValue()));
		model.addAttribute("currentmonth", LocalDate.now().getMonth().getValue());
		model.addAttribute("currentday", LocalDate.now().getDayOfMonth());
		model.addAttribute("currentdate", LocalDate.now());
		model.addAttribute("currentdayOfWeek", LocalDate.now().getDayOfWeek().getValue());
		model.addAttribute("adminpreferences", getAdminPreferences());
		model.addAttribute("useryears", getBookedYearsPerUser(user));
		model.addAttribute("daysOfWeek", weekDayRepository.findAll());
		model.addAttribute("newnotifications",
				notificationRepository.findByUserUserNameAndIsRead(getLoggedInUserName(), false));

		return model;
	}

	public List<String> getBookedYearsPerUser(User user) {
		List<String> years = new ArrayList<String>();
		for (int i = LocalDate.now().getYear() + 2; i >= LocalDate.now().getYear() - 2; i--) {
			years.add(i + "");
		}
		List<WorkingMonth> months = workingMonthRepository.findByUser_Id(user.getId());
		for (WorkingMonth m : months) {
			String month = m.getYear() + "";
			if (!years.contains(month)) {
				years.add(month);
			}
		}
		Collections.sort(years);
		return years;
	}

	public WorkingMonth getCurrentWorkingMonth() {
		return getWorkingMonth(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
	}

	public WorkingMonth getWorkingMonth(int year, int month) {
		User loggedinuser = userDao.findByUserName(getLoggedInUserName());
		WorkingMonth currentMonth = workingMonthRepository.findByUserIdAndMonthAndYear(loggedinuser.getId(), month,
				year);

		if (currentMonth == null) {
			WorkingMonth wm = new WorkingMonth();
			wm.setUser(loggedinuser);
			wm.setMonth(month);
			wm.setYear(year);
			currentMonth = workingMonthRepository.save(wm);
		}
		return currentMonth;
	}

	public WorkingMonth getWorkingMonth(User user, int year, int month) {
		WorkingMonth currentMonth = workingMonthRepository.findByUserIdAndMonthAndYear(user.getId(), month, year);

		if (currentMonth == null) {
			WorkingMonth wm = new WorkingMonth();
			wm.setUser(user);
			wm.setMonth(month);
			wm.setYear(year);
			currentMonth = workingMonthRepository.save(wm);
		}
		return currentMonth;
	}

	public boolean isHoliday(LocalDate date) {
		HolidayDays hd = new HolidayDays();
		CountryAndState st = ((List<AdminPreferences>) adminPreferencesRepository.findAll()).get(0).getCountryState();
		return hd.IsHoliday(date, st);
	}

	public boolean isWeekend(LocalDate date) {
		if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			return true;
		}
		return false;
	}

	public boolean isUserWorkingDay(LocalDate date, User user) {
		for (WeekDay wd : user.getDaysOfWeek()) {
			if (wd.getNumber() == date.getDayOfWeek().getValue()) {
				return true;
			}
		}

		return false;
	}

	public boolean isSunday(LocalDate date) {
		if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			return true;
		}
		return false;
	}

	public List<LocalDate> getOpenWorkingDays(WorkingMonth workingmonth) {
		List<LocalDate> dates = new ArrayList<LocalDate>();

		// List<LocalDate> bookedDates = getBookedWorkingDays(workingmonth);
		List<LocalDate> bookedDates = new ArrayList<LocalDate>();
		List<LocalDate> holidays = getHoliDays(workingmonth);
		List<WeekDay> dayOfWeeks = workingmonth.getUser().getDaysOfWeek();

		if (dayOfWeeks.isEmpty()) {
			dayOfWeeks = (List<WeekDay>) weekDayRepository.findAll();
		}

		YearMonth month = YearMonth.of(workingmonth.getYear(), workingmonth.getMonth());

		for (int i = 1; i <= month.lengthOfMonth(); i++) {
			LocalDate day = LocalDate.of(workingmonth.getYear(), workingmonth.getMonth(), i);
			if (isContained(bookedDates, day) == false && isContained(holidays, day) == false
					&& isContained(workingmonth.getUser().getDaysOfWeek(), day.getDayOfWeek().getValue())) {
				dates.add(day);
			}
		}
		return dates;
	}

	private boolean isContained(List<WeekDay> daysOfWeek, int value) {
		for (WeekDay wd : daysOfWeek) {
			if (wd.getNumber() == value) {
				return true;
			}
		}
		return false;
	}

	public boolean isContained(List<LocalDate> dates, LocalDate date) {
		for (LocalDate d : dates) {
			if (d.equals(date)) {
				return true;
			}
		}
		return false;
	}

	public boolean isContained(List<Integer> days, Integer day) {
		for (Integer d : days) {
			if (d == day) {
				return true;
			}
		}
		return false;
	}

	public double calculateAndSaveMonthHours(WorkingMonth workingMonth) {
		List<AbsenceDay> absenceDays = absenceDayRepository.findByUser_IdAndWorkingMonth_Id(getLoggedInUser().getId(),
				workingMonth.getId());
		double totalDuration = 0;
		for (WorkingDay d : workingDayRepository.findByWorkingMonth_Id(workingMonth.getId())) {
			totalDuration = totalDuration + d.getHours();
		}
		for (AbsenceDay ad : absenceDays) {
			totalDuration = totalDuration + ad.getHours();
		}
		double hours = Util.roundTo2Decimal(totalDuration);
		workingMonth.setWorkingHours(hours);
		workingMonthRepository.save(workingMonth);

		return hours;
	}

	public List<LocalDate> getBookedWorkingDays(WorkingMonth workingmonth) {
		List<LocalDate> dates = new ArrayList<LocalDate>();
		for (Presence presence : workingmonth.getPresences()) {
			dates.add(presence.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		for (AbsenceDay absenceDay : workingmonth.getAbsenceDays()) {
			dates.add(absenceDay.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		for (WorkingDay day : workingmonth.getWorkingDays()) {
			dates.add(day.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		return dates;
	}

	public List<LocalDate> getHoliDays(WorkingMonth workingmonth) {
		List<LocalDate> dates = new ArrayList<LocalDate>();
		LocalDate monthStart = LocalDate.of(workingmonth.getYear(), workingmonth.getMonth(), 1);
		LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

		for (LocalDate d : new DateRange(monthStart, monthEnd).toList()) {
			if (isHoliday(d)) {
				dates.add(d);
			}
		}
		return dates;
	}

	@SuppressWarnings("deprecation")
	public Vacation getVacationEntity(User user, int year, boolean initWithDefaultsIfNew) {
		Vacation vac = vacationRepository.findByUser_IdAndYear(user.getId(), year);
		AdminPreferences basicData = getAdminPreferences();
		if (vac == null) {

			int expYear = 9999;
			int expMonth = basicData.getVacationExpirationDate() != null
					? basicData.getVacationExpirationDate().getMonth()
					: 12;
			int expDay = basicData.getVacationExpirationDate() != null ? basicData.getVacationExpirationDate().getDay()
					: 31;

			switch (basicData.getVacationExpirationYear()) {
			case SAME_YEAR:
				expYear = year;
				break;
			case FOLLOWING_YEAR:
				expYear = year + 1;
				break;
			default:
				break;
			}

			vac = new Vacation();
			vac.setExpirationDate(Util.LocalDateToDate(LocalDate.of(expYear, expMonth, expDay)));
			vac.setPlannedVacations(0);

			if (initWithDefaultsIfNew) {
				vac.setRestVacations(basicData.getVacationsPerYear());
				vac.setVacations(basicData.getVacationsPerYear());
			} else {
				vac.setRestVacations(0);
				vac.setVacations(0);
			}

			vac.setUser(user);
			vac.setYear(year);

			vac = vacationRepository.save(vac);
		}

		return vac;
	}

	public long availableVacations(User user, int year) {
		long available = 0;
		return available;
	}

	public ErrorType updatePlannedVacation(User user) {
		List<Absence> vacations = absenceRepository.findByUser_UserNameAndType_VacationAndPlanned(user.getUserName(),
				true, true);
		for (Absence ab : vacations) {
			if (ab.getStartDate().after(new Date()) == false) {
				ab.setPlanned(false);
				List<AbsenceDay> absenceDays = absenceDayRepository.findByAbsence_Id(ab.getId());
				for (AbsenceDay day : absenceDays) {
					Vacation vac = day.getVacation();
					vac.setPlannedVacations(vac.getPlannedVacations() - 1);
					vacationRepository.save(vac);
				}
			}
			absenceRepository.save(ab);
		}

		return ErrorType.NO_ERROR;
	}

	public ErrorType deleteVacationDaysAndUpdateCount(User user, List<AbsenceDay> vacationDays,
			boolean isPlannedVacation) {

		// update planned vacation ? TODO

		for (AbsenceDay day : vacationDays) {
			Vacation vac = day.getVacation();
			if (isPlannedVacation) {
				vac.setPlannedVacations(vac.getPlannedVacations() - 1);
			}
			vac.setRestVacations(vac.getRestVacations() + 1);
			vacationRepository.save(vac);
			absenceDayRepository.delete(day);
		}

		return ErrorType.NO_ERROR;

	}

	public ErrorType addVacationDaysAndUpdateCount(User user, List<AbsenceDay> vacationDays) {
		LocalDate start = Util.dateToLocalDate(vacationDays.get(0).getDate());
		boolean planned = true;
		if (start.isBefore(LocalDate.now())) {
			planned = false;
		}
		Vacation lastYear = getVacationEntity(user, start.getYear() - 1, false);
		Vacation vacationYear = getVacationEntity(user, start.getYear(), true);
		Vacation followingYear = null;
		LocalDate lastYearExpDate = Util.dateToLocalDate(lastYear.getExpirationDate());
		LocalDate vacationYearExpDate = Util.dateToLocalDate(vacationYear.getExpirationDate());

		for (AbsenceDay day : vacationDays) {
			LocalDate date = Util.dateToLocalDate(day.getDate());
			if (isHoliday(date) == false && contains(user.getDaysOfWeek(), date.getDayOfWeek().getValue())) {
				if (date.getYear() == vacationYear.getYear()) {
					if (lastYear.getRestVacations() > 0 && lastYearExpDate.isAfter(date)) {
						lastYear.setRestVacations(lastYear.getRestVacations() - 1);
						if (planned) {
							lastYear.setPlannedVacations(lastYear.getPlannedVacations() + 1);
						}
						day.setVacation(lastYear);
						absenceDayRepository.save(day);
					} else if (vacationYear.getRestVacations() > 0) {
						vacationYear.setRestVacations(vacationYear.getRestVacations() - 1);
						if (planned) {
							vacationYear.setPlannedVacations(vacationYear.getPlannedVacations() + 1);
						}
						day.setVacation(vacationYear);
						absenceDayRepository.save(day);
					}

				} else if (date.getYear() == vacationYear.getYear() + 1) {
					followingYear = getVacationEntity(user, start.getYear() + 1, true);
					if (vacationYear.getRestVacations() > 0 && vacationYearExpDate.isAfter(date)) {
						vacationYear.setRestVacations(vacationYear.getRestVacations() - 1);
						if (planned) {
							vacationYear.setPlannedVacations(vacationYear.getPlannedVacations() + 1);
						}
						day.setVacation(vacationYear);
						absenceDayRepository.save(day);
					} else if (followingYear.getRestVacations() > 0) {
						followingYear.setRestVacations(followingYear.getRestVacations() - 1);
						if (planned) {
							followingYear.setPlannedVacations(followingYear.getPlannedVacations() + 1);
						}
						day.setVacation(followingYear);
						absenceDayRepository.save(day);
					}
				}
			}
		}
		vacationRepository.save(lastYear);
		vacationRepository.save(vacationYear);
		if (followingYear != null) {
			vacationRepository.save(followingYear);
		}
		return ErrorType.NO_ERROR;

	}

	private boolean contains(List<WeekDay> daysOfWeek, int value) {
		for (WeekDay d : daysOfWeek) {
			if (d.getNumber() == value)
				return true;
		}
		return false;
	}

	public long getAvailableVacationCount(User user, int year) {
		long count = 0;
		List<Vacation> vacs = vacationRepository.findByUser_Id(user.getId());
		if (vacs == null || vacs.isEmpty()) {
			Vacation vac = getVacationEntity(user, Year.now().getValue(), true);
			return vac.getRestVacations();
		}
		for (Vacation vac : vacs) {
			if (vac.getYear() <= year && Util.dateToLocalDate(vac.getExpirationDate()).isAfter(LocalDate.now()))
				count = count + vac.getRestVacations();
		}
		return count;
	}

	public long getPlannedVacationCount(User user, int year) {
		long count = 0;
		List<Vacation> vacs = vacationRepository.findByUser_Id(user.getId());
		if (vacs == null || vacs.isEmpty()) {
			Vacation vac = getVacationEntity(user, Year.now().getValue(), true);
			return vac.getPlannedVacations();
		}
		for (Vacation vac : vacs) {
			if (vac.getYear() <= year)
				count = count + vac.getPlannedVacations();
		}
		return count;
	}

	/**
	 * Returns the principal[user-name] of logged-in user.
	 * 
	 * @return
	 * @author Idrissa Ganemtore
	 */
	public String getLoggedInUserName() {
		String userName = "unknown";
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				userName = ((LdapUserDetails) principal).getUsername();
			} else {
				userName = principal.toString();
			}
			return userName;

		} catch (Exception e) {
			return userName;
		}

	}

	public AdminPreferences getAdminPreferences() {
		List<AdminPreferences> pref = (List<AdminPreferences>) adminPreferencesRepository.findAll();
		if (pref == null || pref.isEmpty()) {
			return createAdminPreferences();
		}
		return pref.get(0);
	}

	private AdminPreferences createAdminPreferences() {
		AdminPreferences pref = new AdminPreferences();
		pref.setDaysOfWeek((List<WeekDay>) weekDayRepository.findAll());
		pref.setVacationExpirationYear(VacationExpirationYear.NO_EXPIRATION);
		pref.setVacationExpirationDate(Util.LocalDateToDate(LocalDate.of(9999, 12, 31)));
		pref.setVacationAvailableDate(Util.LocalDateToDate(LocalDate.of(9999, 01, 01)));
		pref.setMinPause1(minimalPauseRepository.save(new MinimalPause(LocalTime.of(0, 0), LocalTime.of(0, 0))));
		pref.setMinPause2(minimalPauseRepository.save(new MinimalPause(LocalTime.of(0, 0), LocalTime.of(0, 0))));
		return adminPreferencesRepository.save(pref);

	}

	/**
	 * Returns the logged-in user.
	 * 
	 * @return
	 * @author Idrissa Ganemtore
	 */
	public User getLoggedInUser() {
		return userDao.findByUserName(getLoggedInUserName());

	}

	/**
	 * Returns the logged in user full name.
	 * 
	 * @param userdao
	 * @return
	 * @author Idrissa Ganemtore
	 */
	public String getLoggedInUserFullName(UserRepository userdao) {
		String userName = "";
		try {

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				userName = ((LdapUserDetails) principal).getUsername();
			}
			userName = userdao.findByUserName(userName).getFullName();
			return userName;

		} catch (Exception e) {
			return userName;
		}

	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	public UserDetails getUserDetails() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static String convertDateToString(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.GERMAN);
			return sdf.format(date);
		} else {
			return null;
		}

	}

}
