package com.duc.files.fileshandler;

import com.duc.files.fileshandler.config.FileMvcConfig;
import com.duc.files.fileshandler.service.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({FileMvcConfig.class})
public class FilesHandlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesHandlerApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(FileServices services) {
        return (args) -> {
            services.deleteAll();
            services.initDirectory();
        };
    }

}
