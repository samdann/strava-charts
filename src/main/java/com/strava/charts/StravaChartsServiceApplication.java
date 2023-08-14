package com.strava.charts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(exclude = {RepositoryRestMvcAutoConfiguration.class,
        SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableMongoRepositories
public class StravaChartsServiceApplication {

     public static void main(String[] args) {
          SpringApplication.run(StravaChartsServiceApplication.class, args);
     }

     @Bean
     public WebMvcConfigurer corsConfigurer() {
          return new WebMvcConfigurerAdapter() {
               @Override
               public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**").allowedOrigins("*");
               }
          };
     }

}
