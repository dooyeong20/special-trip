package com.project.mega.triplus;

import com.project.mega.triplus.service.UserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TriplusApplication extends WebMvcConfigurationSupport {

	private final UserArgumentResolver userArgumentResolver;

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		super.addArgumentResolvers(argumentResolvers);
		argumentResolvers.add(userArgumentResolver);
	}

	public static void main(String[] args) {
		SpringApplication.run(TriplusApplication.class, args);
	}

}
