package bf.novedus.novedustime.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import bf.novedus.novedustime.util.Util;

/**
 * 
 * Represents a type of presence(i.e Home office)
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class PresenceType {

	public static final String APPROVER_TEAMLEADER = "TEAM_LEADER";
	public static final String APPROVER_AUTO = "AUTOMATIC";
	public static final String APPROVER_HR = "HR";
	public static final String APPROVER_TEAMLEADERS_LEADER = "TEAM_LEADERS_LEADER";

	public static final String APPROVE_METHOD_ONEOFBOTH = "ONE_OF_BOTH";
	public static final String APPROVE_METHOD_BOTH_PARRALEL = "BOTH_PARRALEL";
	public static final String APPROVE_METHOD_BOTH_SERIAL = "BOTH_SERIAL";

	public static final long WORKINGTIME_EQUIVALENCE_DAY = -10;
	public static final long WORKINGTIME_EQUIVALENCE_HALFDAY = -20;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String name;

	private String accountID;

	private long hours;

	private boolean userCanRequest;

	private boolean userCanChange;

	@NotNull
	private String approver1;

	private String approver2;

	private String approveMethod;

	private String comment = "";

	private String color;

	private boolean active;

	private boolean inUse;

	public PresenceType() {
		super();
		color = Util.getRandomColor();
		active = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public long getHours() {
		return hours;
	}

	public void setHours(long hours) {
		this.hours = hours;
	}

	public boolean isUserCanRequest() {
		return userCanRequest;
	}

	public void setUserCanRequest(boolean userCanRequest) {
		this.userCanRequest = userCanRequest;
	}

	public boolean isUserCanChange() {
		return userCanChange;
	}

	public void setUserCanChange(boolean userCanChange) {
		this.userCanChange = userCanChange;
	}

	public String getApprover1() {
		return approver1;
	}

	public void setApprover1(String approver1) {
		this.approver1 = approver1;
	}

	public String getApprover2() {
		return approver2;
	}

	public void setApprover2(String approver2) {
		this.approver2 = approver2;
	}

	public String getApproveMethod() {
		return approveMethod;
	}

	public void setApproveMethod(String approveMethod) {
		this.approveMethod = approveMethod;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

}
