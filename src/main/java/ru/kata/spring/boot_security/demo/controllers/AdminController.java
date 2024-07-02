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
import ru.kata.spring.boot_security.demo.service.EmployeeServiceImpl;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmployeeServiceImpl employeeserviceimpl;

    @GetMapping()
    public String showAllEmployee(Model model) {
        model.addAttribute("employee", employeeserviceimpl.showAllEmployee());
        return "infoEmployee";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@RequestParam("id") int id, Model model) {
        model.addAttribute("employee", employeeserviceimpl.getEmployeeById(id));
        return "showId";
    }

    @GetMapping("/new")
    public String newEmp(Model model) {
        model.addAttribute("employee", new Employee());
        return "new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employee") Employee employee) {
        employeeserviceimpl.save(employee);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editEmployee(Model model, @RequestParam("id") int id) {
        model.addAttribute("employee", employeeserviceimpl.getEmployeeById(id));
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") Employee employee, @RequestParam("id") int id) {
        employeeserviceimpl.update(id, employee);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String delete(@RequestParam("id") int id) {
        employeeserviceimpl.delete(id);
        return "redirect:/admin";
    }
}
