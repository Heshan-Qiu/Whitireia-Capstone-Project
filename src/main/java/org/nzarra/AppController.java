package org.nzarra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.*;

@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    private final UserRepository userRepository;

    private final CompetitionRepository competitionRepository;

    private final SectionRepository sectionRepository;

    private final CompetitorRepository competitorRepository;

    private final ScoreRepository scoreRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AppController(UserRepository userRepository, CompetitionRepository competitionRepository,
                         SectionRepository sectionRepository, CompetitorRepository competitorRepository,
                         ScoreRepository scoreRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.competitionRepository = competitionRepository;
        this.sectionRepository = sectionRepository;
        this.competitorRepository = competitorRepository;
        this.scoreRepository = scoreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/")
    public String index() {
        return "login";
    }

    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, HttpSession session) {
        session.setAttribute("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        return "login";
    }

    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);
        String error = "";
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Login error for unknown reason!";
        }
        return error;
    }

    @GetMapping(value = "/admin/home")
    public String adminHome() {
        return "admin_home";
    }

    @GetMapping(value = "/admin/users")
    public String users(Model model, @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size, @RequestParam("sort") Optional<String> sort) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String pageSort = sort.orElse("username");
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(pageSort));
        Page<User> pageData = userRepository.findAll(pageable);
        model.addAttribute("pageData", pageData);
        return "users";
    }

    @GetMapping(value = "/admin/users/add")
    public String userAddForm(Model model) {
        User user = new User();
        user.setActive(true);
        model.addAttribute("user", user);
        return "user_add";
    }

    @PostMapping(value = "/admin/users/add")
    public String userAddSubmit(@ModelAttribute User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        User emptyUser = new User();
        emptyUser.setActive(true);
        model.addAttribute("user", emptyUser);
        model.addAttribute("message", "Add user successfully!");

        return "user_add";
    }

    @GetMapping(value = "/admin/competitions")
    public String competitions(Model model, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size, @RequestParam("sort") Optional<String> sort) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String pageSort = sort.orElse("date");
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(pageSort));
        Page<Competition> pageData = competitionRepository.findAll(pageable);
        model.addAttribute("pageData", pageData);
        return "competitions";
    }

    @GetMapping(value = "/admin/competitions/add")
    public String competitionAddForm(Model model) {
        initCompetitionFormData(model);
        return "competition_add";
    }

    @PostMapping(value = "/admin/competitions/add")
    public String competitionAddSubmit(@ModelAttribute Competition competition, Model model) {
        competitionRepository.save(competition);

        initCompetitionFormData(model);
        model.addAttribute("message", "Add competition successfully!");

        return "competition_add";
    }

    private void initCompetitionFormData(Model model) {
        Competition competition = new Competition();
        competition.setDate(new Date());
        competition.setActive(true);

        List<String> judges = new ArrayList<>();
        userRepository.findByRolesInAndActive(List.of(User.ROLE_JUDGE), true).forEach((judge) -> judges.add(judge.getUsername()));
        List<String> scrutineers = new ArrayList<>();
        userRepository.findByRolesInAndActive(List.of(User.ROLE_SCRUTINEER), true).forEach((scrutineer) -> scrutineers.add(scrutineer.getUsername()));
        List<String> marshalls = new ArrayList<>();
        userRepository.findByRolesInAndActive(List.of(User.ROLE_MARSHALL), true).forEach((marshall) -> marshalls.add(marshall.getUsername()));

        model.addAttribute("competition", competition);
        model.addAttribute("judges", judges);
        model.addAttribute("scrutineers", scrutineers);
        model.addAttribute("marshalls", marshalls);
    }

    @GetMapping(value = "/profile")
    public String profile(Authentication authentication, Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(authentication.getName());
        model.addAttribute("user", optionalUser.get());
        return "profile";
    }

    @PostMapping(value = "/profile/update")
    public String profileUpdate(@RequestParam("username") String username, @RequestParam("fullName") String fullName,
                                @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass,
                                Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.get();

        if (passwordEncoder.matches(oldPass, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPass));
            user.setFullName(fullName);
            userRepository.save(user);

            model.addAttribute("message", "Profile updated successfully!");
        } else {
            model.addAttribute("message", "Old password is not correct!");
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping(value = "/judge/home")
    public String judgeHome(Authentication authentication, Model model) {
        if (!initJudgeFormData(authentication, model))
            model.addAttribute("message", "You don't have any section to score.");

        return "judge_home";
    }

    @PostMapping(value = "/judge/add")
    public String judgeAdd(Authentication authentication, Model model, @ModelAttribute JudgeForm judgeForm) {
        Optional<Section> optionalSection = sectionRepository.findById(judgeForm.getSectionId());
        Optional<User> judge = userRepository.findByUsername(authentication.getName());

        if (optionalSection.isPresent() && judge.isPresent()) {
            for (Score score : judgeForm.getScores()) {
                if (score.getVariety() != null && score.getTiming() != null && score.getHarmony() != null &&
                        score.getTotal() != null && score.getPlacing() != null) {
                    score.setSection(optionalSection.get());
                    score.setJudge(judge.get());
                    scoreRepository.save(score);
                }
            }
        }

        model.addAttribute("message", "Save data successfully!");

        return "judge_home";
    }

    private boolean initJudgeFormData(Authentication authentication, Model model) {
        Optional<Competition> competition = competitionRepository.findFirstByJudgesInAndActiveOrderByDateDesc(
                List.of(authentication.getName()), true);
        boolean found = false;
        if (competition.isPresent()) {
            Optional<Section> section = sectionRepository.findFirstByCompetitionAndActiveOrderByTimeDesc(
                    competition.get(), true);
            if (section.isPresent()) {
                found = true;

                List<Score> scores = new ArrayList<>();
                for (Competitor competitor : section.get().getCompetitors())
                    scores.add(new Score(competitor));

                JudgeForm judgeForm = new JudgeForm(section.get().getId(), scores);

                model.addAttribute("judgeForm", judgeForm);
                model.addAttribute("section", section.get());
                model.addAttribute("competitionName", competition.get().getName());
            }
        }

        return found;
    }

    @GetMapping(value = "/scrutineer/home")
    public String scrutineerHome() {
        return "scrutineer_home";
    }

    @GetMapping(value = "/marshall/home")
    public String marshallHome(Authentication authentication, Model model) {
        initMarshallFormData(authentication, model);
        return "marshall_home";
    }

    @PostMapping(value = "/marshall/add")
    @Transactional
    public String marshallAdd(Authentication authentication, Model model, @ModelAttribute MarshallForm marshallForm) {
        Optional<Section> optionalSection = sectionRepository.findById(marshallForm.getSectionId());
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            competitorRepository.deleteAllBySection(section);

            if (marshallForm.getCompetitors() != null) {
                for (Competitor competitor : marshallForm.getCompetitors())
                    competitor.setSection(section);
                competitorRepository.saveAll(marshallForm.getCompetitors());
            }
        }

        initMarshallFormData(authentication, model);
        model.addAttribute("message", "Save data successfully!");

        return "marshall_home";
    }

    private void initMarshallFormData(Authentication authentication, Model model) {
        List<Competition> competitions = competitionRepository.findAllByMarshallAndActive(authentication.getName(), true);
        model.addAttribute("competitions", competitions);

        List<Section> formSections = new ArrayList<>();
        MarshallForm marshallForm = new MarshallForm(null, new ArrayList<>());
        String sectionName = null;

        if (!competitions.isEmpty()) {
            Competition competition = competitions.get(0);

            List<Section> sections = sectionRepository.findAllByCompetitionAndActive(competition, true);
            if (!sections.isEmpty()) {
                formSections = sections;
                Section section = sections.get(0);
                marshallForm.setSectionId(section.getId());
                sectionName = section.getName();

                List<Competitor> formCompetitors = marshallForm.getCompetitors();
                List<Competitor> competitors = competitorRepository.findAllBySection(section);
                if (!competitors.isEmpty()) {
                    for (Competitor competitor : competitors) {
                        formCompetitors.add(new Competitor(competitor.getId(), competitor.getLineup(),
                                competitor.getNames(), competitor.getColours(), competitor.getNumber()));
                    }
                }
            }
        }

        model.addAttribute("sections", formSections);
        model.addAttribute("sectionName", sectionName);
        model.addAttribute("marshallForm", marshallForm);
    }

    @GetMapping(value = "/marshall/sections")
    @ResponseBody
    public List<AbstractMap.SimpleEntry<Integer, String>> marshallSections(@RequestParam("competition_id") Integer competitionId) {
        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);

        if (optionalCompetition.isPresent()) {
            List<Section> sections = sectionRepository.findAllByCompetitionAndActive(optionalCompetition.get(), true);
            List<AbstractMap.SimpleEntry<Integer, String>> returnList = new ArrayList<>();
            for (Section section : sections) {
                returnList.add(new AbstractMap.SimpleEntry<>(section.getId(), section.getName()));
            }

            return returnList;
        }

        return null;
    }

    @GetMapping(value = "/marshall/competitors")
    @ResponseBody
    public List<Competitor> marshallCompetitors(@RequestParam("section_id") Integer sectionId) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            List<Competitor> competitors = new ArrayList<>();
            for (Competitor competitor : section.getCompetitors())
                competitors.add(new Competitor(competitor.getId(), competitor.getLineup(), competitor.getNames(), competitor.getColours(), competitor.getNumber()));

            return competitors;
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping(value = "/admin/sections")
    public String sections(Model model, @RequestParam("competition_id") Integer competitionId,
                           @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sort") Optional<String> sort) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String pageSort = sort.orElse("id");
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(pageSort));

        Optional<Competition> optionalCompetition = competitionRepository.findById(competitionId);
        Page<Section> pageData = sectionRepository.findAllByCompetition(optionalCompetition.get(), pageable);
        model.addAttribute("pageData", pageData);
        model.addAttribute("competition", optionalCompetition.get());

        return "sections";
    }

    @GetMapping(value = "/admin/sections/add")
    public String sectionAddForm(@RequestParam("competition_id") Integer competitionId, Model model) {
        initSectionFormData(competitionId, model);
        return "section_add";
    }

    @PostMapping(value = "/admin/sections/add")
    public String sectionAddSubmit(@ModelAttribute Section section, Model model) {
        sectionRepository.save(section);

        initSectionFormData(section.getCompetition().getId(), model);
        model.addAttribute("message", "Add section successfully!");

        return "section_add";
    }

    private void initSectionFormData(Integer competitionId, Model model) {
        Competition competition = competitionRepository.findById(competitionId).get();

        Section section = new Section();
        section.setIndex(competition.getSectionList().size() + 1);
        section.setCompetition(competition);
        section.setTime(new Time(new Date().getTime()));
        section.setActive(true);

        model.addAttribute("section", section);
    }
}
