package com.yrgo.services.customers;

import com.yrgo.dataaccess.CustomerDao;
import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

import java.util.List;

public class CustomerManagementServiceProductionImpl implements CustomerManagementService{
    private CustomerDao dao;

    public CustomerManagementServiceProductionImpl(CustomerDao dao){
        this.dao = dao;
    }


    @Override
    public void newCustomer(Customer newCustomer) {
        dao.create(newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException {
        try{
            dao.update(changedCustomer);
        }
        catch (RecordNotFoundException e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException {
        try{
            dao.delete(oldCustomer);
        }
        catch (Exception e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        try{
            return dao.getById(customerId);
        }
        catch (Exception e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<Customer> findCustomersByName(String name) throws CustomerNotFoundException {
        try{
            return dao.getByName(name);
        }
        catch (Exception e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() throws CustomerNotFoundException {
        try{
            return dao.getAllCustomers();
        }
        catch (RecordNotFoundException e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        try{
            return dao.getFullCustomerDetail(customerId);
        }
        catch (RecordNotFoundException e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        try{
            dao.addCall(callDetails, customerId);
        }
        catch (RecordNotFoundException e){
            throw new CustomerNotFoundException();
        }
    }
}
