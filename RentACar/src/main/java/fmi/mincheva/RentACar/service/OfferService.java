package fmi.mincheva.RentACar.service;

import fmi.mincheva.RentACar.model.Car;
import fmi.mincheva.RentACar.model.Offer;
import fmi.mincheva.RentACar.repository.CarRepository;
import fmi.mincheva.RentACar.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    @Autowired
    private OfferRepository offerRepository;
    private CarRepository carRepository;

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    public Offer addOffer(Offer offer) {
        // Проверка дали вече има същата оферта (същия автомобил, потребител и брой дни)
        if (offerRepository.existsByUserNameAndCarIdAndRentalDays(offer.getUserName(), offer.getCarId(), offer.getRentalDays())) {
            throw new RuntimeException("Офертата вече съществува.");
        }
        double totalPrice = calculateTotalPrice(offer);
        offer.setTotalPrice(totalPrice);
        return offerRepository.save(offer);
    }

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    private double calculateTotalPrice(Offer offer) {
        double basePricePerDay = 100.0; // Примерна стойност, можеш да я вземеш от Car обекта
        int rentalDays = offer.getRentalDays();
        double totalPrice = basePricePerDay * rentalDays;

        // Увеличение за уикенд дни
        if (isWeekendIncluded(rentalDays)) {
            totalPrice *= 1.10; // Увеличение с 10%
        }

        // Допълнителна такса при наличие на инциденти
        if (offer.isHasAccidents()) {
            totalPrice += 200;
        }

        return totalPrice;
    }
    public Offer acceptOffer(Long offerId) {
        // Намерете офертата по ID
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Офертата не е намерена."));

        // Намерете свързания автомобил
        Car car = carRepository.findById(offer.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Автомобилът не е намерен."));

        // Проверете дали автомобилът вече е зает
        if (!car.isAvailable()) {
            throw new RuntimeException("Автомобилът вече е зает.");
        }

        // Маркирайте автомобила като зает
        car.setAvailable(false);
        carRepository.save(car);

        // Обновете офертата, ако е необходимо (например задайте статус на 'Приета')
        offer.setAccepted(true);
        return offerRepository.save(offer);
    }

    private boolean isWeekendIncluded(int rentalDays) {
        // Логика за проверка дали наемът включва уикенд
        return rentalDays > 5; // Примерна логика, можеш да я адаптираш според нуждите
    }

    public List<Offer> getOffersByUserName(String userName) {
        return offerRepository.findByUserName(userName);
    }
}
