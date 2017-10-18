package atest.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableWebMvc
public class DataApplication {

	private static final Logger logger = LoggerFactory.getLogger("Application");
	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(
						"http://localhost:4200", "http://127.0.0.1:4200", "http://jiens-mbp.mul.ie.ibm.com:4200",
						"http://jiens-mbp.locals:4200", "http://crystal1024.mulvm.ie.ibm.com:4200"
				);
			}
		};
	}
}
