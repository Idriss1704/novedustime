package bf.novedus.novedustime.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author Idrissa Ganemtore
 */

@Entity
public class Role {

	public final static String ROLE_ADMIN = "ROLE_ADMIN";
	public final static String ROLE_USER = "ROLE_USER";
	public final static String ROLE_HR = "ROLE_HR";
	public final static String ROLE_TEAMLEAD = "ROLE_TEAMLEAD";

	public final static int ROLE_RANK_ADMIN = 40;
	public final static int ROLE_RANK_USER = 10;
	public final static int ROLE_RANK_HR = 30;
	public final static int ROLE_RANK_TEAMLEAD = 20;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(unique = true)
	private String roleName;

	@OneToMany(mappedBy = "role")
	private List<User> user;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public int getRank() {
		return getRank(roleName);
	}

	public int getRank(String roleName) {
		switch (roleName) {
		case ROLE_USER:
			return ROLE_RANK_USER;
		case ROLE_TEAMLEAD:
			return ROLE_RANK_TEAMLEAD;
		case ROLE_HR:
			return ROLE_RANK_HR;
		case ROLE_ADMIN:
			return ROLE_RANK_ADMIN;

		default:
			return 0;
		}
	}

}
