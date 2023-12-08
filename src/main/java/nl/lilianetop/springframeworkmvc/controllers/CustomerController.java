package nl.lilianetop.springframeworkmvc.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.Customer;
import nl.lilianetop.springframeworkmvc.services.CustomerService;
import nl.lilianetop.springframeworkmvc.utils.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService service;

    @GetMapping(value = Constants.CUSTOMER_URL)
    public List<Customer> customerList(){
        return service.listCustomers();
    }

    @GetMapping(value = Constants.CUSTOMER_URL_ID)
    public Customer getCustomerById(@PathVariable(value = "customerId") UUID customerId){
        log.debug("Get Customer by id - in controller");
        return service.getCustomerById(customerId);
    }

    @PostMapping(value = Constants.CUSTOMER_URL)
    public ResponseEntity<Customer> createAndSaveCustomer(@RequestBody Customer newCustomer){
        Customer savedCustomer = service.createAndSaveCustomer(newCustomer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", Constants.CUSTOMER_URL +  savedCustomer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    @PutMapping(value = {Constants.CUSTOMER_URL_ID})
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") UUID id, @RequestBody Customer customer){
        service.updateCustomerById(id, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = {Constants.CUSTOMER_URL_ID})
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable("customerId") UUID id) {
        service.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = {Constants.CUSTOMER_URL_ID})
    public ResponseEntity<Customer> patchCustomerById(@PathVariable("customerId") UUID id, @RequestBody Customer customer) {
    service.patchCustomerById(id, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
