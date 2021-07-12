package com.duc.files.fileshandler.service;

import com.duc.files.fileshandler.config.FileMvcConfig;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServices {
    private final Path root;

    @Autowired
    public FileServices(FileMvcConfig fileMvcConfig) {
        root = Paths.get(fileMvcConfig.getRootLocation());
    }

    public void initDirectory() throws IOException {
        Files.createDirectories(root);
    }

    public void deleteAll() throws IOException {
        if (Files.exists(root)) {
            Files.walk(root, 1)
                    .filter(file -> !file.equals(root))
                    .forEach(Unchecked.consumer(Files::delete));
        }

    }

    public void store(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty");
        }
        String name = file.getOriginalFilename();
        Path path = root.resolve(name).normalize().toAbsolutePath();
        if (!path.getParent().equals(root.toAbsolutePath())) {
            throw new Exception("Not allowed located file this folder!!!");
        }

        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    public Stream<Path> loadAll() throws IOException {
        return Files.walk(this.root, 1)
                .filter(path -> !path.equals(this.root));
    }

    public Resource loadFileAsResource(String filename) throws IOException {
        Path load = load(filename);
        Resource urlResource = new UrlResource(load.toUri());
        if (urlResource.exists() || urlResource.isReadable()) {
            return urlResource;
        } else throw new IOException("Resource cant read or existed");

    }

    public Path load(String filename) {
        return this.root.resolve(filename);
    }
}
