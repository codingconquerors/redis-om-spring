package com.redis.om.skeleton;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;

import com.redis.om.skeleton.models.Address;
import com.redis.om.skeleton.models.Person;
import com.redis.om.skeleton.repositories.PeopleRepository;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "com.redis.om.skeleton.*")
public class SkeletonApplication {

    @Autowired
    PeopleRepository repo;

    @Bean
    CommandLineRunner loadTestData(PeopleRepository repo) {
        return args -> {
            repo.deleteAll();

            String thorSays = "The Rabbit Is Correct, And Clearly The Smartest One Among You.";

            // Serendipity, 248 Seven Mile Beach Rd, Broken Head NSW 2481, Australia
            Address thorsAddress = Address.of("248", "Seven Mile Beach Rd", "Broken Head", "NSW", "2481", "Australia");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            String date = "2017-12-03T10:15:30+01:00";
            OffsetDateTime date1 = OffsetDateTime.parse(date, dateTimeFormatter);

            Instant inst = Instant.parse("2017-02-03T10:37:30.00Z");

            Person thor = Person.of("Chris", "Hemsworth", 38, thorSays, new Point(153.616667, -28.716667), thorsAddress,
                    Set.of("hammer", "biceps", "hair", "heart"), date1);
            repo.saveAll(List.of(thor));
        };
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Redis OM Spring Skeleton").version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("http")
                .pathsToMatch("/**")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SkeletonApplication.class, args);
    }

}
