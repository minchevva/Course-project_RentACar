package fmi.mincheva.RentACar.dto;

import fmi.mincheva.RentACar.model.Car;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CarDTO {
    private Long id;

    @NotEmpty(message = "Името на автомобила не може да бъде празно.")
    private String name;

    @NotEmpty(message = "Локацията не може да бъде празна.")
    private String location;

    @NotNull(message = "Цената на ден не може да бъде празна.")
    @Positive(message = "Цената на ден трябва да бъде положителна.")
    private Double pricePerDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
