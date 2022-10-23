package org.nzarra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @RequestMapping(value = "/")
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

    @RequestMapping(value = "/dashboard")
    public String dashboard() {
        return "admin_home";
    }

    @RequestMapping(value = "/users")
    public String users() {
        return "users";
    }

    @RequestMapping(value = "/competitions")
    public String competitions() {
        return "competitions";
    }

    @RequestMapping(value = "/profile")
    public String profile() {
        return "profile";
    }
}
