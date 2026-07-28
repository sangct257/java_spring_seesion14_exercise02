package ra.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.demo.entity.Employee;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @GetMapping
    public List<Employee> getAll() {
        return List.of(
                new Employee(1L, "Nguyen Van A", 10000.0),
                new Employee(1L, "Nguyen Van B", 20000.0),
                new Employee(1L, "Nguyen Van C", 30000.0)
                );
    }
}
