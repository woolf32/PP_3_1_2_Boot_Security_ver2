package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.EmployeeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final RoleRepository roleRepository;


    public AdminController(ru.kata.spring.boot_security.demo.service.EmployeeService employeeService, RoleRepository roleRepository) {
        this.employeeService = employeeService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String showAllEmployee(Model model) {
        List<Employee> employees = employeeService.showAllEmployee();
        if (employees.isEmpty()) {
            System.out.println("EMPTY");
        }
        model.addAttribute("employees", employeeService.showAllEmployee());
        return "inf";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@RequestParam("id") int id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "inf";
        /*return "showId";*/
    }

    @GetMapping("/new")
    public String newEmp(Model model) {
        model.addAttribute("employee", new Employee());
        return "new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employee") Employee employee) {
        employeeService.save(employee);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editEmployee(Model model, @RequestParam("id") int id) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") Employee employee, @RequestParam("id") int id,
                         @RequestParam List<String> listRoles) {
        Set<Role> roles = listRoles.stream()
                .map(roleName -> roleRepository.findByName(roleName))
                .collect(Collectors.toSet());
        employee.setRoles(roles);
        employeeService.update(id, employee);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String delete(@RequestParam("id") int id) {
        employeeService.delete(id);
        return "redirect:/admin";
    }
}
