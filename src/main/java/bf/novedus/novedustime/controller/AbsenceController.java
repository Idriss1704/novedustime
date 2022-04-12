package bf.novedus.novedustime.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import bf.novedus.novedustime.enums.RequestStatus;
import bf.novedus.novedustime.model.Absence;
import bf.novedus.novedustime.model.AbsenceDay;
import bf.novedus.novedustime.model.AbsenceType;
import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.model.WorkingDay;
import bf.novedus.novedustime.model.WorkingMonth;
import bf.novedus.novedustime.repository.AbsenceDayRepository;
import bf.novedus.novedustime.repository.AbsenceRepository;
import bf.novedus.novedustime.repository.AbsenceTypeRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.repository.WorkingDayRepository;
import bf.novedus.novedustime.util.DateRange;
import bf.novedus.novedustime.util.Util;

@Controller
public class AbsenceController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	AbsenceTypeRepository absenceTypeRepository;

	@Autowired
	AbsenceRepository absenceRepository;

	@Autowired
	AbsenceDayRepository absenceDayRepository;

	@Autowired
	WorkingDayRepository workingDayRepository;

	@GetMapping("/user/requestabsence")
	public String resquestAbsenceGet(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		List<AbsenceType> absencesTypes = (List<AbsenceType>) absenceTypeRepository.findAll();
		List<Absence> absences = (List<Absence>) absenceRepository.findByUser_UserName(ctrlUtil.getLoggedInUserName());

		model.addAttribute("absencetypes", absencesTypes);
		model.addAttribute("absence", new Absence());
		model.addAttribute("absences", absences);

		return "user/requestabsence";
	}

	@GetMapping("/user/deleteabsence/{id}")
	public String deleteAbsence(ModelMap model, @PathVariable long id) {
		Absence absence = absenceRepository.findById(id);

		performDeleteAbsenceChecks(absence);

		List<WorkingMonth> workingMonths = new ArrayList<WorkingMonth>();

		User user = ctrlUtil.getLoggedInUser();

		if (ctrlUtil.getLoggedInUser().getId() != absence.getUser().getId()) {
			return "redirect:/user/requestabsence";
		}
		List<AbsenceDay> absenceDays = absenceDayRepository.findByAbsence_Id(absence.getId());

		long lastWorkingMonthId = -10;
		for (AbsenceDay ad : absenceDays) {

			WorkingMonth workingMonth = ctrlUtil.getWorkingMonth(Util.dateToLocalDate(ad.getDate()).getYear(),
					Util.dateToLocalDate(ad.getDate()).getMonthValue());
			if (workingMonth.getId() != lastWorkingMonthId) {
				workingMonths.add(workingMonth);
				lastWorkingMonthId = workingMonth.getId();
			}
			if (absence.getType().isVacation() == false) {
				absenceDayRepository.delete(ad);
			}
		}

		if (absence.getType().isVacation()) {
			ctrlUtil.deleteVacationDaysAndUpdateCount(user, absenceDays, absence.isPlanned());
		}
		absenceRepository.delete(absence);

		for (WorkingMonth m : workingMonths) {
			ctrlUtil.calculateAndSaveMonthHours(m);
		}

		return "redirect:/user/requestabsence";

	}

	@PostMapping("/user/requestabsence")
	public String resquestAbsencePost(@ModelAttribute(value = "absence") Absence absence, BindingResult result,
			ModelMap model) {
		User user = userRepository.findByUserName(ctrlUtil.getLoggedInUserName());

		ErrorType error = performRequestAbsenceChecks(absence, user);
		if (error.equals(ErrorType.NO_ERROR) == false) {

			List<AbsenceType> absencesTypes = (List<AbsenceType>) absenceTypeRepository.findAll();
			List<Absence> absences = (List<Absence>) absenceRepository
					.findByUser_UserName(ctrlUtil.getLoggedInUserName());

			model = ctrlUtil.createModelMap(model);
			model.addAttribute("absencetypes", absencesTypes);
			model.addAttribute("absence", absence);
			model.addAttribute("absences", absences);
			model.addAttribute("error", true);
			model.addAttribute("errormessage", error.label);

			return "user/requestabsence";
		}

		List<AbsenceDay> absenceDays = new ArrayList<AbsenceDay>();
		List<WorkingMonth> workingMonths = new ArrayList<WorkingMonth>();

		AbsenceType absenceType = absenceTypeRepository.findById(absence.getAbsenceTypeId());
		if (absenceType.isInUse() == false) {
			absenceType.setInUse(true);
			absenceTypeRepository.save(absenceType);
		}

		absence.setDate(new Date());
		absence.setType(absenceType);

		LocalDate start = Util.stringTolocalDate(absence.getDateRange().split("-")[0].trim());
		absence.setStartDate(Util.LocalDateToDate(start));

		LocalDate end = Util.stringTolocalDate(absence.getDateRange().split("-")[1].trim());
		absence.setEndDate(Util.LocalDateToDate(end));

		absence.setStatus(RequestStatus.OPEN);
		absence.setUser(user);

		// In order to get the ID
		absence = absenceRepository.save(absence);

		long workingDayCount = 0;
		long lastWorkingMonthId = -10;
		for (LocalDate date : new DateRange(start, end).toList()) {
			if (ctrlUtil.isHoliday(date) || ctrlUtil.isUserWorkingDay(date, user) == false) {
				continue;
			}
			workingDayCount++;
			WorkingMonth workingMonth = ctrlUtil.getWorkingMonth(date.getYear(), date.getMonthValue());
			if (workingMonth.getId() != lastWorkingMonthId) {
				workingMonths.add(workingMonth);
				lastWorkingMonthId = workingMonth.getId();
			}
			AbsenceDay ad = new AbsenceDay();
			ad.setDate(Util.LocalDateToDate(date));
			if (absence.getStringHours() != null && absence.getType().isUserCanChange()) {
				double hours = Double.parseDouble(absence.getStringHours().replace(",", "."));
				ad.setHours(hours);
			} else {
				ad.setHours(absence.getType().getHours());
			}
			ad.setUser(user);
			ad.setWorkingMonth(workingMonth);
			ad.setType(absence.getType());
			ad.setAbsence(absence);

			ad = absenceDayRepository.save(ad);
			absenceDays.add(ad);

		}

		for (WorkingMonth m : workingMonths) {
			ctrlUtil.calculateAndSaveMonthHours(m);
		}

		if (absence.getType().isVacation()) {
			ctrlUtil.addVacationDaysAndUpdateCount(user, absenceDays);
		}

		absence.setAbsenceDays(absenceDays);
		if (start.isAfter(LocalDate.now())) {
			absence.setPlanned(true);
		}
		absence.setWorkingsDays(workingDayCount);

		userRepository.save(user);

		absenceRepository.save(absence);

		return "redirect:/user/requestabsence";

	}

	private ErrorType performRequestAbsenceChecks(Absence absence, User user) {

		LocalDate start = Util.stringTolocalDate(absence.getDateRange().split("-")[0].trim());
		LocalDate end = Util.stringTolocalDate(absence.getDateRange().split("-")[1].trim());
		AbsenceType absenceType = absenceTypeRepository.findById(absence.getAbsenceTypeId());

		// Date format check
		if (start == null || end == null) {
			return ErrorType.WRONG_DATE_FORMAT;
		}

		// Check if working day in requested range
		List<WorkingDay> workingDays = workingDayRepository.findByUser_IdAndDateBetween(user.getId(),
				Util.LocalDateToDate(start), Util.LocalDateToDate(end));
		if (workingDays != null && workingDays.isEmpty() == false) {
			return ErrorType.DATE_RANGE_NOT_AVAILABLE;
		}

		// Check if absence day in requested range
		List<AbsenceDay> absenceDays = absenceDayRepository.findByUser_IdAndDateBetween(user.getId(),
				Util.LocalDateToDate(start), Util.LocalDateToDate(end));
		if (absenceDays != null && absenceDays.isEmpty() == false) {
			return ErrorType.DATE_RANGE_NOT_AVAILABLE;
		}

		long workingDayCount = 0;
		for (LocalDate date : new DateRange(start, end).toList()) {
			if (ctrlUtil.isHoliday(date) || ctrlUtil.isUserWorkingDay(date, user) == false) {
				continue;
			}

			workingDayCount++;
		}
		if (absenceType.isVacation() && user.getRestVacation() < workingDayCount) {
			return ErrorType.VACATION_NOT_ENOUGHT;
		}

		if (workingDayCount == 0) {
			return ErrorType.NO_WORKING_DAY_SELECTED;
		}

		return ErrorType.NO_ERROR;
	}

	private void performDeleteAbsenceChecks(Absence absence) {
		// TODO Auto-generated method stub

	}

}
