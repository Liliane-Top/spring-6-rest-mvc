package nl.lilianetop.springframeworkmvc.controllersIT;

import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nl.lilianetop.springframeworkmvc.controllers.BeerController;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.mappers.BeerMapper;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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

  @Test
  void beerListByBeerStyle() throws Exception {
    mockMvc.perform(get(BEER_URL)
            .queryParam("beerStyle", BeerStyle.IPA.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(548)));

  }

  @Test
  void beerListByName() throws Exception {
    mockMvc.perform(get(BEER_URL)
            .queryParam("beerName", "%IPA%"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(336)));
  }

  @Test
  void beerListByNameAndByBeerStyle() throws Exception {
    mockMvc.perform(get(BEER_URL)
            .queryParam("beerName", "%IPA%")
            .queryParam("beerStyle", BeerStyle.IPA.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)));
  }

  @Test
  void beerListByNameByStyleByInventoryFalse() throws Exception {
    mockMvc.perform(get(BEER_URL)
            .queryParam("beerName", "IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", "false"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)))
        .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()))
        .andExpect(jsonPath("$.[155].quantityOnHand").value(IsNull.nullValue()));
  }

  @Test
  void beerListByNameByStyleByInventoryTrue() throws Exception {
    mockMvc.perform(get(BEER_URL)
            .queryParam("beerName", "%IPA%")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("showInventory", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)))
        .andExpect(jsonPath("$.[0].quantityOnHand").value(29))
        .andExpect(jsonPath("$.[5].quantityOnHand").value(61));
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
    beerMap.put("beerName",
        "New Namehdjkjui jejruiwe;r juiojuioawejijfnka. jiejfiaejfkjnaek .fneks ensgkenjkjskg");

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
    assertThrows(ExceptionNotFound.class,
        () -> beerController.patchBeerById(UUID.randomUUID(), BeerDto.builder().build()));
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
    List<BeerDto> dtos = beerController.listBeers(null, null, false);
    assertThat(dtos.size()).isEqualTo(2413);
  }

  @Transactional
  @Rollback
  @Test
  void getEmptyList() {
    beerRepository.deleteAll();
    List<BeerDto> dtos = beerController.listBeers(null, null, false);
    assertThat(dtos.size()).isEqualTo(0);
  }
}