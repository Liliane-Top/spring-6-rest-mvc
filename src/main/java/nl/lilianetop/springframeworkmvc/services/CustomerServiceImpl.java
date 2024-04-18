package nl.lilianetop.springframeworkmvc.services;

import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.models.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Liliane")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Krystina")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Ton")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("Get Customer by Id - in service. Id: " + id.toString());
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerMap.values().stream().toList();
    }

    @Override
    public CustomerDTO createAndSaveCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .customerName(customer.getCustomerName())
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(1)
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(id);
        existing.setCustomerName(customer.getCustomerName());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        customerMap.remove(id);
        return true;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(id);

        if (StringUtils.hasText(customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }
        return Optional.of(existing);
    }
}
