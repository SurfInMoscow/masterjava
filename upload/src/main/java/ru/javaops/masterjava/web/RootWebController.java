package ru.javaops.masterjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootWebController {

    @GetMapping("/")
    public String root() {
        return "index";
    }
}
