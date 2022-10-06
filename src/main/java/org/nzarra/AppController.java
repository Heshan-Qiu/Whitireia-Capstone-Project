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
}
