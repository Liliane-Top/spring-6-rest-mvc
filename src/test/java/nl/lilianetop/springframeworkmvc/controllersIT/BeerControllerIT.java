package nl.lilianetop.springframeworkmvc.controllersIT;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.lilianetop.springframeworkmvc.controllers.BeerController;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.mappers.BeerMapper;
import nl.lilianetop.springframeworkmvc.models.BeerDTO;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static nl.lilianetop.springframeworkmvc.controllersTests.BeerControllerTests.jwtRequestPostProcessor;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getBeersWithoutAuthorization() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void listBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .with(jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(50)))
                .andExpect(jsonPath("$.content.[46].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void deleteBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.deleteBeerById(UUID.randomUUID()));
    }

    @Test
    void beerListByBeerStyle() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .with(jwtRequestPostProcessor)
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(25)));

    }

    @Test
    void beerListByName() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .with(jwtRequestPostProcessor)
                        .queryParam("beerName", "%IPA%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(25)));
    }

    @Test
    void beerListByNameAndByBeerStyle() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .with(jwtRequestPostProcessor)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(25)));
    }

    @Test
    void beerListByNameByStyleByInventoryFalse() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .with(jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(25)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content.[24].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void beerListByNameByStyleByInventoryTrue() throws Exception {
        mockMvc.perform(get(BEER_URL)
                        .with(jwtRequestPostProcessor)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(25)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(1907))
                .andExpect(jsonPath("$.content.[5].quantityOnHand").value(830));
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeer() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity<BeerDTO> responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Transactional
    @Rollback
    @Test
    void updateBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () ->
                beerController.updateBeer(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Transactional
    @Rollback
    @Test
    void updateBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String updatedName = "Updated";
        beerDto.setBeerName(updatedName);

        ResponseEntity<BeerDTO> responseEntity = beerController.updateBeer(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Beer updatedBeer = beerRepository.findAll().get(0);
        assertThat(updatedBeer.getBeerName()).isEqualTo(updatedName);
    }

    @Rollback
    @Transactional
    @Test
    void patchBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setPrice(BigDecimal.valueOf(14.98));
        beerDto.setQuantityOnHand(786);

        ResponseEntity<BeerDTO> responseEntity = beerController.patchBeerById(beer.getId(), beerDto);
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
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest());
//    beerRepository.flush();
    }


    @Test
    void patchBeerNotFound() {
        assertThrows(ExceptionNotFound.class,
                () -> beerController.patchBeerById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeer() {
        BeerDTO beerDto = BeerDTO.builder().beerName("Lager").build();
        ResponseEntity<BeerDTO> responseEntity = beerController.createAndSaveBeer(beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        String[] UUIDLocations = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID savedUuid = UUID.fromString(UUIDLocations[3].substring(4));
        System.out.println(savedUuid);
        Beer beer = beerRepository.findById(savedUuid).orElse(null);
        assertThat(beer).isNotNull();
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
    }

    @Test
    void getBeerByIdNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void getBeerList() {
        Page<BeerDTO> dtos = beerController
                .listBeers(null, null, false, 1, 25);
        assertThat(dtos.getContent().size()).isEqualTo(25);
    }

    @Transactional
    @Rollback
    @Test
    void getEmptyList() {
        beerRepository.deleteAll();
        Page<BeerDTO> dtos = beerController
                .listBeers(null, null, false, 1, 25);
        assertThat(dtos.getContent().size()).isEqualTo(0);
    }
}