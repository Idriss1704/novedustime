package bf.novedus.novedustime.model;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import bf.novedus.novedustime.enums.RequestStatus;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class Absence {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	private AbsenceType type;

	@OneToMany(mappedBy = "absence", fetch = FetchType.EAGER)
	private List<AbsenceDay> absenceDays;

	@NotNull
	private Date date = new Date();

	@NotNull
	@Enumerated(EnumType.STRING)
	private RequestStatus status = RequestStatus.OPEN;

	private Date startDate;

	private Date endDate;

	private boolean planned;

	private LocalTime startTime = LocalTime.of(0, 0);

	private LocalTime endTime = LocalTime.of(23, 59);

	private long workingsDays;

	private String comment = "";

	@Transient
	private String dateRange;

	@Transient
	private long absenceTypeId;

	@Transient
	private String stringHours;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AbsenceType getType() {
		return type;
	}

	public void setType(AbsenceType type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getAbsenceTypeId() {
		return absenceTypeId;
	}

	public void setAbsenceTypeId(long absenceTypeId) {
		this.absenceTypeId = absenceTypeId;
	}

	public long getWorkingsDays() {
		return workingsDays;
	}

	public void setWorkingsDays(long workingsDays) {
		this.workingsDays = workingsDays;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public List<AbsenceDay> getAbsenceDays() {
		return absenceDays;
	}

	public void setAbsenceDays(List<AbsenceDay> absenceDays) {
		this.absenceDays = absenceDays;
	}

	public String getStringHours() {
		return stringHours;
	}

	public void setStringHours(String stringHours) {
		this.stringHours = stringHours;
	}

	public boolean isPlanned() {
		return planned;
	}

	public void setPlanned(boolean planned) {
		this.planned = planned;
	}

}
