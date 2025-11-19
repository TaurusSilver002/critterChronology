package com.udacity.jdnd.course3.critter.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomerService handles business logic for Customer operations.
 * 
 * Annotations explained:
 * @Service - Marks this class as a service component in Spring's IoC container
 *          Spring will automatically create and manage an instance of this class
 * @Transactional - Ensures database operations are atomic (all succeed or all fail)
 *                 Important for maintaining data consistency
 * @Autowired - Tells Spring to automatically inject dependencies
 *            Spring finds CustomerRepository bean and injects it here
 */
@Service
@Transactional
public class CustomerService {
    
    /**
     * Repository dependency injection
     * @Autowired tells Spring to find and inject a CustomerRepository bean
     * Spring Data JPA automatically creates the repository implementation
     */
    private final CustomerRepository customerRepository;
    
    /**
     * Constructor injection (preferred over field injection)
     * @Autowired on constructor is optional when there's only one constructor
     * 
     * Benefits of constructor injection:
     * - Ensures dependencies are provided at object creation
     * - Makes testing easier (can provide mocks)
     * - Makes dependencies immutable (final fields)
     */
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    /**
     * Save a new customer to the database.
     * 
     * Business logic:
     * 1. Validate customer data (if needed)
     * 2. Save to database via repository
     * 3. Return saved customer with generated ID
     * 
     * @param customer The customer to save (ID will be generated)
     * @return Saved customer with generated ID
     */
    public Customer saveCustomer(Customer customer) {
        // Could add validation here:
        // if (customer.getName() == null || customer.getName().trim().isEmpty()) {
        //     throw new IllegalArgumentException("Customer name is required");
        // }
        
        return customerRepository.save(customer);
    }
    
    /**
     * Find a customer by their ID.
     * 
     * Uses Optional to handle cases where customer doesn't exist:
     * - Optional.empty() if not found
     * - Optional.of(customer) if found
     * 
     * @param customerId The ID of the customer to find
     * @return Optional containing the customer if found, empty otherwise
     */
    public Optional<Customer> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }
    
    /**
     * Get a customer by ID, throwing exception if not found.
     * 
     * Convenience method for cases where customer must exist.
     * 
     * @param customerId The ID of the customer to get
     * @return The customer entity
     * @throws RuntimeException if customer not found
     */
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }
    
    /**
     * Get all customers in the system.
     * 
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Update an existing customer.
     * 
     * Business logic:
     * 1. Verify customer exists
     * 2. Update the customer data
     * 3. Save changes to database
     * 
     * @param customerId The ID of customer to update
     * @param updatedCustomer The updated customer data
     * @return Updated customer entity
     * @throws RuntimeException if customer not found
     */
    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerById(customerId);
        
        // Update fields (could use BeanUtils.copyProperties for more fields)
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existingCustomer.setNotes(updatedCustomer.getNotes());
        
        return customerRepository.save(existingCustomer);
    }
    
    /**
     * Delete a customer by ID.
     * 
     * Note: This will also delete all associated pets due to cascade settings
     * in the Customer entity (@OneToMany with orphanRemoval = true)
     * 
     * @param customerId The ID of customer to delete
     * @throws RuntimeException if customer not found
     */
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }
    
    /**
     * Check if a customer exists by ID.
     * 
     * @param customerId The ID to check
     * @return true if customer exists, false otherwise
     */
    public boolean customerExists(Long customerId) {
        return customerRepository.existsById(customerId);
    }
    
    /**
     * Get the total number of customers.
     * 
     * @return Total count of customers
     */
    public long getCustomerCount() {
        return customerRepository.count();
    }
}