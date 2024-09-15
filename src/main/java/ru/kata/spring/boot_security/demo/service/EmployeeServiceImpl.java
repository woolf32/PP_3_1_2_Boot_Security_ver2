package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    final EmployeeRepository employeeRepository;
    final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public Employee update(Employee employee) {
        System.out.println("Updating employee: " + employee); // Добавьте лог

        Employee employee1 = employeeRepository.findById(employee.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + employee.getId()));

        // Логируйте роли
        System.out.println("Received roles: " + employee1.getRoles());

        Set<Role> roles = new HashSet<>();
        for (Role role : employee.getRoles()) {
            Role existingRole = roleRepository.findRoleByid(role.getId()); // Проверка роли по ID
            if (existingRole != null) {
                roles.add(existingRole);
            } else {
                if (role.getName() == null) {
                    throw new IllegalArgumentException("Role name cannot be null for role ID: " + role.getId());
                }
                roles.add(role); // Добавляем новую роль с именем
            }
        }
        employee1.setRoles(roles);

        if (!employee.getPassword().isEmpty() && !employee.getPassword().equals(employee1.getPassword())) {
            employee1.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        }


        System.out.println("Employee updated: " + employee1); // Логируем обновлённого пользователя

        return employeeRepository.save(employee1);
    }

    @Transactional
    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Employee getAuthenticationPrincipal( @AuthenticationPrincipal Employee employee) {
        return employee;
    }

}
