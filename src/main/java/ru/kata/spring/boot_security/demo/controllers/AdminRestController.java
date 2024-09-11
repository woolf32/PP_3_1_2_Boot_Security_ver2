package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.service.EmployeeService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://localhost:63343")
public class AdminRestController {

    private final EmployeeService employeeService;
    private final RoleService roleService;

    public AdminRestController(EmployeeService employeeService, RoleService roleService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    @GetMapping()
    public List <Employee> getUsers() {
        return employeeService.showAllEmployee();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employee;
    }

    @PostMapping("/new")
    public Employee create(@RequestBody Employee employee) {
        employeeService.save(employee);
        return employee;
    }

    @PutMapping("/edit/{id}")
    public Employee update(@PathVariable int id, @RequestBody Employee employee) {
        employee.setId(id);
        employeeService.update(employee);
        return employee;
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        employeeService.delete(id);
    }
}