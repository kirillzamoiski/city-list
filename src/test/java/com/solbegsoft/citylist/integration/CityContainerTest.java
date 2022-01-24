package com.solbegsoft.citylist.integration;

import com.solbegsoft.citylist.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-integration")
@Testcontainers
public class CityContainerTest {

    @Container
    private static final PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:14.1");

    @Autowired
    private TestRestTemplate testRestTemplate;

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresDB::getDriverClassName);
    }

    @Test
    void findAllTest() {
        City[] response = testRestTemplate.getForObject("/api/cities", City[].class);

        List<City> cities = List.of(response);

        assertEquals(1000, cities.size());
    }

    @Test
    void findByIdTest() {
        City city = testRestTemplate
                .withBasicAuth("admin", "admin")
                .getForObject("/api/cities/{id}", City.class, 25);

        assertThat(25L, equalTo(city.getId()));
        assertThat("Los Angeles", equalTo(city.getName()));
    }

    @Test
    void findCityByNameTest() {
        City city = testRestTemplate.getForObject("/api/cities/search?name=London", City.class);

        assertThat("London", equalTo(city.getName()));
    }

    @Test
    void updateCityByNameOrPhotoTest() {
        String name = "Ivacare";
        Integer id = 22;

        City beforeUpdateCity = testRestTemplate
                .withBasicAuth("admin", "admin")
                .getForObject("/api/cities/{id}", City.class, id);

        testRestTemplate
                .withBasicAuth("admin", "admin")
                .put("/api/cities/{id}?name=" + name, null, id);

        City updatedCity = testRestTemplate
                .withBasicAuth("admin", "admin")
                .getForObject("/api/cities/{id}", City.class, id);

        assertNotEquals(beforeUpdateCity.getName(), name);
        assertThat(name, equalTo(updatedCity.getName()));
    }
}
