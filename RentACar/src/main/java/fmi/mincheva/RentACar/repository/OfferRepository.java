package fmi.mincheva.RentACar.repository;

import fmi.mincheva.RentACar.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    boolean existsByUserNameAndCarIdAndRentalDays(String userName, Long carId, int rentalDays);
    List<Offer> findByUserName(String userName);
}
