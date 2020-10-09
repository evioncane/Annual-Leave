package com.example.demo.service;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    EmployeeDao employeeDao;

    private static final Logger logger = LogManager.getLogger(TestService.class);

    public String greet() {
        logger.debug("Greetings in logs");
        Employee employee1 = new Employee();
        employee1.setEmpId("1");
        employee1.setEmpName("Kico");
        Employee employee2 = new Employee();
        employee2.setEmpId("2");
        employee2.setEmpName("Yes");
        employeeDao.insertEmployee(employee1);
        employeeDao.insertEmployee(employee2);
        return "Greetings!";
    }

    public int beRude() {
        return employeeDao.getAllEmployees().size();
    }
}
