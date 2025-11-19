package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 * 
 * Additional controller annotations:
 * @PutMapping - Maps HTTP PUT requests (typically for updates)
 * @GetMapping with @RequestBody - GET request with request body (unusual but valid)
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;
    
    /**
     * Constructor injection for all required services.
     */
    @Autowired
    public UserController(CustomerService customerService, 
                         EmployeeService employeeService,
                         PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    /**
     * Save a new customer.
     * 
     * HTTP POST /user/customer
     * Request body: CustomerDTO (JSON)
     * Response: CustomerDTO (JSON) with generated ID
     */
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        // throw new UnsupportedOperationException();
        // Replaced with service layer call:
        // - Converts DTO to entity for business logic
        // - Uses service for validation and persistence
        // - Returns DTO with generated ID for API response
        
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        Customer savedCustomer = customerService.saveCustomer(customer);
        return convertCustomerEntityToDTO(savedCustomer);
    }

    /**
     * Get all customers.
     * 
     * HTTP GET /user/customer
     * Response: List<CustomerDTO> (JSON array)
     */
    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        // throw new UnsupportedOperationException();
        // Replaced with service call to retrieve all customers
        // Converts list of entities to list of DTOs using streams
        
        List<Customer> customers = customerService.getAllCustomers();
        return customers.stream()
                .map(this::convertCustomerEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get the owner (customer) of a specific pet.
     * 
     * HTTP GET /user/customer/pet/{petId}
     * Path variable: petId (long)
     * Response: CustomerDTO (JSON)
     */
    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        // throw new UnsupportedOperationException();
        // Replaced with logic to:
        // - Find pet by ID using pet service
        // - Get the owner from pet's owner relationship
        // - Convert owner entity to DTO
        
        Pet pet = petService.getPetById(petId);
        Customer owner = pet.getOwner();
        return convertCustomerEntityToDTO(owner);
    }

    /**
     * Save a new employee.
     * 
     * HTTP POST /user/employee
     * Request body: EmployeeDTO (JSON)
     * Response: EmployeeDTO (JSON) with generated ID
     */
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // throw new UnsupportedOperationException();
        // Replaced with service layer integration
        // Handles skills and availability collections properly
        
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return convertEmployeeEntityToDTO(savedEmployee);
    }

    /**
     * Get an employee by ID.
     * 
     * HTTP POST /user/employee/{employeeId} (Note: Should probably be GET)
     * Path variable: employeeId (long)
     * Response: EmployeeDTO (JSON)
     * 
     * Note: The original mapping uses POST, which is unusual for a read operation.
     * Typically this would be GET, but keeping original mapping for compatibility.
     */
    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call to get employee by ID
        
        Employee employee = employeeService.getEmployeeById(employeeId);
        return convertEmployeeEntityToDTO(employee);
    }

    /**
     * Set an employee's availability.
     * 
     * HTTP PUT /user/employee/{employeeId}
     * Path variable: employeeId (long)
     * Request body: Set<DayOfWeek> (JSON array)
     * Response: void (HTTP 200 OK)
     * 
     * @PutMapping indicates this is an update operation
     */
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call to update employee availability
        // Service handles validation and persistence
        
        employeeService.setEmployeeAvailability(employeeId, daysAvailable);
    }

    /**
     * Find employees available for service based on date and required skills.
     * 
     * HTTP GET /user/employee/availability
     * Request body: EmployeeRequestDTO (JSON) - contains date and skills
     * Response: List<EmployeeDTO> (JSON array)
     * 
     * Note: GET with request body is unusual but valid.
     * Could be refactored to use query parameters instead.
     */
    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        // throw new UnsupportedOperationException();
        // Replaced with service call that finds employees matching criteria:
        // - Available on the requested date
        // - Have all required skills
        
        List<Employee> availableEmployees = employeeService.findEmployeesForService(
            employeeDTO.getDate(), 
            employeeDTO.getSkills()
        );
        
        return availableEmployees.stream()
                .map(this::convertEmployeeEntityToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert CustomerDTO to Customer entity (manual mapping).
     */
    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId() != 0 ? customerDTO.getId() : null);
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        // Note: Pets relationship is managed separately in the pet operations
        return customer;
    }
    
    /**
     * Convert Customer entity to CustomerDTO (manual mapping).
     */
    private CustomerDTO convertCustomerEntityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());
        
        // Convert pets list to pet IDs list
        if (customer.getPets() != null) {
            List<Long> petIds = customer.getPets().stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        
        return customerDTO;
    }
    
    /**
     * Convert EmployeeDTO to Employee entity (manual mapping).
     */
    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId() != 0 ? employeeDTO.getId() : null);
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        return employee;
    }
    
    /**
     * Convert Employee entity to EmployeeDTO (manual mapping).
     */
    private EmployeeDTO convertEmployeeEntityToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return employeeDTO;
    }
}
