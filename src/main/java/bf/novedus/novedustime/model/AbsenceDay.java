package bf.novedus.novedustime.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class AbsenceDay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private Date date;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	private Absence absence;

	@ManyToOne(fetch = FetchType.EAGER)
	private Vacation Vacation;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private AbsenceType type;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private WorkingMonth workingMonth;

//	private LocalTime startTime;
//
//	private LocalTime endTime;

	private double hours;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AbsenceType getType() {
		return type;
	}

	public void setType(AbsenceType type) {
		this.type = type;
	}

	public WorkingMonth getWorkingMonth() {
		return workingMonth;
	}

	public void setWorkingMonth(WorkingMonth workingMonth) {
		this.workingMonth = workingMonth;
	}

//	public LocalTime getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(LocalTime startTime) {
//		this.startTime = startTime;
//	}
//
//	public LocalTime getEndTime() {
//		return endTime;
//	}
//
//	public void setEndTime(LocalTime endTime) {
//		this.endTime = endTime;
//	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public Absence getAbsence() {
		return absence;
	}

	public void setAbsence(Absence absence) {
		this.absence = absence;
	}

	public Vacation getVacation() {
		return Vacation;
	}

	public void setVacation(Vacation vacation) {
		Vacation = vacation;
	}

}
