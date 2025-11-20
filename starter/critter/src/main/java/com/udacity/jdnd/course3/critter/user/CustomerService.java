package com.udacity.jdnd.course3.critter.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service layer for customer business logic
@Service
@Transactional // Ensures database operations are atomic
public class CustomerService {
    
    // Repository dependency for database operations
    private final CustomerRepository customerRepository;
    
    // Constructor injection of dependencies
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    // Saves a new customer to the database
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    // Retrieves all customers from the database
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}