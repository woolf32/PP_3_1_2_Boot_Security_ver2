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
        Employee emp = new Employee("username", "$2a$12$kOOeRbQOtru9WawUDBCe6e5UgnVmMSgR71blRAqkVi5JCopjSLqhy", "surname", "IT", 221);
        employeeRepository.save(emp);
        em.createNativeQuery("INSERT INTO roles (role_id,name) values (1,'ADMIN')").executeUpdate();
        em.createNativeQuery("INSERT INTO users_roles (user_id,role_id) values (1,1)").executeUpdate();

    }
}
