package bf.novedus.novedustime.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class WorkingMonth {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private int month;

	@NotNull
	private int year;

	@ManyToOne
	@NotNull
	private User user;

	@OneToMany(mappedBy = "workingMonth")
	private List<WorkingDay> workingDays = new ArrayList<WorkingDay>();

	@OneToMany(mappedBy = "workingMonth")
	private List<AbsenceDay> absenceDays = new ArrayList<AbsenceDay>();

	@OneToMany(mappedBy = "workingMonth")
	private List<Presence> presences = new ArrayList<Presence>();

	private double workingHours;

	private boolean closed;

	private boolean approved;

	private String notification;

	private String comment = "";

	public WorkingMonth() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<WorkingDay> getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(List<WorkingDay> workingDays) {
		this.workingDays = workingDays;
	}

	public List<AbsenceDay> getAbsenceDays() {
		return absenceDays;
	}

	public void setAbsenceDays(List<AbsenceDay> absenceDays) {
		this.absenceDays = absenceDays;
	}

	public List<Presence> getPresences() {
		return presences;
	}

	public void setPresences(List<Presence> presences) {
		this.presences = presences;
	}

	public double getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(double workingHours) {
		this.workingHours = workingHours;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

}
