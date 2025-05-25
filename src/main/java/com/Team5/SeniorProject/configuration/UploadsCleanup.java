package com.Team5.SeniorProject.configuration;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadsCleanup implements ApplicationRunner {
	
	@Value("${file.upload-dir}")
    private String uploadDir;

	@Override
	public void run(ApplicationArguments args) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (Files.exists(uploadPath) && Files.isDirectory(uploadPath)) {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(uploadPath)) {
				for (Path file : ds) {
					if (Files.isRegularFile(file)) {
						Files.deleteIfExists(file);
					}
				}
			}
		}
	}
}
