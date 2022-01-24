package com.solbegsoft.citylist.service;

import com.solbegsoft.citylist.model.City;
import com.solbegsoft.citylist.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CityServiceTest {

    @Mock
    private CityRepository repository;

    @InjectMocks
    private CityService service;

    @BeforeEach
    public void setUp() {
        Mockito.reset(repository);
    }

    @Test
    void findAllTest() {

        City city = buildCity();

        List<City> cities = new ArrayList<>();
        cities.add(city);

        when(repository.findAll()).thenReturn(cities);

        List<City> returnedCities = service.findAll();

        assertThat(cities, equalTo(returnedCities));
    }

    @Test
    void findByIdTest() {

        City city = buildCity();

        when(repository.findById(city.getId())).thenReturn(Optional.of(city));

        City returnedCity = service.findByIdOrThrowException(city.getId());

        assertThat(city, equalTo(returnedCity));
    }

    @Test
    void findCityByNameTest() {

        City city = buildCity();

        when(repository.findCityByName(city.getName())).thenReturn(Optional.of(city));

        City returnedCity = service.findCityByName(city.getName());

        assertThat(city, equalTo(returnedCity));
    }

    @Test
    void updateCityByNameOrPhotoTest() {

        City city = buildCity();
        City updateCity = buildUpdateCity();

        when(repository.findById(city.getId())).thenReturn(Optional.of(city));
        when(repository.save(city)).thenReturn(updateCity);

        City returnedCity = service.update(city.getId(), updateCity.getName(), updateCity.getPhoto());

        assertThat(returnedCity.getName(), equalTo(updateCity.getName()));
    }

    private City buildCity() {
        return City.builder()
                .id(1L)
                .name("Dubai")
                .photo("https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/DubaiCollage.jpg/800px-DubaiCollage.jpg")
                .build();
    }

    private City buildUpdateCity() {
        return City.builder()
                .id(2L)
                .name("London")
                .photo("https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/DubaiCollage.jpg/800px-DubaiCollage.jpg")
                .build();
    }
}
