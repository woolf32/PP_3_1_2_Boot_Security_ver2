package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public EmployeeServiceImpl() {
    }

    @Transactional
    @Override
    public List<Employee> showAllEmployee() {
        return employeeRepository.findAll();
    }

    @Transactional
    @Override
    public Employee getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(new Employee());
    }

    @Transactional
    @Override
    public void save(Employee employee, List <Integer> selectedRoles) {
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        List<Role> roles = roleRepository.findAllById(selectedRoles);
        savedEmployee.setRoles(new HashSet<>(roles));
        employeeRepository.save(savedEmployee);
    }

    @Transactional
    @Override
    public void update(int id, Employee employee, List <Integer> selectedRoles) {
        Employee savedEmployee = employeeRepository.getById(id);
        savedEmployee.setName(employee.getName());
        savedEmployee.setSurname(employee.getSurname());
        savedEmployee.setDepartment(employee.getDepartment());
        savedEmployee.setSalary(employee.getSalary());
        savedEmployee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        List<Role> roles = roleRepository.findAllById(selectedRoles);
        savedEmployee.setRoles(new HashSet<>(roles));
        employeeRepository.save(savedEmployee);

    }

    @Transactional
    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

}
