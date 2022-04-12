package bf.novedus.novedustime.enums;

public enum VacationExpirationYear {
	SAME_YEAR("general.vacexpirationsame"), FOLLOWING_YEAR("general.vacexpirationfollowing"),
	NO_EXPIRATION("general.vacexpirationno");

	public final String label;

	private VacationExpirationYear(String label) {
		this.label = label;
	}
}
