package bf.novedus.novedustime.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bf.novedus.novedustime.enums.CountryAndState;

/**
 * Don�t worry about the style ;-)
 * It was converted from a c# class.
 * @author Idrissa Ganemtore
 *
 */
public class HolidayDays {

	private List<Holiday> _holidays = new ArrayList<Holiday>();

	public HolidayDays() {
		Initialize();
	}

	public String GetHoliday(LocalDate date, CountryAndState state) {
		for (Holiday holiday : _holidays) {
			try {
				if (date.equals(holiday.GetDate(GetEasterSunday(date.getYear())))) {
					if (Arrays.asList(holiday.States).contains(state))
						return holiday.HolidayName;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";
	}

	public boolean IsHoliday(LocalDate date, CountryAndState state) {
		return (GetHoliday(date, state).length() > 0);
	}

	private void Initialize() {
		CountryAndState[] all = { CountryAndState.BadenWuertenberg, CountryAndState.Bayern, CountryAndState.Berlin, CountryAndState.Brandenburg, CountryAndState.Bremen,
				CountryAndState.Hamburg, CountryAndState.Hessen, CountryAndState.MecklenburgVorpommern, CountryAndState.Niedersachsen, CountryAndState.NordrheinWestfalen,
				CountryAndState.RheinlandPfalz, CountryAndState.Saarland, CountryAndState.Sachsen, CountryAndState.SachsenAnhalt, CountryAndState.SchleswigHolstein,
				CountryAndState.Thueringen };

		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.newyear"), "01.01", all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.epiphany"), "06.01",
				new CountryAndState[] { CountryAndState.BadenWuertenberg, CountryAndState.Bayern, CountryAndState.SachsenAnhalt }));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.goodfriday"), -2, all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.easter"), 0, all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.eastermonday"), 1, all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.labourday"), "01.05", all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.ascension"), 39, all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.whitsun"), 49, all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.whitmonday"), 50, all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.corpuschristi"), 60, new CountryAndState[] { CountryAndState.BadenWuertenberg, CountryAndState.Bayern, CountryAndState.Hessen,
				CountryAndState.NordrheinWestfalen, CountryAndState.RheinlandPfalz, CountryAndState.Saarland }));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.assumption"), "15.08", new CountryAndState[] { CountryAndState.Saarland }));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.germanunity"), "03.10", all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.allsaint"), "01.11", new CountryAndState[] { CountryAndState.BadenWuertenberg, CountryAndState.Bayern,
				CountryAndState.NordrheinWestfalen, CountryAndState.RheinlandPfalz, CountryAndState.Saarland }));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.chrismas"), "25.12", all));
		_holidays.add(new Holiday(Util.LanguageBundle().getString("holidaynames.chrismas2"), "26.12", all));
	}

	private static LocalDate GetEasterSunday(int year) {
		int c = year / 100;
		int n = year - 19 * (year / 19);
		int k = (c - 17) / 25;
		int i = c - c / 4 - ((c - k) / 3) + 19 * n + 15;
		i = i - 30 * (i / 30);
		i = i - (i / 28) * ((1 - (i / 28)) * (29 / (i + 1)) * ((21 - n) / 11));
		int j = year + (year / 4) + i + 2 - c + (c / 4);
		j = j - 7 * (j / 7);
		int l = i - j;

		int easterMonth = 3 + ((l + 40) / 44);
		int easterDay = l + 28 - 31 * (easterMonth / 4);

		return LocalDate.of(year, easterMonth, easterDay);

	}

}
