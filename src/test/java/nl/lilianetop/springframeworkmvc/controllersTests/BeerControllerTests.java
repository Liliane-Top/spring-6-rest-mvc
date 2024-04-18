package nl.lilianetop.springframeworkmvc.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.lilianetop.springframeworkmvc.config.SpringSecurityConfig;
import nl.lilianetop.springframeworkmvc.controllers.BeerController;
import nl.lilianetop.springframeworkmvc.models.BeerDTO;
import nl.lilianetop.springframeworkmvc.services.BeerService;
import nl.lilianetop.springframeworkmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static nl.lilianetop.springframeworkmvc.Constants.PASSWORD;
import static nl.lilianetop.springframeworkmvc.Constants.USER;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL;
import static nl.lilianetop.springframeworkmvc.utils.Constants.BEER_URL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@Import(SpringSecurityConfig.class)
class BeerControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BeerService beerService;
    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<BeerDTO> beerDtoArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }


    @Test
    void createBeerWithoutBeerName() throws Exception {
        BeerDTO beerDto = BeerDTO.builder().build();
        when(beerService.saveNewBeer(any())).thenReturn(beerServiceImpl.listBeers(null, null, false,
                1, 25).getContent().get(0));

        MvcResult result = mockMvc.perform(post(BEER_URL)
                        .with(httpBasic(USER, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void createBeerWithValidation() throws Exception {
        BeerDTO beerDto = BeerDTO.builder().build();
        when(beerService.saveNewBeer(any())).thenReturn(beerServiceImpl.listBeers(null, null, false,
                1, 25).getContent().get(0));

        MvcResult result = mockMvc.perform(post(BEER_URL)
                        .with(httpBasic(USER, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    void updateBeerWithBlankName() throws Exception {
        BeerDTO beerDto = beerServiceImpl
                .listBeers(null, null, false, 1, 25)
                .getContent().get(0);
        beerDto.setBeerName("");
        when(beerService.updateBeerById(any(), any())).thenReturn(Optional.of(beerDto));

        MvcResult result = mockMvc.perform(put(BEER_URL_ID, beerDto.getId())
                        .with(httpBasic(USER, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    void patchBeer() throws Exception {
        BeerDTO beer = beerServiceImpl
                .listBeers(null, null, false, 1, 25)
                .getContent().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        when(beerService.patchBeerById(any(), any())).thenReturn(Optional.ofNullable(beer));

        assert beer != null;
        mockMvc.perform(patch(BEER_URL_ID, beer.getId())
                        .with(httpBasic(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(),
                beerDtoArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerDtoArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.listBeers(null, null, false, 0, 25));

        mockMvc.perform(get(BEER_URL)
                        .with(httpBasic(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));

    }

    @Test
    void deleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25)
                .getContent().get(0);

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BEER_URL_ID, beer.getId())
                        .with(httpBasic(USER, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BEER_URL_ID, UUID.randomUUID())
                        .with(httpBasic(USER, PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImpl
                .listBeers(null, null, false, 1, 25)
                .getContent().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_URL_ID, testBeer.getId())
                        .with(httpBasic(USER, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}