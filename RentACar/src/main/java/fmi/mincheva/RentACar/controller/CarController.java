package fmi.mincheva.RentACar.controller;

import fmi.mincheva.RentACar.dto.CarDTO;
import fmi.mincheva.RentACar.model.Car;
import fmi.mincheva.RentACar.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.getAllCars().stream().map(this::convertToCarDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(car -> ResponseEntity.ok(convertToCarDTO(car)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarDTO> addCar(@Valid @RequestBody CarDTO carDTO) {
        if (!carService.isValidLocation(carDTO.getLocation())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Car car = convertToCarEntity(carDTO);
        Car savedCar = carService.addCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToCarDTO(savedCar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @Valid @RequestBody CarDTO carDTO) {
        if (!carService.isValidLocation(carDTO.getLocation())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Car updatedCar = carService.updateCar(id, convertToCarEntity(carDTO));
        return ResponseEntity.ok(convertToCarDTO(updatedCar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-location")
    public List<CarDTO> getCarsByLocation(@RequestParam String location) {
        return carService.getCarsByLocation(location).stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }

    // Методи за преобразуване между Car и CarDTO
    private CarDTO convertToCarDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setName(car.getName());
        carDTO.setLocation(car.getLocation());
        carDTO.setPricePerDay(car.getPricePerDay());
        return carDTO;
    }

    private Car convertToCarEntity(CarDTO carDTO) {
        Car car = new Car();
        car.setName(carDTO.getName());
        car.setLocation(carDTO.getLocation());
        car.setPricePerDay(carDTO.getPricePerDay());
        return car;
    }
}
