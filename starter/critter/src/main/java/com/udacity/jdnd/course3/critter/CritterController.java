package com.udacity.jdnd.course3.critter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller to verify Spring Boot application startup.
 * Provides a simple endpoint to confirm the application is running.
 * Not part of the main application functionality.
 */
@RestController // @RestController = @Controller + @ResponseBody for REST endpoints
public class CritterController {

    // Simple GET endpoint for application health check
    @GetMapping("/test")
    public String test(){
        return "Critter Starter installed successfully";
    }
}
