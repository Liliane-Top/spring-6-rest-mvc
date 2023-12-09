package nl.lilianetop.springframeworkmvc.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.services.BeerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL_ID;

@Slf4j
@RestController
@AllArgsConstructor

public class BeerController {

    private final BeerService beerService;

    @GetMapping(value = BEER_URL)
    public List<BeerDto> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(value = BEER_URL_ID)
    public BeerDto getBeerById(@PathVariable("beerId") UUID beerId){
        log.debug("Get beer by id - in controller");
        return beerService.getBeerById(beerId).orElseThrow(ExceptionNotFound::new);
    }
}
