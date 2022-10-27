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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    private final UserRepository userRepository;

    private final CompetitionRepository competitionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AppController(UserRepository userRepository, CompetitionRepository competitionRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.competitionRepository = competitionRepository;
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
    public String home() {
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
        logger.debug("Reach the user add get method.");
        User user = new User();
        user.setActive(true);
        model.addAttribute("user", user);
        return "user_add";
    }

    @PostMapping(value = "/admin/users/add")
    public String userAddSubmit(@ModelAttribute User user, Model model) {
        logger.debug("Submit user: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        User emptyUser = new User();
        emptyUser.setActive(true);
        model.addAttribute("user", emptyUser);
        model.addAttribute("message", "Added user successfully!");

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
        model.addAttribute("message", "Added competition successfully!");

        return "competition_add";
    }

    public void initCompetitionFormData(Model model) {
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
}
