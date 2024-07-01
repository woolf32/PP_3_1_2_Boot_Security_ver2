package ru.kata.spring.boot_security.demo;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.Employee;

import java.util.List;

public interface EmployeeService extends UserDetailsService{
    List<Employee> showAllEmployee ();
    Employee getEmployeeById (int id);
    void save(Employee employee);
    void update(int id, Employee employee);
    void delete(int id);
}
