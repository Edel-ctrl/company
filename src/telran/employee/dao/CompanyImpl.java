package telran.employee.dao;

import telran.employee.model.Employee;
import telran.employee.model.SalesManager;

import java.util.function.Predicate;

public class CompanyImpl implements Company {
    private Employee[] employees;
    private int size;

    public CompanyImpl(int capacity) {
        employees = new Employee[capacity];
    }

    @Override
    public boolean addEmployee(Employee employee) {
        if (employee == null || size == employees.length || findEmployee(employee.getId()) != null) {
            return false;
        }
        employees[size++] = employee;// постфикс запись (++ после)
        return true;
    }

    @Override
    public Employee removeEmployee(int id) {
        Employee victim = null;
        for (int i = 0; i < size; i++) {
            if (employees[i].getId() == id) {
                victim = employees[i];
                employees[i] = employees[--size];// префикс запись (-- сначала)
                employees[size] = null;
                break;
            }
        }
        return victim;
    }

    @Override
    public Employee findEmployee(int id) {
        for (int i = 0; i < size; i++) {
            if (employees[i].getId() == id) {
                return employees[i];
            }
        }
        return null;
    }

    @Override
    public int quantity() {
        return size;
    }

    @Override
    public double totalSalary() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += employees[i].calcSalary();
        }
        return sum;
    }

    @Override
    public double totalSales() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            if (employees[i] instanceof SalesManager sm) {
                sum += sm.getSalesValue();
            }
        }
        return sum;
    }

    @Override
    public void printEmployees() {
        System.out.println("=== " + COUNTRY + " ===");
        for (int i = 0; i < size; i++) {
            System.out.println(employees[i]);
        }
        System.out.println("=======================");
    }

    @Override
    public Employee[] findEmployeesHoursGreaterThan(int hours) {
        Predicate<Employee> predicate = new Predicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getHours() > hours;
            }
        };
        return findEmployeesByPredicate(predicate);
    }
    /*
    public Employee[] findEmployeesHoursGreaterThan(int hours) {
        int count = 0;
        for (int i = 0; i < size ; i++) {
            if (employees[i].getHours() > hours) {
               count++;
            }
        }
        Employee[] res = new Employee[count];
        for (int i = 0,j = 0; i < res.length ; i++) {
            if (employees[i].getHours() > hours) {
                res[j++] = employees[i];// j - index для массива res; i- index для массива count
            }
        }
        return res;
    }
     */

    @Override
    public Employee[] findEmployeesSalaryBetween(int minSalary, int maxSalary) {
        return findEmployeesByPredicate(e -> e.calcSalary() >= minSalary && e.calcSalary() < maxSalary);
    }

    private Employee[] findEmployeesByPredicate(Predicate<Employee> predicate){
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(employees[i])) {
                count++;
            }
        }
        Employee[] res = new Employee[count];
        for (int i = 0, j = 0; j < res.length; i++) {
            if (predicate.test(employees[i])) {
                res[j++] = employees[i];
            }
        }
        return res;
    }
}
