package bf.novedus.novedustime.enums;

public enum RequestApprover {
	TEAM_LEADER("general.approverhr"), HR("general.approverteamleader"),
	TEAM_LEADERS_LEADER("general.approverteamleadersleader");

	public final String label;

	private RequestApprover(String label) {
		this.label = label;
	}
}
