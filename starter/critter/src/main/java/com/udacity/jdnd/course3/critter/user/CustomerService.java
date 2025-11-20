package com.udacity.jdnd.course3.critter.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service layer for customer business logic
@Service
public class CustomerService {
    
    // Repository dependency for database operations
    @Autowired
    private CustomerRepository customerRepository;
    
    // Saves a new customer to the database
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    // Retrieves all customers from the database
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}