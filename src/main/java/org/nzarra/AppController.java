package org.nzarra;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

    @RequestMapping(value = "/")
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "home";
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        return "login";
    }

    @RequestMapping(value = "/dashboard")
    public String dashboard() {
        return "home";
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
