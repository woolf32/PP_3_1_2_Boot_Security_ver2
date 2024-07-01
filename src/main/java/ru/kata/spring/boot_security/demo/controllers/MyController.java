package ru.podlesnykh.spring_mvc_hibernate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.podlesnykh.spring_mvc_hibernate.entity.Employee;
import ru.podlesnykh.spring_mvc_hibernate.services.EmployeeService;

@Controller
@RequestMapping("/employee")
public class MyController {

    private final EmployeeService employeeService;

    @Autowired
    public MyController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String showAllEmployee (Model model){
        model.addAttribute("employee",employeeService.showAllEmployee());
        return "employee/infoEmployee";
    }

    @GetMapping ("/{id}")
    public String getEmployeeById (@RequestParam("id") int id, Model model){
        model.addAttribute("employee",employeeService.getEmployeeById(id));
        return "employee/showId";
    }

    @GetMapping("/new")
    public String newEmp (Model model){
        model.addAttribute("employee",new Employee());
        return "employee/new";
    }

    @PostMapping()
    public String create (@ModelAttribute("employee") Employee employee){
        employeeService.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("/{id}/edit")
    public String editEmployee ( Model model, @RequestParam("id") int id){
        model.addAttribute("employee",employeeService.getEmployeeById(id));
        return "employee/edit";
    }
    @PatchMapping("/{id}")
    public String update (@ModelAttribute("employee") Employee employee, @RequestParam("id") int id){
        employeeService.update(id,employee);
        return "redirect:/employee";
    }

    @PostMapping ("/{id}")
    public String delete (@RequestParam("id") int id){
        employeeService.delete(id);
        return "redirect:/employee";
    }
}
