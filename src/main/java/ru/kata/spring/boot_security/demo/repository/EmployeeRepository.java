package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.entity.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select u from Employee u left join fetch u.roles where u.name=:username")
    Employee getUserByUsername(@Param("username") String username);


}
