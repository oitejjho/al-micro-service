package amlan.employee.service;

import amlan.employee.controller.EmployeeController;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger LOG = LogManager.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    //can be override by spring converter for the simplicity used only model mapper
    @Autowired
    private ModelMapper modelMapper;

    public EmployeeListDTO getEmployee(int page, int size) {

        LOG.info("Start getting employee page : {}, size {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeDTO> employees = employeePage.getContent().stream().parallel().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());

        EmployeeListDTO employeeListDTO = EmployeeListDTO.builder()
                .employees(employees)
                .totalElementsInPage(employees.size())
                .totalPages(employeePage.getTotalPages())
                .totalElements(employeePage.getTotalElements())
                .build();

        LOG.info("Start getting employee page : {}, size {}", page, size);

        return employeeListDTO;
    }

}
