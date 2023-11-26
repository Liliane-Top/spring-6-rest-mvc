package nl.lilianetop.springframeworkmvc.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.lilianetop.springframeworkmvc.controllers.CustomerController;
import nl.lilianetop.springframeworkmvc.models.Customer;
import nl.lilianetop.springframeworkmvc.services.CustomerService;
import nl.lilianetop.springframeworkmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService service;

    CustomerServiceImpl customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl();
    }


    @Test
    void call_listCustomers() throws Exception {
        given(service.listCustomers()).willReturn(customerService.listCustomers());

        mockMvc.perform(get("/api/v1/customer")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));


    }

    @Test
    void call_customerById() throws Exception {
        Customer testCustomer = customerService.listCustomers().get(0);

        given(service.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));
    }

    @Test
    void call_createAndSaveCustomer() throws Exception {
        Customer testCustomer = customerService.listCustomers().get(0);
        testCustomer.setId(null);
        testCustomer.setVersion(null);

        given(service.createAndSaveCustomer(any(Customer.class))).willReturn(customerService.listCustomers().get(1));

        mockMvc.perform(post("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void call_updateCustomer() throws Exception {
        Customer testCustomer = customerService.listCustomers().get(0);
        mockMvc.perform(put("/api/v1/customer/" + testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isNoContent());

        verify(service).updateCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
        assertThat(customerArgumentCaptor.getValue()).isEqualTo(testCustomer);
    }

    @Test
    void call_patchCustomer() throws Exception {
        Customer testCustomer = customerService.listCustomers().get(0);

        Map<String, Object> updatedMap = new HashMap<>();
        //The key of this map MUST be exactly the same as the field you want to update.
        //I want to update Customer's name which fiels is called 'customerName'
        //jackson creates a json from this map which will be used as body of the request.
        updatedMap.put("customerName", "updated name");

        mockMvc.perform(patch("/api/v1/customer/" + testCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMap)))
                .andExpect(status().isNoContent());


        verify(service).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        System.out.println(customerArgumentCaptor.getValue());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(updatedMap.get("customerName"));
    }

    @Test
    void call_deleteCustomer() throws Exception{
        Customer testCustomer = customerService.listCustomers().get(0);

        mockMvc.perform(delete("/api/v1/customer/" + testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

//        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(service).deleteCustomerById(uuidArgumentCaptor.capture());

        Assertions.assertEquals(uuidArgumentCaptor.getValue(), testCustomer.getId());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
    }


}
