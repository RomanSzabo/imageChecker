package at.ac.univie.imagechecker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(
		info = @Info(
				title = "Image Checker",
				version = "0.0.1-SNAPSHOT",
				description = "Image checker project for LP1 course of University of Vienna, Faculty of Computer Science"
		)
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
@Slf4j
@SpringBootApplication
//@ConfigurationPropertiesScan("at.ac.univie.imagechecker.util")
public class ImageCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageCheckerApplication.class, args);
	}

}
