package nl.lilianetop.springframeworkmvc.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.models.Customer;
import nl.lilianetop.springframeworkmvc.services.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Customer> createAndSaveCustomer(@RequestBody Customer newCustomer){
        Customer savedCustomer = service.createAndSaveCustomer(newCustomer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "api/v1/customer/" + savedCustomer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    @PutMapping(value = {"{customerId}"})
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") UUID id, @RequestBody Customer customer){
        service.updateCustomerById(id, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = {"{customerId}"})
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable("customerId") UUID id) {
        service.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
