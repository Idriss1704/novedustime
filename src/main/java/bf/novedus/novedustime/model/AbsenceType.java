package bf.novedus.novedustime.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import bf.novedus.novedustime.enums.ApproveMethod;
import bf.novedus.novedustime.enums.RequestApprover;
import bf.novedus.novedustime.util.Util;

/**
 * 
 * Represents a type of absence (i.e Vacation)
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class AbsenceType {

	public static final long WORKINGTIME_EQUIVALENCE_DAY = -10;
	public static final long WORKINGTIME_EQUIVALENCE_HALFDAY = -20;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String name;

	private String accountID;

	@NotNull
	private double hours;

	private boolean userCanRequest;

	private boolean userCanChange;

	private boolean vacation;

	private boolean active;

	private boolean inUse;

	@NotNull
	@Enumerated(EnumType.STRING)
	private RequestApprover approver1;

	@Enumerated(EnumType.STRING)
	private RequestApprover approver2;

	@Enumerated(EnumType.STRING)
	private ApproveMethod approveMethod;

	private String color;

	private String comment = "";

	public AbsenceType() {
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
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

	public boolean isVacation() {
		return vacation;
	}

	public void setVacation(boolean isVacation) {
		this.vacation = isVacation;
	}

	public void setUserCanChange(boolean userCanChange) {
		this.userCanChange = userCanChange;
	}

	public RequestApprover getApprover1() {
		return approver1;
	}

	public void setApprover1(RequestApprover approver1) {
		this.approver1 = approver1;
	}

	public RequestApprover getApprover2() {
		return approver2;
	}

	public void setApprover2(RequestApprover approver2) {
		this.approver2 = approver2;
	}

	public ApproveMethod getApproveMethod() {
		return approveMethod;
	}

	public void setApproveMethod(ApproveMethod approveMethod) {
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
