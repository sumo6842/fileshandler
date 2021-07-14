package com.duc.files.fileshandler.controller;

import com.duc.files.fileshandler.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private DocumentRepository repo;

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/db")
    public String homePage(Model model) {
        model.addAttribute("files", repo.findAll());
        return "home";
//        return (List<Document>) repo.findAll();
    }
}
