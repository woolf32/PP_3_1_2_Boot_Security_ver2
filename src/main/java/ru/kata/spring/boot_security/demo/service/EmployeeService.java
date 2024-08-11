package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> showAllEmployee();

    Employee getEmployeeById(int id);

    void save(Employee employee, List <Integer> selectedRoles);

    void update(int id, Employee employee, List <Integer> selectedRoles);

    void delete(int id);
}
