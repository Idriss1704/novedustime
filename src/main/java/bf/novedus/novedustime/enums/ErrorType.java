package bf.novedus.novedustime.enums;

public enum ErrorType {
	
	VACATION_NOT_ENOUGHT("error.vacationnotenought"), 
	NO_ERROR("error.noerror"),
	NAME_RESERVED("error.namereserved"),
	NAME_ALREADY_EXIST("error.namealreadyexists"),
	FILE_TOO_BIG("error.filetoobig"),
	WRONG_DATE_FORMAT("error.wrongdateformat"),
	DATE_RANGE_NOT_AVAILABLE("error.daterangenotavailable"),
	NO_WORKING_DAY_SELECTED("error.noworkingdayselected"),
	
	//TODO
	
	NEGATIVE_TIME("error.negativetime"),
	MAX_TIME_EXEED("error.maxtimeexeed"),
	OVERLAPING_TIMERANGE("error.overlapingtimerange");

	public final String label;

	private ErrorType(String label) {
		this.label = label;
	}
}
