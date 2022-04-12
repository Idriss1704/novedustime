package bf.novedus.novedustime.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(unique = true)
	private String userName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@JsonIgnore
	@NotNull
	private Role role;

	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	private String email;

	private String gender;

	private String title;

	private String telephone = "";

	@ManyToMany
	@NotNull
	private List<WeekDay> daysOfWeek;

	@Transient
	private long[] dayOfWeekIds;

	private double dayHours;

	private long vacationCount;

	private long restVacation;

	private double overtime;

	private long plannedVacation;

	private long restVacationLastYear;

	private String imgPath;

	private String comment = "";

	private boolean active;

	private boolean alreadyUsed;

	@Transient
	private MultipartFile image;

	@Transient
	private long teamId = -10;

	@Transient
	private String roleName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private Team team;

	public User() {
		active = true;
	}

	public double getDayHours() {
		return dayHours;
	}

	public void setDayHours(double dayHours) {
		this.dayHours = dayHours;
	}

	public long getVacationCount() {
		return vacationCount;
	}

	public void setVacationCount(long vacationCount) {
		this.vacationCount = vacationCount;
	}

	public long getRestVacation() {
		return restVacation;
	}

	public void setRestVacation(long restVacation) {
		this.restVacation = restVacation;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean isActive) {
		this.active = isActive;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public boolean isAlreadyUsed() {
		return alreadyUsed;
	}

	public void setAlreadyUsed(boolean alreadyUsed) {
		this.alreadyUsed = alreadyUsed;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public long getRestVacationLastYear() {
		return restVacationLastYear;
	}

	public void setRestVacationLastYear(long restVacationLastYear) {
		this.restVacationLastYear = restVacationLastYear;
	}

	public long getPlannedVacation() {
		return plannedVacation;
	}

	public void setPlannedVacation(long plannedVacation) {
		this.plannedVacation = plannedVacation;
	}

	public double getOvertime() {
		return overtime;
	}

	public void setOvertime(double overtime) {
		this.overtime = overtime;
	}

	public long[] getDayOfWeekIds() {
		return dayOfWeekIds;
	}

	public void setDayOfWeekIds(long[] dayOfWeekIds) {
		this.dayOfWeekIds = dayOfWeekIds;
	}

	public List<WeekDay> getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(List<WeekDay> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

}