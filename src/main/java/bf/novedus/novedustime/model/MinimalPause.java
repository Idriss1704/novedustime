package bf.novedus.novedustime.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class MinimalPause {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private LocalTime time;

	@NotNull
	private LocalTime minPause;

	public MinimalPause() {

	}

	public MinimalPause(LocalTime time, LocalTime minPause) {
		this.time = time;
		this.minPause = minPause;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public LocalTime getMinPause() {
		return minPause;
	}

	public void setMinPause(LocalTime minPause) {
		this.minPause = minPause;
	}

}
