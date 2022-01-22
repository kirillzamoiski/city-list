package com.solbegsoft.citylist.service;

import com.solbegsoft.citylist.exception.ResourceNotFoundException;
import com.solbegsoft.citylist.model.City;
import com.solbegsoft.citylist.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository repository;

    public List<City> findAll() {
        return repository.findAll();
    }

    public City findCityByName(String name) {

        return repository.findCityByName(name).orElseThrow(() -> new ResourceNotFoundException("name", name));
    }

    public City findByIdOrThrowException(Long id) {

        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString()));
    }

    public City update(Long id, String name, String photo) {

        if (name == null && photo == null) {
            return findByIdOrThrowException(id);
        }

        City city = findByIdOrThrowException(id);

        if (name != null) {
            city.setName(name);
        }

        if (photo != null) {
            city.setPhoto(photo);
        }

        return repository.save(city);
    }
}
