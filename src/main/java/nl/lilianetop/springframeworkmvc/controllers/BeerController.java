package nl.lilianetop.springframeworkmvc.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.BeerDTO;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import nl.lilianetop.springframeworkmvc.services.BeerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL_ID;

@Slf4j
@RestController
@AllArgsConstructor

public class BeerController {

    private final BeerService beerService;

    @GetMapping(value = BEER_URL)
    public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle,
                                   @RequestParam(required = false) Boolean showInventory,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {
        return beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping(value = BEER_URL_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get beer by id - in controller");
        return beerService.getBeerById(beerId).orElseThrow(ExceptionNotFound::new);
    }

    @PostMapping(value = BEER_URL)
    public ResponseEntity<BeerDTO> createAndSaveBeer(@Validated @RequestBody BeerDTO newBeer) {
        BeerDTO savedBeer = beerService.saveNewBeer(newBeer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", BEER_URL + savedBeer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = BEER_URL_ID)
    public ResponseEntity<BeerDTO> updateBeer(@PathVariable("beerId") UUID id,
                                              @Validated @RequestBody BeerDTO beerDto) {
        if (beerService.updateBeerById(id, beerDto).isEmpty()) {
            throw new ExceptionNotFound();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = BEER_URL_ID)
    public ResponseEntity<BeerDTO> deleteBeerById(@PathVariable("beerId") UUID id) {
        if (!beerService.deleteById(id)) {
            throw new ExceptionNotFound();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = BEER_URL_ID)
    public ResponseEntity<BeerDTO> patchBeerById(@PathVariable("beerId") UUID id,
                                                 @RequestBody BeerDTO beerDto) {
        if (beerService.patchBeerById(id, beerDto).isEmpty()) {
            throw new ExceptionNotFound();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
