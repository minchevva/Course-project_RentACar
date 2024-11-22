package fmi.mincheva.RentACar.repository;


import fmi.mincheva.RentACar.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByNameAndLocation(String name, String location);
    List<Car> findByLocation(String location);
}