package ru.kata.spring.boot_security.demo.test;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DataInitializer implements ApplicationRunner {

    EmployeeRepository employeeRepository;

    @PersistenceContext
    EntityManager em;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        Employee emp1 = new Employee("username1", "$2a$12$wGFtp/eYWceVZTuAwNq1duwt576aqaH99.4F0l5ge9fgqRzqjstgy", "surname1", "IT", 221);
        Employee emp2 = new Employee("username2", "$2a$12$rOz6UF27rad7ya2rHDE6cuas1kxr9qVaUFbb3qRINNVSSfD9BpxqS", "surname2", "HT", 523);
        Employee emp3 = new Employee("username3", "$2a$12$gE5HmgQmo0gwlgkm9Alj9eNu161STSqRXyCT86DimXZMMGxsTU5r6", "surname3", "DG", 645);
        Employee emp4 = new Employee("username4", "$2a$12$qiTnZ1zJaqNRwY.JboPh.eh/9Om9YEqvyJsEcQbWlP2gHl8xSFcMm", "surname4", "WQ", 234);
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        /*employeeRepository.save(emp3);
        employeeRepository.save(emp4);*/
        em.createNativeQuery("INSERT INTO roles (role_id,name) values (1,'ADMIN')").executeUpdate();
        em.createNativeQuery("INSERT INTO roles (role_id,name) values (2,'USER')").executeUpdate();
        em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (1,1)").executeUpdate();
        em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (1,2)").executeUpdate();
        em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (2,2)").executeUpdate();
        /*em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (3,2)").executeUpdate();
        em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (4,2)").executeUpdate();*/

    }
}
