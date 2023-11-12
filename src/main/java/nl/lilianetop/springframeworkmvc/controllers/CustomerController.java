package nl.lilianetop.springframeworkmvc.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.models.Customer;
import nl.lilianetop.springframeworkmvc.services.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/customer")
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService service;

    @GetMapping()
    public List<Customer> customerList(){
        return service.listCustomers();
    }

    @GetMapping(value = "{customerId}")
    public Customer getCustomerById(@PathVariable(value = "customerId") UUID customerId){
        log.debug("Get Customer by id - in controller");
        return service.getCustomerById(customerId);
    }


}
