package nl.lilianetop.springframeworkmvc.controllersIT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.lilianetop.springframeworkmvc.controllers.BeerController;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.mappers.BeerMapper;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void deleteBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.deleteBeerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeer() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity<BeerDto> responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }
    @Transactional
    @Rollback
    @Test
    void updateBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () ->
                beerController.updateBeer(UUID.randomUUID(), BeerDto.builder().build()));
    }


    @Transactional
    @Rollback
    @Test
    void updateBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String updatedName = "Updated";
        beerDto.setBeerName(updatedName);

        ResponseEntity<BeerDto> responseEntity = beerController.updateBeer(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Beer updatedBeer = beerRepository.findAll().get(0);
        assertThat(updatedBeer.getBeerName()).isEqualTo(updatedName);

    }
    @Rollback
    @Transactional
    @Test
    void patchBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setPrice(BigDecimal.valueOf(14.98));
        beerDto.setQuantityOnHand(786);

        ResponseEntity<BeerDto> responseEntity = beerController.patchBeerById(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Beer updatedBeer = beerRepository.findAll().get(0);
       assertThat(updatedBeer.getPrice()).isEqualTo(BigDecimal.valueOf(14.98));
       assertThat(updatedBeer.getQuantityOnHand()).isEqualTo(786);
    }


    @Test
    void patchBeerWithInvalidName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Namehdjkjui jejruiwe;r juiojuioawejijfnka. jiejfiaejfkjnaek .fneks ensgkenjkjskg");

        assert beer != null;
        mockMvc.perform(patch(BEER_URL_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest());
        beerRepository.flush();
    }


    @Test
    void patchBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.patchBeerById(UUID.randomUUID(), BeerDto.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeer() {
        BeerDto beerDto = BeerDto.builder().beerName("Lager").build();
        ResponseEntity<BeerDto> responseEntity = beerController.createAndSaveBeer(beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        String[] UUIDLocations = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID savedUuid = UUID.fromString(UUIDLocations[4]);

        Beer beer = beerRepository.findById(savedUuid).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
    }

    @Test
    void getBeerByIdNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void getBeerList() {
        List<BeerDto> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }
    @Transactional
    @Rollback
    @Test
    void getEmptyList() {
        beerRepository.deleteAll();
        List<BeerDto> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);
    }
}