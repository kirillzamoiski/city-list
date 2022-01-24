package com.solbegsoft.citylist.resource;

import com.solbegsoft.citylist.model.City;
import com.solbegsoft.citylist.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CityResourceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CityService cityService;

    private List<City> cities;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .dispatchOptions(true)
                .apply(springSecurity())
                .build();

        this.cities = new ArrayList<>();
        this.cities = buildCitiesList();
    }

    @Test
    @WithAnonymousUser
    void findAllTest() throws Exception {

        given(cityService.findAll()).willReturn(cities);

        this.mockMvc.perform(get("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(cities.size())));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findByIdTest() throws Exception {

        City city = this.cities.get(0);

        given(cityService.findByIdOrThrowException(city.getId())).willReturn(city);

        this.mockMvc.perform(get("/api/cities/" + city.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(city.getName()))
                .andExpect(jsonPath("$.photo").value(city.getPhoto()));
    }

    @Test
    @WithAnonymousUser
    void findCityByNameTest() throws Exception {

        City city = this.cities.get(0);

        when(cityService.findCityByName(city.getName())).thenReturn(city);

        this.mockMvc.perform(get("/api/cities/search")
                        .param("name", city.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(city.getName()))
                .andExpect(jsonPath("$.photo").value(city.getPhoto()));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateCityByNameOrPhotoTest() throws Exception {

        City city = this.cities.get(0);
        City returnedCity = this.cities.get(1);

        when(cityService.update(city.getId(), returnedCity.getName(), returnedCity.getPhoto())).thenReturn(returnedCity);

        this.mockMvc.perform(put("/api/cities/" + city.getId())
                        .param("name", returnedCity.getName())
                        .param("photo", returnedCity.getPhoto())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(returnedCity.getName()))
                .andExpect(jsonPath("$.photo").value(returnedCity.getPhoto()));
    }

    private List<City> buildCitiesList() {

        List<City> cityList = new ArrayList<>();

        cityList.add(City.builder()
                .id(1L)
                .name("Dubai")
                .photo("photo_url")
                .build());
        cityList.add(City.builder()
                .id(2L)
                .name("London")
                .photo("photo_url")
                .build());

        return cityList;

    }
}
