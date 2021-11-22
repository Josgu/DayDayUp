package com.ymplans.patterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jos
 */
public class CompositeExample {

    public static void main(String[] args) {
        Department aDepartment = new Department(1);
        Department bDepartment = new Department(2);
        Employee aEmployee = new Employee(1001, 10000);
        Employee bEmployee = new Employee(1002, 11000);
        Employee cEmployee = new Employee(1003, 12000);

        bDepartment.addSubNode(bEmployee);
        bDepartment.addSubNode(cEmployee);
        aDepartment.addSubNode(aEmployee);
        aDepartment.addSubNode(bDepartment);

        System.out.println("a部门成本：" + aDepartment.calculateSalary());
        System.out.println("b部门成本：" +bDepartment.calculateSalary());
    }

}

abstract class HumanResource {
    protected long id;
    protected double salary;

    HumanResource(long id){
        this.id = id;
    }

    public long getId(){
      return this.id;
    }
    public abstract double calculateSalary();
}

class Employee extends HumanResource {

    Employee(long id, double salary) {
        super(id);
        this.salary = salary;
    }
    @Override
    public double calculateSalary() {
        return salary;
    }
}

class Department extends HumanResource {
    private final List<HumanResource> subNodes = new ArrayList<>();

    Department(long id) {
        super(id);
    }

    @Override
    public double calculateSalary() {
        double totalSalary = 0;
        for (HumanResource subNode : subNodes) {
            totalSalary += subNode.calculateSalary();
        }
        this.salary = totalSalary;
        return totalSalary;
    }

    public void addSubNode(HumanResource humanResource) {
        subNodes.add(humanResource);
    }
}