package in.chill.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("in.chill.main.entity")
@EnableJpaRepositories("in.chill.main.repository")
@ComponentScan(basePackages = {"in.chill.main"})
public class EventsGroup1Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EventsGroup1Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(EventsGroup1Application.class, args);
    }
}
