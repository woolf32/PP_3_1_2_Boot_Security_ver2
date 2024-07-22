package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RoleRepository roleRepository;


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
        employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void update(int id, Employee employee) {
        em.merge(employee);
    }

    @Transactional
    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public void updateRoles(int id, Employee updatedEmployee) {
        Employee employee = getEmployeeById(id);

        employee.setName(updatedEmployee.getName());
        employee.setSurname(updatedEmployee.getSurname());
        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setSalary(updatedEmployee.getSalary());
        employee.setPassword(updatedEmployee.getPassword());

        Set<Role> roles = updatedEmployee.getRoles().stream()
                .map(role -> roleRepository.findByName(role.getName()))
                .collect(Collectors.toSet());

        employee.setRoles(roles);
        save(employee);
    }

}
