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
import java.util.Set;


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
    public void save(Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        Set<Role> detachedRoles = employee.getRoles();
        Set<Role> attachedRoles = new HashSet<>();
        for (Role role : detachedRoles) {
            attachedRoles.add(roleRepository.findById(role.getId()).orElse(null));
        }
        employee.setRoles(attachedRoles);
        employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void update(Employee employee ) {
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() ->
                new RuntimeException("Employee not found with id: " + employee.getId()));
       savedEmployee.updateFrom(employee,bCryptPasswordEncoder);
        Set<Role> updatedRoles  = new HashSet<>();
        for (Role role : employee.getRoles()) {
            updatedRoles.add(roleRepository.findById(role.getId()).orElse(null));
        }
        savedEmployee.setRoles(updatedRoles);
        employeeRepository.save(savedEmployee);

    }

    @Transactional
    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

}
