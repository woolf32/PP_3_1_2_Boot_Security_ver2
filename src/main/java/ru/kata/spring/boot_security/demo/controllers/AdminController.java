package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.service.EmployeeService;



@Controller
@RequestMapping("/admin")
public class AdminController {

    private EmployeeService employeeService;

    public AdminController(ru.kata.spring.boot_security.demo.service.EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String showAllEmployee(Model model) {
        model.addAttribute("employee", employeeService.showAllEmployee());
        return "infoEmployee";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@RequestParam("id") int id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "showId";
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
    public String update(@ModelAttribute("employee") Employee employee, @RequestParam("id") int id) {
        employeeService.update(id, employee);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String delete(@RequestParam("id") int id) {
        employeeService.delete(id);
        return "redirect:/admin";
    }
}
