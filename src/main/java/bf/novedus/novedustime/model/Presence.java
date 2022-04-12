package bf.novedus.novedustime.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import bf.novedus.novedustime.enums.RequestStatus;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class Presence {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	private WorkingMonth workingMonth;

	@ManyToOne(fetch = FetchType.EAGER)
	private PresenceType type;

	@NotNull
	private Date date;

	@NotNull
	@Enumerated
	private RequestStatus status = RequestStatus.OPEN;

	@Transient
	private LocalDate localDate;

	private LocalTime start;

	private LocalTime end;

	@NotNull
	private Duration hours;

	private String comment = "";

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

	public WorkingMonth getWorkingMonth() {
		return workingMonth;
	}

	public void setWorkingMonth(WorkingMonth workingMonth) {
		this.workingMonth = workingMonth;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}

	public PresenceType getType() {
		return type;
	}

	public void setType(PresenceType type) {
		this.type = type;
	}

	public Duration getHours() {
		return hours;
	}

	public void setHours(Duration hours) {
		this.hours = hours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

}
