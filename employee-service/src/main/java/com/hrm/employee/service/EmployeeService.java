package com.hrm.employee.service;

import com.hrm.employee.mapper.EmployeeMapper;
import com.hrm.employee.mapper.ext.ExtEmployeeMapper;
import com.hrm.employee.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final ExtEmployeeMapper extEmployeeMapper;

    @Transactional
    public Employee createEmployee(Employee employee) {
        // Gán ID mới nếu chưa có
        if (employee.getId() == null) {
            employee.setId(UUID.randomUUID());
        }
        // TODO: Kiểm tra trùng employeeId
        employeeMapper.insert(employee);
        return employee;
    }

    public Employee getEmployeeById(UUID id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(employee)) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }
        return employee;
    }

    public List<Employee> getAllEmployees() {
        return employeeMapper.selectAll();
    }

    @Transactional
    public Employee updateEmployee(UUID id, Employee details) {
        Employee existingEmployee = getEmployeeById(id);

        // Cập nhật các trường
        existingEmployee.setFullName(details.getFullName());
        existingEmployee.setEmail(details.getEmail());
        // ... (cập nhật các trường khác) ...
        existingEmployee.setId(id); // Đảm bảo ID được thiết lập lại cho hàm update

        employeeMapper.updateByPrimaryKey(existingEmployee);
        return existingEmployee;
    }

    @Transactional
    public void deactivateEmployee(UUID id) {
        extEmployeeMapper.deactivate(id);
    }
}
