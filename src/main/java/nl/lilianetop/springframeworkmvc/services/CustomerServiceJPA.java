package nl.lilianetop.springframeworkmvc.services;

import lombok.RequiredArgsConstructor;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.mappers.CustomerMapper;
import nl.lilianetop.springframeworkmvc.models.CustomerDto;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        return Optional.ofNullable(customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto)
                .orElseThrow(ExceptionNotFound::new));
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .toList();
    }

    @Override
    public CustomerDto createAndSaveCustomer(CustomerDto customerDto) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customerDto)));
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customerDto) {
        AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(
                customer -> {
                    customer.setCustomerName(customerDto.getCustomerName());
                    atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customer))));
                },
                () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        if(customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customerDto) {
        AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(

                customer -> {
                    if(StringUtils.hasText(customer.getCustomerName())){
                        customer.setCustomerName(customerDto.getCustomerName());
                    }
                    atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customer))));
                },
                () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }
}
