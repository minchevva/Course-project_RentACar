package fmi.mincheva.RentACar;

import fmi.mincheva.RentACar.model.Car;
import fmi.mincheva.RentACar.repository.CarRepository;
import fmi.mincheva.RentACar.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCar() {
        Car car = new Car();
        car.setName("Toyota");
        car.setLocation("София");
        car.setPricePerDay(50.0);

        when(carRepository.save(car)).thenReturn(car);

        Car savedCar = carService.addCar(car);

        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getName());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testGetCarById() {
        Car car = new Car();
        car.setId(1L);
        car.setName("Toyota");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Optional<Car> foundCar = carService.getCarById(1L);

        assertTrue(foundCar.isPresent());
        assertEquals("Toyota", foundCar.get().getName());
    }

    @Test
    void testDeleteCar() {
        Long carId = 1L;

        doNothing().when(carRepository).deleteById(carId);

        carService.deleteCar(carId);

        verify(carRepository, times(1)).deleteById(carId);
    }
}
