package bf.novedus.novedustime.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.model.WeekDay;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.repository.WeekDayRepository;

@RestController
public class RestControllerUtil {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WeekDayRepository weekDayRepository;

	@GetMapping("getUserList")
	public List<User> getUserList(@RequestParam(value = "q", required = false) String query) {

		List<User> list = (List<User>) userRepository.findAll();

		if (StringUtils.isEmpty(query)) {
			list = Arrays.stream(list.toArray(new User[0])).limit(15).collect(Collectors.toList());
			return list;
		}

		return Arrays.stream(list.toArray(new User[0]))
				.filter(state -> state.getFullName().toLowerCase().contains(query)).limit(15)
				.collect(Collectors.toList());

	}

	@GetMapping("getWeekDays/{locale}")
	public List<WeekDay> getWeekDays(@RequestParam(value = "q", required = false) String query,
			@PathVariable String locale) {

		Locale loc = new Locale(locale);
		List<WeekDay> list = new ArrayList<WeekDay>();
		List<WeekDay> foundlist = (List<WeekDay>) weekDayRepository.findAll();
		for (WeekDay wd : foundlist) {
			wd.setDisplayName(ResourceBundle.getBundle("Messages", loc).getString(wd.getName()));
			list.add(wd);
		}

		if (StringUtils.isEmpty(query)) {
			list = Arrays.stream(list.toArray(new WeekDay[0])).limit(15).collect(Collectors.toList());
			return list;
		}
		return Arrays.stream(list.toArray(new WeekDay[0]))
				.filter(state -> state.getName().toLowerCase().contains(query)).limit(15).collect(Collectors.toList());

	}

}
