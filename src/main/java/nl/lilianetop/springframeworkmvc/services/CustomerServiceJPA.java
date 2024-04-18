package nl.lilianetop.springframeworkmvc.services;

import lombok.RequiredArgsConstructor;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.mappers.CustomerMapper;
import nl.lilianetop.springframeworkmvc.models.CustomerDTO;
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
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto)
                .orElseThrow(ExceptionNotFound::new));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .toList();
    }

    @Override
    public CustomerDTO createAndSaveCustomer(CustomerDTO customerDto) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customerDto)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDto) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
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
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID id, CustomerDTO customerDto) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(

                customer -> {
                    if (StringUtils.hasText(customer.getCustomerName())) {
                        customer.setCustomerName(customerDto.getCustomerName());
                    }
                    atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customer))));
                },
                () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }
}
