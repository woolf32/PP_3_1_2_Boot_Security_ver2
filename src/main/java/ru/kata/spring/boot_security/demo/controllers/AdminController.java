package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.service.EmployeeService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final RoleService roleService;


    public AdminController(ru.kata.spring.boot_security.demo.service.EmployeeService employeeService, RoleService roleService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getUsers(Model model, @AuthenticationPrincipal Employee employee) {
        model.addAttribute("employee", employee);
        model.addAttribute("employeeList", employeeService.showAllEmployee());
        model.addAttribute("roles", roleService.findAll());
        return "mainPage";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@RequestParam("id") int id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "showId";
    }

    @GetMapping ("/create")
    public String newEmp(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("allRoles", roleService.findAll());
        return "new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("employee") Employee employee, BindingResult bindingResult,
                         @RequestParam ("selectedRoles") List <Integer> selectedRoles) {
        if (bindingResult.hasErrors()) {
            return "new";
        }
        employeeService.save(employee,selectedRoles);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editEmployee(Model model, @RequestParam("id") int id) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        model.addAttribute("allRoles", roleService.findAll());
        return  "edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("employee") Employee employee, BindingResult bindingResult,
                         @RequestParam("id") int id, @RequestParam ("selectedRoles") List <Integer> selectedRoles) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        employeeService.update(id, employee, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        employeeService.delete(id);
        return "redirect:/admin";
    }
}
