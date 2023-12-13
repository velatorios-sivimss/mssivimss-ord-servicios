package com.imss.sivimss.ordservicios;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.imss.sivimss.ordservicios.util.NoRedirectSimpleClientHttpRequestFactory;

@SpringBootApplication
public class OrdServiciosApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdServiciosApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder().requestFactory(NoRedirectSimpleClientHttpRequestFactory.class)
				.setConnectTimeout(Duration.ofMillis(195000)).setReadTimeout(Duration.ofMillis(195000)).build();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
