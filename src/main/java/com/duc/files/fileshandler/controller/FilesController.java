package com.duc.files.fileshandler.controller;

import com.duc.files.fileshandler.service.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
public class FilesController {

    private final FileServices services;

    @Autowired
    public FilesController(FileServices services) {
        this.services = services;
    }

    @GetMapping("upload")
    public String showUriFiles() {
        return "index";
    }

    @GetMapping("/upload/{filename.+}")
    public ResponseEntity<File> downloadFile(@PathVariable File filename) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename=" + filename)
                .body(filename);
    }

    /**
     * RequestParam Vs @PathVariable: http://www.localhost/admin/duc?age=15: requestParam: age = 15 | PathVariable {"admin"} Duc
     * PathParam vs Param
     *
     * @param file
     * @param attributes
     * @return
     * @throws IOException
     */
    @PostMapping("/files/upload")
    public String uploadFiles(@RequestParam MultipartFile file,
                              RedirectAttributes attributes) throws Exception {
        services.store(file);
        attributes.addFlashAttribute("message", "The Files has already been uploaded!");
        return "redirect:/upload";
    }

}
