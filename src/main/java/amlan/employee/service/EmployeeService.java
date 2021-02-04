package amlan.employee.service;

import amlan.common.constant.StatusConstants;
import amlan.common.exception.DuplicateEntityException;
import amlan.common.exception.NotFoundException;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
import amlan.employee.dto.EmployeeRequest;
import amlan.employee.model.Employee;
import amlan.employee.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger LOG = LogManager.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    //can be override by spring converter or mapStruct for the simplicity used only model mapper
    @Autowired
    private ModelMapper modelMapper;

    public EmployeeListDTO getEmployee(int page, int size) {

        LOG.info("Start getting employee page : {}, size {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeDTO> employees = employeePage.getContent().stream().parallel().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());
        //can catch the exception from objectMapper.map method but for simplicity as of now not added
        EmployeeListDTO employeeListDTO = EmployeeListDTO.builder()
                .page(page)
                .size(employees.size())
                .employees(employees)
                .totalElementsInPage(employees.size())
                .totalPages(employeePage.getTotalPages())
                .totalElements(employeePage.getTotalElements())
                .build();

        LOG.info("Done getting employee page : {}, size {}", page, size);

        return employeeListDTO;
    }

    public EmployeeDTO getEmployee(Integer employeeId) {

        LOG.info("Start getting employee by employee id : {}", employeeId);

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            EmployeeDTO employeeDTO = this.modelMapper.map(employeeOptional.get(), EmployeeDTO.class);
            //can catch the exception from objectMapper.map method but for simplicity as of now not added
            LOG.info("Done getting employee by employee id : {}", employeeId);
            return employeeDTO;
        } else {
            LOG.error("Failed getting employee by employee id : {}", employeeId);
            throw new NotFoundException(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST);
        }

    }

    public EmployeeDTO create(Employee employee) {

        LOG.info("Start creating employee");

        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            LOG.error("Failed creating employee same email exists in database");
            throw new DuplicateEntityException(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS);
        }

        Employee employeeEntity = employeeRepository.save(employee);
        EmployeeDTO employeeDTO = this.modelMapper.map(employeeEntity, EmployeeDTO.class);
        //can catch the exception from objectMapper.map method but for simplicity as of now not added

        LOG.info("Start creating employee");
        return employeeDTO;
    }

    public EmployeeDTO update(Integer employeeId, EmployeeRequest employee) {
        LOG.info("Start updating employee");

        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(employeeId);
        if (!existingEmployeeOptional.isPresent()) {
            LOG.error("Failed updating employee same email exists in database");
            throw new NotFoundException(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST);
        }

        Employee existingEmployee = existingEmployeeOptional.get();
        if (!existingEmployee.getEmail().equalsIgnoreCase(employee.getEmail())) {
            Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
            if (employeeOptional.isPresent()) {
                LOG.error("Failed updating employee same email exists in database");
                throw new DuplicateEntityException(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS);
            }
        }

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setCareer(employee.getCareer());

        Employee employeeEntity = employeeRepository.save(existingEmployee);
        EmployeeDTO employeeDTO = this.modelMapper.map(employeeEntity, EmployeeDTO.class);

        LOG.info("Done updating employee");
        return employeeDTO;
    }

    public void delete(Integer employeeId) {
        LOG.info("Start deleting employee");
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            employeeRepository.deleteById(employeeId);
            LOG.info("Start deleting employee");
        } else {
            LOG.error("Failed getting employee by employee id : {}", employeeId);
            throw new NotFoundException(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST);
        }
    }
}
