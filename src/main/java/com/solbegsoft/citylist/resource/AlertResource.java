package com.solbegsoft.citylist.resource;

import com.solbegsoft.citylist.model.City;
import com.solbegsoft.citylist.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cities")
public class AlertResource {

    private final CityService service;

    @GetMapping
    private ResponseEntity<List<City>> findAll() {
        return ok(service.findAll());
    }

    @GetMapping("/{id}")
    private ResponseEntity<City> findById(@PathVariable final Long id) {
        return ok(service.findByIdOrThrowException(id));
    }

    @GetMapping("/search")
    private ResponseEntity<City> findCityByName(@RequestParam(name = "name") final String name) {
        return ok(service.findCityByName(name));
    }

    @PutMapping("/{id}")
    private ResponseEntity<City> updateCityNameOrPhoto(@PathVariable final Long id,
                                                       @RequestParam(name = "name", required = false) final String name,
                                                       @RequestParam(name = "photo", required = false) final String photo) {
        return ok(service.update(id, name, photo));
    }
}
