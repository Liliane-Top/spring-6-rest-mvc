package nl.lilianetop.springframeworkmvc.services;

import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.models.CustomerDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDto> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDto customer1 = CustomerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Liliane")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDto customer2 = CustomerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Krystina")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDto customer3 = CustomerDto.builder()
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
    public Optional<CustomerDto> getCustomerById(UUID id) {
        log.debug("Get Customer by Id - in service. Id: " + id.toString());
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return customerMap.values().stream().toList();
    }

    @Override
    public CustomerDto createAndSaveCustomer(CustomerDto customer) {
       CustomerDto savedCustomer =  CustomerDto.builder()
                .customerName(customer.getCustomerName())
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(1)
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer );
        return savedCustomer;
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer) {
        CustomerDto existing =  customerMap.get(id);
        existing.setCustomerName(customer.getCustomerName());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        customerMap.remove(id);
        return true;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer) {
        CustomerDto existing = customerMap.get(id);

        if(StringUtils.hasText( customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }
        return Optional.of(existing);
    }
}
