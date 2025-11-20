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

// REST Controller for user-related operations (customers and employees)
@RestController
@RequestMapping("/user") // Base URL path for all user endpoints
public class UserController {
    
    // Service dependencies for business logic
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;
    
    // Constructor injection of all required services
    @Autowired
    public UserController(CustomerService customerService, 
                         EmployeeService employeeService,
                         PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    // POST endpoint: Creates a new customer
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        Customer savedCustomer = customerService.saveCustomer(customer);
        return convertCustomerEntityToDTO(savedCustomer);
    }

    // GET endpoint: Retrieves all customers
    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return customers.stream()
                .map(this::convertCustomerEntityToDTO)
                .collect(Collectors.toList());
    }

    // GET endpoint: Finds the owner (customer) of a specific pet
    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.getPetById(petId);
        Customer owner = pet.getOwner();
        // Force loading of pets collection to ensure consistency
        owner.getPets().size();
        return convertCustomerEntityToDTO(owner);
    }

    // POST endpoint: Creates a new employee
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return convertEmployeeEntityToDTO(savedEmployee);
    }

    // POST endpoint: Retrieves a specific employee by ID
    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return convertEmployeeEntityToDTO(employee);
    }

    // PUT endpoint: Updates an employee's availability schedule
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(employeeId, daysAvailable);
    }

    // GET endpoint: Finds employees available for specific service requirements
    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> availableEmployees = employeeService.findEmployeesForService(
            employeeDTO.getDate(), 
            employeeDTO.getSkills()
        );
        
        return availableEmployees.stream()
                .map(this::convertEmployeeEntityToDTO)
                .collect(Collectors.toList());
    }
    
    // Helper method: Converts CustomerDTO (API layer) to Customer entity (database layer)
    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId() != 0 ? customerDTO.getId() : null);
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        return customer;
    }
    
    // Helper method: Converts Customer entity (database layer) to CustomerDTO (API layer)
    private CustomerDTO convertCustomerEntityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());
        
        // Include pet IDs if customer has pets (lazy loading consideration)
        if (customer.getPets() != null) {
            List<Long> petIds = customer.getPets().stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        
        return customerDTO;
    }
    
    // Helper method: Converts EmployeeDTO (API layer) to Employee entity (database layer)
    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId() != 0 ? employeeDTO.getId() : null);
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        return employee;
    }
    
    // Helper method: Converts Employee entity (database layer) to EmployeeDTO (API layer)
    private EmployeeDTO convertEmployeeEntityToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return employeeDTO;
    }
}
