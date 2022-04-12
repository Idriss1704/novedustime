package bf.novedus.novedustime.util;

import java.time.LocalDate;

import bf.novedus.novedustime.enums.CountryAndState;

public class Contants {

	public static final String ONE_APPROVER = "ONE_APPROVER";
	public static final String TWO_APPROVER = "TWO_APPROVER";
	public static final String TWO_APPROVER_ONE_BEFORE_TWO = "APPROVER1_BEFORE_APPROVER2";
	public static final String TWO_APPROVER_TWO_BEFORE_ONE = "APPROVER2_BEFORE_APPROVER1";
	
	
	public static void main(String[] args) {
		HolidayDays d = new HolidayDays();
		System.out.println(d.GetHoliday(LocalDate.of(2052, 12, 26), CountryAndState.Bayern));
	}

}
