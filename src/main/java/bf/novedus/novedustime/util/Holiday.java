package bf.novedus.novedustime.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import bf.novedus.novedustime.enums.CountryAndState;
import bf.novedus.novedustime.enums.HolidayType;

public class Holiday {

	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	private final HolidayType _holidayKind;
	private int _addDays;
	private String _fixedDate;

	public String HolidayName;
	public LocalDate Date;
	public CountryAndState[] States;

	public Holiday(String holidayName, String fixedDate, CountryAndState[] states) {
		_addDays = 0;
		_holidayKind = HolidayType.Fixed;
		_fixedDate = fixedDate;

		HolidayName = holidayName;
		States = states;
	}

	public Holiday(String holidayName, int addDays, CountryAndState[] states) {
		_addDays = addDays;
		_holidayKind = HolidayType.Dynamic;

		HolidayName = holidayName;
		States = states;
	}

	public LocalDate GetDate(LocalDate easterSunday) throws ParseException {
		Date = _holidayKind != HolidayType.Fixed ? easterSunday.plusDays(_addDays)
				: Util.dateToLocalDate(sdf.parse(_fixedDate + "." + easterSunday.getYear()));
		return Date;
	}

	public String getHolidayName() {
		return HolidayName;
	}

	public void setHolidayName(String holidayName) {
		HolidayName = holidayName;
	}

	public LocalDate getDate() {
		return Date;
	}

	public void setDate(LocalDate date) {
		Date = date;
	}

	public CountryAndState[] getStates() {
		return States;
	}

	public void setStates(CountryAndState[] states) {
		States = states;
	}

}



