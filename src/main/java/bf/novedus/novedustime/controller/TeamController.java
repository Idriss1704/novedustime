package bf.novedus.novedustime.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import bf.novedus.novedustime.model.Role;
import bf.novedus.novedustime.model.Team;
import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.repository.RoleRepository;
import bf.novedus.novedustime.repository.TeamRepository;
import bf.novedus.novedustime.repository.UserRepository;
import bf.novedus.novedustime.util.Util;

@Controller
public class TeamController {

	@Autowired
	ControllerUtil ctrlUtil;

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/hr/newteam")
	public String newTeam(ModelMap model) {
		model = ctrlUtil.createModelMap(model);

		Team team = new Team();
		model.addAttribute("team", team);

		return "hr/newteam";

	}

	@SuppressWarnings({ "unlikely-arg-type" })
	@PostMapping("/hr/newteam")
	public String newTeam(@ModelAttribute(value = "team") Team team, BindingResult result, ModelMap model) {

		Team savedTeam = teamRepository.findById(team.getId());
		MultipartFile logo = team.getLogo();

		/*
		 * START ERROR CHECK SECTION
		 */
		Team foundTeam = teamRepository.findByName(team.getName());
		if (foundTeam != null && foundTeam.getId() != team.getId()) {
			FieldError error = new FieldError("team", "name", "["+ team.getName()+ "] "+ Util.LanguageBundle().getString("error.namealreadyexists"));
			result.addError(error);
			model = ctrlUtil.createModelMap(model);
			model.addAttribute("team", team);
			return "hr/newteam";
		}

		if (logo != null && logo.getOriginalFilename() != null
				&& FilenameUtils.getExtension(logo.getOriginalFilename()).equals("") == false) {
			if (team.getLogo().getSize() > (5 * 1024 * 1024)) {
				FieldError error = new FieldError("team", "logo", "["+ logo.getOriginalFilename()+ "] "+ Util.LanguageBundle().getString("error.filetoobig"));
				result.addError(error);
				model = ctrlUtil.createModelMap(model);
				model.addAttribute("team", team);
				return "hr/newteam";
			}
		}

		/*
		 * END ERROR CHECK SECTION
		 */

		if (team.getLeaderId() >= 0) {
			User leader = userRepository.findById(team.getLeaderId());
			if (savedTeam.getLeader() != null && savedTeam.getLeader().getRole() != null
					&& savedTeam.getLeader().getRole().getRoleName().equals(Role.ROLE_TEAMLEAD)) {
				savedTeam.getLeader().setRole(roleRepository.findByRoleName(Role.ROLE_USER));
				userRepository.save(savedTeam.getLeader());
			}

			if (leader.getRole() == null || leader.getRole().getRoleName().equals(Role.ROLE_USER)) {
				leader.setRole(roleRepository.findByRoleName(Role.ROLE_TEAMLEAD));
			}
			team.setLeader(leader);
		} else {
			team.setLeader(null);
		}

		if (team.getHrId() >= 0) {
			User hr = userRepository.findById(team.getHrId());
			team.setHr(hr);
		} else {
			team.setHr(null);
		}

		if (savedTeam != null && savedTeam.getMembers() != null && savedTeam.getMembers().size() > 0) {
			for (User member : savedTeam.getMembers()) {
				member.setTeam(null);
			}
			userRepository.saveAll(savedTeam.getMembers());
			savedTeam.setMembers(null);
			teamRepository.save(savedTeam);
		}

		if (team.getMemberIds() != null && team.getMemberIds().length > 0) {
			List<User> members = new ArrayList<User>();
			for (long id : team.getMemberIds()) {
				User member = userRepository.findById(id);
				member.setAlreadyUsed(true);
				member.setTeam(team);
				members.add(member);
			}

			// Team leader is member of the team
			if (Arrays.asList(team.getMemberIds()).contains(team.getLeaderId()) == false) {
				team.getLeader().setTeam(team);
				members.add(team.getLeader());
			}

			team.setMembers(members);
		} else {
			team.setMembers(null);
		}

		team = teamRepository.save(team);

		// Logo handling after entity save because the entity id is not available before
		// but is needed for
		// the file name
		if (logo != null && logo.getOriginalFilename() != null
				&& FilenameUtils.getExtension(logo.getOriginalFilename()).equals("") == false) {
			String ext = FilenameUtils.getExtension(logo.getOriginalFilename());
			Util.saveImage(logo, "teamlogo_" + team.getId() + "." + ext);
			team.setLogoPath("teamlogo_" + team.getId() + "." + ext);
		} else if (team.getLogoPath() != null && team.getLogoPath().equals("") == false) {
			// do nothing
		} else {
			team.setLogoPath("teamlogo_default.jpg");
		}

		teamRepository.save(team);
		return "redirect:/hr/teamlist";

	}

	@PostMapping("/hr/deleteteam")
	public String deleteTeam(@ModelAttribute(value = "teamId") long teamId) {
		Team team = teamRepository.findById(teamId);

		// remove the team from all members first
		if (team.getMembers() != null && team.getMembers().isEmpty() == false) {
			for (User user : team.getMembers()) {
				user.setTeam(null);
				userRepository.save(user);
			}
			team.setMembers(null);
			teamRepository.save(team);
		}

		teamRepository.delete(team);
		return "redirect:/hr/teamlist";
	}

	@PostMapping("/hr/excludeuserfromteam")
	public String excludeUser(@ModelAttribute(value = "teamId") long teamId,
			@ModelAttribute(value = "memberId") long memberId) {
		Team team = teamRepository.findById(teamId);
		User user = userRepository.findById(memberId);

		// remove the team from all members first
		if (team.getMembers() != null && team.getMembers().isEmpty() == false) {
			for (User member : team.getMembers()) {
				if (member.getId() == user.getId()) {
					team.getMembers().remove(member);
					user.setTeam(null);
					break;
				}

			}
			userRepository.save(user);
			teamRepository.save(team);
		}

		return "redirect:/hr/teamdetails/" + teamId;
	}

	@GetMapping("/hr/teamlist")
	public String teamList(ModelMap model) {
		model = ctrlUtil.createModelMap(model);
		List<Team> teams = (List<Team>) teamRepository.findAll();
		model.addAttribute("teams", teams);
		return "hr/teamlist";
	}

	@GetMapping("/hr/teamdetails/{teamId}")
	public String teamDetails(ModelMap model, @PathVariable String teamId) {
		model = ctrlUtil.createModelMap(model);
		Team team = teamRepository.findById(Long.parseLong(teamId));
		model.addAttribute("team", team);
		return "hr/teamdetails";
	}

	@GetMapping("/hr/editteam/{teamId}")
	public String editTeam(ModelMap model, @PathVariable String teamId) {
		model = ctrlUtil.createModelMap(model);
		Team team = teamRepository.findById(Long.parseLong(teamId));

		if (team.getMembers() != null && team.getMembers().isEmpty() == false) {
			long[] memberIds = new long[team.getMembers().size()];
			for (int i = 0; i < team.getMembers().size(); i++) {
				memberIds[i] = team.getMembers().get(i).getId();
			}
			team.setMemberIds(memberIds);
		}

		if (team.getLeader() != null) {
			team.setLeaderId(team.getLeader().getId());
		}
		if (team.getHr() != null) {
			team.setHrId(team.getHr().getId());
		}

		model.addAttribute("team", team);
		model.addAttribute("users", userRepository.findAll());
		return "hr/newteam";
	}

}
