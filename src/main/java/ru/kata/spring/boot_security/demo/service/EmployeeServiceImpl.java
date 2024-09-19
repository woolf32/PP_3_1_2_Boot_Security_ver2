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
    public void update(Employee employee) {
        // Находим пользователя по ID
        Employee savedEmployee = employeeRepository.findById(employee.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employee.getId()));

        // Обновляем пароль, если он был передан
        if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
            savedEmployee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        }

        // Обновляем основные данные
        savedEmployee.setName(employee.getName());
        savedEmployee.setSurname(employee.getSurname());
        savedEmployee.setSalary(employee.getSalary());
        savedEmployee.setDepartment(employee.getDepartment());

        // Инициализируем ленивую коллекцию ролей
        savedEmployee.getRoles().size(); // Инициализация ленивой коллекции

        // Обновляем роли, полученные с фронта
        if (employee.getRoles() != null) {
            // Удаляем старые роли
            savedEmployee.getRoles().clear();

            // Добавляем новые роли из запроса
            savedEmployee.getRoles().addAll(employee.getRoles());
        }

        // Сохраняем обновленного пользователя
        employeeRepository.save(savedEmployee);
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
