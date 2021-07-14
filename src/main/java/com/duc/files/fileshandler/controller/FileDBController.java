package com.duc.files.fileshandler.controller;

import com.duc.files.fileshandler.model.Document;
import com.duc.files.fileshandler.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

@Controller
public class FileDBController {

    @Autowired
    private DocumentRepository repo;

    @PostMapping("/files/db")
    public String uploadFilesDB(@RequestParam MultipartFile file,
                                Model model,
                                RedirectAttributes attributes) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        var document = new Document();
        document.setName(filename);
        document.setContent(file.getBytes());
        var size = file.getSize();
        document.setSize(size);
        document.setUploadTime(LocalDate.now());

        var save = repo.save(document);
        System.out.println(save.getName());

        attributes.addFlashAttribute("message", "The File has been uploaded!");


        return "redirect:/db";
    }

//    @GetMapping("/download/file/{filename}")
//    public void downloadFromDB(@PathVariable("filename") String filename,
//                               HttpServletResponse response) {
//        if (!filename.isEmpty()) {
//            var documentByName = repo.findDocumentByName(filename);
//            if (documentByName != null) {
//                setupResponse(filename, response);
//                try (OutputStream output = response.getOutputStream()) {
//                    output.write(documentByName.getContent());
//                } catch (IOException ex) {
//                    System.out.println("Error: " + ex.getMessage());
//                }
//
//            }
//
//        }
//    }

    private void setupResponse(String filename, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headKey = "Content-Disposition";
        String headerValue = "attachment;filename" + filename;
        response.setHeader(headKey, headerValue);
    }
//
    private Function<Long, ResponseEntity> getResponse = (id) -> {
        var document = repo.findById(id).get();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename="+ document.getName())
                .body(document.getContent());
    };

    @GetMapping("/download/file")
    public ResponseEntity<byte[]> getFile(@Param("id") Long id) {
        return getResponse.apply(id);
    }


}
