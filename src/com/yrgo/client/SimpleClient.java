package com.yrgo.client;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class SimpleClient {
    public static void main(String[] args) throws CustomerNotFoundException {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");
        CustomerManagementService cms = container.getBean(CustomerManagementService.class);

        cms.newCustomer(new Customer("RB200", "River Ltd", "note"));
        cms.updateCustomer(new Customer("RB200", "North Ltd", "note"));
        cms.deleteCustomer(cms.findCustomerById("RB200"));

        List<Customer> customers = cms.getAllCustomers();
        for (Customer customer:customers
             ) {
            System.out.println(customer);
        }

        System.out.println("Find customer by id: " + cms.findCustomerById("RM210").toString());
        System.out.println("Find customer by name: " + cms.findCustomersByName("River Ltd"));
        System.out.println("Get full customer detail: " + cms.getFullCustomerDetail("RM210"));


    }
}
