package bf.novedus.novedustime.model;

import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class UserPreferences {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	private LocalTime dayStart;

	private LocalTime dayEnd;

	private LocalTime dayPause;

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

	public LocalTime getDayStart() {
		return dayStart;
	}

	public void setDayStart(LocalTime dayStart) {
		this.dayStart = dayStart;
	}

	public LocalTime getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(LocalTime dayEnd) {
		this.dayEnd = dayEnd;
	}

	public LocalTime getDayPause() {
		return dayPause;
	}

	public void setDayPause(LocalTime dayPause) {
		this.dayPause = dayPause;
	}

}
