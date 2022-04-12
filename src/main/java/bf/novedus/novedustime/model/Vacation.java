package bf.novedus.novedustime.model;

import java.time.MonthDay;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import bf.novedus.novedustime.enums.VacationExpirationYear;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class Vacation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne
	@NotNull
	private User user;

	private int year;

	private long vacations;

	private long restVacations;

	private long plannedVacations;

	private Date expirationDate;

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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public long getVacations() {
		return vacations;
	}

	public void setVacations(long vacations) {
		this.vacations = vacations;
	}

	public long getRestVacations() {
		return restVacations;
	}

	public void setRestVacations(long restVacations) {
		this.restVacations = restVacations;
	}

	public long getPlannedVacations() {
		return plannedVacations;
	}

	public void setPlannedVacations(long plannedVacations) {
		this.plannedVacations = plannedVacations;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

}