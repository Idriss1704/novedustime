package bf.novedus.novedustime.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class WorkingDay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@Transient
	private long userId;

	@ManyToOne(fetch = FetchType.EAGER)
	private WorkingMonth workingMonth;

	@Transient
	private long workingMonthId;  

	@NotNull
	private Date date;

	private LocalTime start;

	private LocalTime end;

	private Duration pause;

	private double hours;

	private String comment = "";

	@OneToOne(fetch = FetchType.EAGER)
	private Project project;

	@Transient
	private long projectId;

	@Transient
	private String stringDate;

	@Transient
	private LocalTime pauseTime;

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

	public Duration getPause() {
		return pause;
	}

	public void setPause(Duration pause) {
		this.pause = pause;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getWorkingMonthId() {
		return workingMonthId;
	}

	public void setWorkingMonthId(long workingMonthId) {
		this.workingMonthId = workingMonthId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public LocalTime getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(LocalTime pauseTime) {
		this.pauseTime = pauseTime;
	}

	public String getStringDate() {
		return stringDate;
	}

	public void setStringDate(String stringDate) {
		this.stringDate = stringDate;
	}

}
