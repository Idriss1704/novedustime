package bf.novedus.novedustime.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;

public class Util {

	public static final String DATE_PATTERN = "dd.MM.yyyy";
	public final static String DATE_PATTERN_LONG = "EEE, dd.MM.yyyy";
	public static final String TIME_PATTERN = "HH:mm";

	public static final String EXT_IMG_RESOURCES_DIR = System.getProperty("user.dir") + "\\..\\resources\\images";

	public static String getRandomColor() {
		Random obj = new Random();
		int rand_num = obj.nextInt(0xffffff + 1);
		// format it as hexadecimal string and print
		String colorCode = String.format("#%06x", rand_num);
		return colorCode;
	}

	public static String saveImage(MultipartFile file, String fileName) {
		try {
			file.transferTo(new File(EXT_IMG_RESOURCES_DIR + File.separator + fileName));
			return fileName;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static double durationToHours(Duration duration) {
		long minutes = duration.toMinutes();
		double hours = minutes / 60;
		int tmp = (int) Math.round(hours * 100);
		return tmp / 100;
	}

	public static Duration hoursToDuration(double hours) {
		long minutes = (long) (hours * 60);
		return Duration.ofMinutes(minutes);
	}

	public static Duration LocalTimeToDuration(LocalTime time) {
		Duration duration = Duration.ofHours(time.getHour()).plus(Duration.ofMinutes(time.getMinute()))
				.plus(Duration.ofSeconds(time.getSecond()));
		return duration;
	}

	/**
	 * 
	 * @return Resouce bundle containing the messages in the current set language
	 * @author Idrissa Ganemtore
	 */
	public static ResourceBundle LanguageBundle() {
		Locale locale = LocaleContextHolder.getLocale();
		return ResourceBundle.getBundle("messages", locale);

	}

	public static String localDateToString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN, LocaleContextHolder.getLocale()));
	}

	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		return sdf.format(date);
	}

	public static String dateToLongString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_LONG);
		return sdf.format(date);
	}

	public static LocalDate dateToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static String getCurrentZone() {
		return ZoneId.systemDefault().toString();
	}

	public static Date LocalDateToDate(LocalDate date) {
		return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalTime durationToLocalTime(Duration duration) {
		int hours = (int) duration.toHours();
		int minutes = (int) duration.minus(Duration.ofHours(hours)).toMinutes();
		return LocalTime.of(hours, minutes);
	}

	public static LocalDate stringTolocalDate(String input) {
		return LocalDate.parse(input, DateTimeFormatter.ofPattern(DATE_PATTERN, LocaleContextHolder.getLocale()));
	}

	public static String localDateToLongString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN_LONG, LocaleContextHolder.getLocale()));
	}

	public static LocalDate longStringTolocalDate(String input) {
		return LocalDate.parse(input, DateTimeFormatter.ofPattern(DATE_PATTERN_LONG, LocaleContextHolder.getLocale()));
	}

	public static String localTimeToString(LocalTime time) {
		return time.format(DateTimeFormatter.ofPattern(TIME_PATTERN, LocaleContextHolder.getLocale()));
	}

	public static LocalTime stringTolocalTime(String input) {
		return LocalTime.parse(input, DateTimeFormatter.ofPattern(TIME_PATTERN, LocaleContextHolder.getLocale()));
	}

	public static double roundTo2Decimal(double input) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		String value = decimalFormat.format(input);
		return Double.parseDouble(value.replace(",", "."));
	}

}
