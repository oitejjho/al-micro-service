package amlan.employee.repository;

import amlan.employee.model.Employee;
import amlan.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Integer> {

    Page<Employee> findAll(Pageable pageable);

    Optional<Employee> findById(Integer id);

    Optional<Employee> findByEmail(String email);

}
