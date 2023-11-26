package com.yrgo.client;

import java.util.*;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import com.yrgo.services.calls.CallHandlingService;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;

public class SimpleClient {

    public static void main(String[] args) throws CustomerNotFoundException {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        CustomerManagementService customerService = container.getBean(CustomerManagementService.class);

        CallHandlingService callService = container.getBean(CallHandlingService.class);
        DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);

        customerService.newCustomer(new Customer("CS03939", "Acme", "Good Customer"));

        Call newCall = new Call("Larry Wall called from Acme Corp");
        Action action1 = new Action("Call back Larry to ask how things are going", new GregorianCalendar(2016, 0, 0), "rac");
        Action action2 = new Action("Check our sales dept to make sure Larry is being tracked", new GregorianCalendar(2016, 0, 0), "rac");

        List<Action> actions = new ArrayList<Action>();
        actions.add(action1);
        actions.add(action2);

        try{
            callService.recordCall("CS03939", newCall, actions);
        }catch (CustomerNotFoundException e){
            System.out.println("That customer doesn't exist");
        }

        //System.out.println("Here are the outstanding actions:");
        Collection<Action> incompleteActions = diaryService.getAllIncompleteActions("rac");
        for (Action next: incompleteActions){
            //System.out.println(next);
        }

        //Customer customer1 = customerService.getFullCustomerDetail("CS03939");
        //System.out.println(customer1.getCalls());


        // --- Här börjar "testerna" ---------------------------------------

        // Prova newCustomer
        customerService.newCustomer(new Customer("2", "Billy", "Hej svejsan"));
        customerService.newCustomer(new Customer("3", "Nisse", "Lägg till 2"));
        customerService.newCustomer(new Customer("4", "Hult", "Ring tomten"));

        // Prova getAllCustomers
        List<Customer> allCustomers = customerService.getAllCustomers();
        System.out.println("All customers:");
        for (Customer c: allCustomers
        ) {
            System.out.println(c);
        }

        // Prova findCustomersByName
        System.out.println("Customer information");
        List<Customer> customers = customerService.findCustomersByName("Nisse");
        for (Customer c:customers
             ) {
            System.out.println("id: " + c.getCustomerId() + " name: " + c.getCompanyName() + " notes: " + c.getNotes());
        }

        // Prova findCustomerById
        Customer customerById = customerService.findCustomerById(customers.get(0).getCustomerId());

        // Prova updateCustomer
        System.out.println("Updating: " + customerById);
        Customer updatedCustomer = new Customer("3", "Nisse", "Ta bort 2");
        customerService.updateCustomer(updatedCustomer);
        System.out.println("New notes: " + customerService.findCustomerById(updatedCustomer.getCustomerId()).getNotes());


        // Prova recordCall
        customerService.recordCall(updatedCustomer.getCustomerId(), new Call("Nisse vill inte vara kvar", new Date(2015,11,21,11,10,5)));
        customerService.recordCall(updatedCustomer.getCustomerId(), new Call("Vad blir det för mat?", new Date(2022,4,1,5,45,12)));
        // Prova getFullCustomerDetail
        Customer fullCustomer = customerService.getFullCustomerDetail(updatedCustomer.getCustomerId());
        System.out.println("Calls: " + fullCustomer.getCalls());

        // Prova deleteCustomer
        System.out.println("Removing: " + updatedCustomer);
        customerService.deleteCustomer(updatedCustomer);

        // Prova getAllCustomers
        allCustomers = customerService.getAllCustomers();
        System.out.println("All customers:");
        for (Customer c: allCustomers
             ) {
            System.out.println(c);
        }

        container.close();
    }
}