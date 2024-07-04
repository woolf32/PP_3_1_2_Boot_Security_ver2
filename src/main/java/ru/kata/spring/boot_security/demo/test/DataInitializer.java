package ru.kata.spring.boot_security.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Employee;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    EmployeeRepository employeeRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Employee emp = new Employee("username","$2a$12$kOOeRbQOtru9WawUDBCe6e5UgnVmMSgR71blRAqkVi5JCopjSLqhy","surname","IT",221);
        employeeRepository.save(emp);
        em.createNativeQuery("INSERT INTO roles (role_id,name) values (1,'ADMIN')").executeUpdate();
        em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (1,1)").executeUpdate();

    }
}
