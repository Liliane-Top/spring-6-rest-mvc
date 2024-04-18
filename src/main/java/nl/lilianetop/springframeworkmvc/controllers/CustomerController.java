package nl.lilianetop.springframeworkmvc.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.CustomerDTO;
import nl.lilianetop.springframeworkmvc.services.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static nl.lilianetop.springframeworkmvc.utils.Constants.CUSTOMER_URL;
import static nl.lilianetop.springframeworkmvc.utils.Constants.CUSTOMER_URL_ID;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService service;

    @GetMapping(value = CUSTOMER_URL)
    public List<CustomerDTO> customerList() {
        return service.listCustomers();
    }

    @GetMapping(value = CUSTOMER_URL_ID)
    public CustomerDTO getCustomerById(@PathVariable(value = "customerId") UUID customerId) {
        log.debug("Get Customer by id - in controller");
        return service.getCustomerById(customerId).orElseThrow(ExceptionNotFound::new);
    }

    @PostMapping(value = CUSTOMER_URL)
    public ResponseEntity<CustomerDTO> createAndSaveCustomer(@RequestBody CustomerDTO newCustomer) {
        CustomerDTO savedCustomer = service.createAndSaveCustomer(newCustomer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", CUSTOMER_URL + savedCustomer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = {CUSTOMER_URL_ID})
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer) {
        if (service.updateCustomerById(id, customer).isEmpty()) {
            throw new ExceptionNotFound();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = {CUSTOMER_URL_ID})
    public ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable("customerId") UUID id) {
        if (!service.deleteCustomerById(id)) {
            throw new ExceptionNotFound();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = {CUSTOMER_URL_ID})
    public ResponseEntity<CustomerDTO> patchCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer) {
        if (service.patchCustomerById(id, customer).isEmpty()) {
            throw new ExceptionNotFound();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
