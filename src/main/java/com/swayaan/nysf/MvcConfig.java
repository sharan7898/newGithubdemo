package com.swayaan.nysf;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String asanaImagesPath = env.getProperty("nysf.asana-images.path");
		String participantImagesPath = env.getProperty("nysf.participant-images.path");
		String judgeImagesPath = env.getProperty("nysf.judge-images.path");
		String eventManagerImagesPath = env.getProperty("nysf.eventmanager-images.path");
		String scoreboardImagePath = env.getProperty("nysf.scoreboard-image.path");
//		exposeDirectory("/static", registry);
//		exposeDirectory("asana-images", registry);
//		exposeDirectory("participant-reg-uploads", registry);
//		exposeDirectory("judge-reg-uploads", registry);
//		exposeDirectory("eventmanager-reg-uploads", registry);
//		exposeDirectory("scoreboard-image", registry);

		// for linux
		registry.addResourceHandler("/asana-images/**").addResourceLocations(asanaImagesPath);
		registry.addResourceHandler("/participant-reg-uploads/**").addResourceLocations(participantImagesPath);
		registry.addResourceHandler("/judge-reg-uploads/**").addResourceLocations(judgeImagesPath);
		registry.addResourceHandler("/eventmanager-reg-uploads/**").addResourceLocations(eventManagerImagesPath);
		registry.addResourceHandler("/scoreboard-image/**").addResourceLocations(scoreboardImagePath);

	}

	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();

		String logicalPath = pathPattern.replace("../", "") + "/**";
		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");

	}

}