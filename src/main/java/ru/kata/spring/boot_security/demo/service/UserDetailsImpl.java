package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;

import java.util.Optional;

@Service
public class UserDetailsImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public UserDetailsImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = Optional.ofNullable(employeeRepository.getUserByUsername(username))
                .orElseThrow();
        return user;
    }
}
