package amlan.employee.service;

import amlan.common.config.security.JwtTokenProvider;
import amlan.common.exception.CustomException;
import amlan.employee.dto.EmployeeResponseDTO;
import amlan.employee.model.Employee;
import amlan.employee.repository.EmployeeRepository;
import amlan.user.model.User;
import amlan.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EmployeeResponseDTO> getEmployee(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponseDTO> employeeResponseDTOList = employeePage.getContent().stream().parallel().map(employee -> modelMapper.map(employee, EmployeeResponseDTO.class)).collect(Collectors.toList());

        return employeeResponseDTOList;
    }

}
