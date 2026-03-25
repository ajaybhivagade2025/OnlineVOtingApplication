package com.onlineVotingApplication2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*@Controller
public class HomeController {


        @GetMapping("/")
        public String welcome() {
            return "welcome"; // looks for welcome.html in templates/
        }
    }*/



@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "welcome";
    }

    /*@GetMapping("/homepage")
    public String home1(){
        return "home";
    }*/
}


