package ru.kata.spring.boot_security.demo.service;



import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ru.kata.spring.boot_security.demo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> showAllEmployee();

    Employee getEmployeeById(int id);

    void save(Employee employee);

    Employee update(Employee employee);

    void delete(int id);

    Employee getAuthenticationPrincipal (@AuthenticationPrincipal Employee employee);
}
