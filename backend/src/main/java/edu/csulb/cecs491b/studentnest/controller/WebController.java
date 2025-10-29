package edu.csulb.cecs491b.studentnest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @GetMapping(value = {"/"})
    public String index() {
        return "forward:/landing.html";
    }

//    @GetMapping(value = {"/login"})
//    public String login() {
//        return "forward:/login.html";
//    }
//
//    @GetMapping(value = {"/auth-test"})
//    public String auth_test() {
//        return "forward:/auth-test.html";
//    }
//
//    @GetMapping(value = {"/profile"})
//    public String profile() {
//        return "forward:/profile.html";
//    }
//
    @GetMapping(value = {"/react"})
    public String react() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "/{path:[^.]*}")
    public String catchAll() {
        return "forward:/index.html";
    }
}

