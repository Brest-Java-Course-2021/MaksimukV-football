package com.epam.brest.service.web_app;

import com.epam.brest.model.Team;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Controller
public class HomeController {

//    @GetMapping(value = "/")
//    public String defaultPageRedirect() {
//        return "redirect:teams";
//    }

@GetMapping("/")
public String homePage(Model model) {
    return "home";
}
}
