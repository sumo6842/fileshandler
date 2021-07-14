package com.duc.files.fileshandler.controller;

import com.duc.files.fileshandler.service.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class FilesController {

    private final FileServices services;

    @Autowired
    public FilesController(FileServices services) {
        this.services = services;
    }

    @GetMapping("/upload")
    public String showUriFile(Model model) throws IOException {
        model.addAttribute("files", getUri().collect(Collectors.toList()));
        return "index";
    }

    private Stream<String> getUri() throws IOException {
        return services.loadAll().map(path -> MvcUriComponentsBuilder
                .fromMethodName(FilesController.class, "loadFile",
                        path.getFileName().toString())
                .build().toUri().toString());
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadFile(@PathVariable String filename) throws IOException {
        var resource = services.loadFileAsResource(filename);
        return ResponseEntity.ok().header("Content-Disposition",
                "attachment;filename=" + filename).body(resource);
    }


    @PostMapping("/files/upload")
    public String uploadFiles(@RequestParam MultipartFile file,
                              RedirectAttributes attributes) throws Exception {
        services.store(file);
        attributes.addFlashAttribute("message", "The Files has already been uploaded!");
        return "redirect:/upload";
    }

}
