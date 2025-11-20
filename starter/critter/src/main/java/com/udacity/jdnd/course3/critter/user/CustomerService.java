package com.udacity.jdnd.course3.critter.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}