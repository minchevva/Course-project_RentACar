package fmi.mincheva.RentACar.service;

import fmi.mincheva.RentACar.model.Car;
import fmi.mincheva.RentACar.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    private static final List<String> ALLOWED_LOCATIONS = Arrays.asList("Пловдив", "София", "Варна", "Бургас");


    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car addCar(Car car) {
        // Проверка дали вече има автомобил със същото име и локация
        if (carRepository.existsByNameAndLocation(car.getName(), car.getLocation())) {
            throw new RuntimeException("Автомобилът вече съществува.");
        }
        if (!isValidLocation(car.getLocation())) {
            throw new IllegalArgumentException("Автомобилът може да бъде добавен само в Пловдив, София, Варна или Бургас.");
        }
        return carRepository.save(car);
    }

    public boolean isValidLocation(String location) {
        return ALLOWED_LOCATIONS.contains(location);
    }

    public Car updateCar(Long id, Car carDetails) {
        if (!isValidLocation(carDetails.getLocation())) {
            throw new IllegalArgumentException("Автомобилът може да бъде актуализиран само в Пловдив, София, Варна или Бургас.");
        }
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Няма автомобил с посоченото ID."));

        car.setName(carDetails.getName());
        car.setLocation(carDetails.getLocation());
        car.setPricePerDay(carDetails.getPricePerDay());

        return carRepository.save(car);
    }

    public List<Car> getCarsByLocation(String location) {
        if (!isValidLocation(location)) {
            throw new IllegalArgumentException("Невалидна локация. Разрешени са: Пловдив, София, Варна, Бургас.");
        }
        return carRepository.findByLocation(location);
    }


    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}