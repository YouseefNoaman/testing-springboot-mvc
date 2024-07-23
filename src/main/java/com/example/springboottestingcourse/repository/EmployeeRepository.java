package com.example.springboottestingcourse.repository;

import com.example.springboottestingcourse.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // custom query defined using JPQL with index params
    @Query("select e from Employee e where e.firstname = ?1 and e.lastname = ?2")
    Employee findByJPQL(String firstName, String lastName);

    // custom query defined using JPQL with named params
    @Query("select e from Employee e where e.firstname =:firstname and e.lastname =:lastname")
    Employee findByJPQLNamedParams(@Param("firstname") String firstName,@Param("lastname") String lastName);

    // custom query using native SQL with index params
    @Query(value = "select * from employees e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
    Employee findByNativeSQLWithIndexPrams(String firstname, String lastname);

    // custom query using native SQL with named params
    @Query(value = "select * from employees e where e.first_name =:firstname and e.last_name =:lastname", nativeQuery = true)
    Employee findByNativeSQLWithNamedPrams(@Param("firstname") String firstname, @Param("lastname") String lastname);
}
