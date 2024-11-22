package fmi.mincheva.RentACar.controller;

import fmi.mincheva.RentACar.dto.OfferDTO;
import fmi.mincheva.RentACar.service.OfferService;
import fmi.mincheva.RentACar.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @GetMapping
    public List<OfferDTO> getAllOffers() {
        return offerService.getAllOffers().stream().map(this::convertToOfferDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id)
                .map(offer -> ResponseEntity.ok(convertToOfferDTO(offer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OfferDTO> addOffer(@Valid @RequestBody OfferDTO offerDTO) {
        Offer offer = convertToOfferEntity(offerDTO);
        Offer savedOffer = offerService.addOffer(offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToOfferDTO(savedOffer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<Offer>> getOffersByUserName(@PathVariable String userName) {
        List<Offer> offers = offerService.getOffersByUserName(userName);
        if (offers.isEmpty()) {
            return ResponseEntity.noContent().build(); // Ако няма оферти за този потребител
        }
        return ResponseEntity.ok(offers);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Offer> acceptOffer(@PathVariable Long id) {
        Offer acceptedOffer = offerService.acceptOffer(id);
        return ResponseEntity.ok(acceptedOffer);
    }

    // Методи за преобразуване между Offer и OfferDTO
    private OfferDTO convertToOfferDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setUserName(offer.getUserName());
        offerDTO.setCarId(offer.getCarId());
        offerDTO.setRentalDays(offer.getRentalDays());
        offerDTO.setTotalPrice(offer.getTotalPrice());
        offerDTO.setHasAccidents(offer.isHasAccidents());
        return offerDTO;
    }

    private Offer convertToOfferEntity(OfferDTO offerDTO) {
        Offer offer = new Offer();
        offer.setUserName(offerDTO.getUserName());
        offer.setCarId(offerDTO.getCarId());
        offer.setRentalDays(offerDTO.getRentalDays());
        offer.setTotalPrice(offerDTO.getTotalPrice());
        offer.setHasAccidents(offerDTO.isHasAccidents());
        return offer;
    }
}
