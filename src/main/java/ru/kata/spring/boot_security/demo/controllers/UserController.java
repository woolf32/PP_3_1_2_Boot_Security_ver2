package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.EmployeeServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    EmployeeRepository employeeRepository;


    @GetMapping()
    public String dashboardPageList(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Employee employee = (Employee) employeeRepository.getUserByUsername(currentUser.getUsername());
        model.addAttribute("currentEmployee", employee);

        return "infoEmpOne";
    }

}
