package bf.novedus.novedustime.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.springframework.web.multipart.MultipartFile;

import bf.novedus.novedustime.enums.CountryAndState;
import bf.novedus.novedustime.enums.VacationExpirationYear;

@Entity
public class AdminPreferences {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String companyName = "";

	private double defaultDayHours;

	private int vacationsPerYear;

	private double maxDailyHours;

	@Enumerated(EnumType.STRING)
	private CountryAndState countryState;

	@ManyToMany
	private List<WeekDay> daysOfWeek;

	@OneToOne(fetch = FetchType.EAGER)
	private MinimalPause minPause1;

	@OneToOne(fetch = FetchType.EAGER)
	private MinimalPause minPause2;

	private String companyEmail = "";

	private String companyLogoPath = "";

	private String companyTel = "";

	private String companyAddress = "";

	private String bgImagePath = "";

	private Date vacationExpirationDate;

	private Date vacationAvailableDate;

	@Enumerated(EnumType.STRING)
	private VacationExpirationYear vacationExpirationYear;

	@Transient
	private MultipartFile bgImage;

	@Transient
	private MultipartFile companyLogo;

	@Transient
	private long[] dayOfWeekIds;

	@Transient
	private String expDateString;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public double getDefaultDayHours() {
		return defaultDayHours;
	}

	public void setDefaultDayHours(double defaultDayHours) {
		this.defaultDayHours = defaultDayHours;
	}

	public List<WeekDay> getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(List<WeekDay> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyLogoPath() {
		return companyLogoPath;
	}

	public void setCompanyLogoPath(String companyLogoPath) {
		this.companyLogoPath = companyLogoPath;
	}

	public MultipartFile getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(MultipartFile companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public Date getVacationExpirationDate() {
		return vacationExpirationDate;
	}

	public void setVacationExpirationDate(Date vacationExpirationDate) {
		this.vacationExpirationDate = vacationExpirationDate;
	}

	public VacationExpirationYear getVacationExpirationYear() {
		return vacationExpirationYear;
	}

	public void setVacationExpirationYear(VacationExpirationYear vacationExpirationYear) {
		this.vacationExpirationYear = vacationExpirationYear;
	}

	public String getBgImagePath() {
		return bgImagePath;
	}

	public void setBgImagePath(String bgImagePath) {
		this.bgImagePath = bgImagePath;
	}

	public MultipartFile getBgImage() {
		return bgImage;
	}

	public void setBgImage(MultipartFile bgImage) {
		this.bgImage = bgImage;
	}

	public CountryAndState getCountryState() {
		return countryState;
	}

	public void setCountryState(CountryAndState countryState) {
		this.countryState = countryState;
	}

	public int getVacationsPerYear() {
		return vacationsPerYear;
	}

	public void setVacationsPerYear(int vacationsPerYear) {
		this.vacationsPerYear = vacationsPerYear;
	}

	public double getMaxDailyHours() {
		return maxDailyHours;
	}

	public void setMaxDailyHours(double maxDailyHours) {
		this.maxDailyHours = maxDailyHours;
	}

	public MinimalPause getMinPause1() {
		return minPause1;
	}

	public void setMinPause1(MinimalPause minPause1) {
		this.minPause1 = minPause1;
	}

	public MinimalPause getMinPause2() {
		return minPause2;
	}

	public void setMinPause2(MinimalPause minPause2) {
		this.minPause2 = minPause2;
	}

	public Date getVacationAvailableDate() {
		return vacationAvailableDate;
	}

	public void setVacationAvailableDate(Date vacationAvailableDate) {
		this.vacationAvailableDate = vacationAvailableDate;
	}

	public long[] getDayOfWeekIds() {
		return dayOfWeekIds;
	}

	public void setDayOfWeekIds(long[] dayOfWeekIds) {
		this.dayOfWeekIds = dayOfWeekIds;
	}

	public String getExpDateString() {
		return expDateString;
	}

	public void setExpDateString(String expDateString) {
		this.expDateString = expDateString;
	}

}