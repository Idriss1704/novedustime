package bf.novedus.novedustime.enums;

public enum ApproveMethod {
	ONE_OF_BOTH("approvemethod.approvemethodone"),
	BOTH_PARRALEL("approvemethod.approvemethodparallel"),
	AUTOMATIC("approvemethod.approvemethodauto"),
	BOTH_SERIAL("approvemethod.approvemethodserial");

	public final String label;

	private ApproveMethod(String label) {
		this.label = label;
	}

}
