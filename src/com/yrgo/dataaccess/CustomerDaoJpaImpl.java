package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class CustomerDaoJpaImpl implements CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public void create(Customer customer) {
        entityManager.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return entityManager.createQuery("SELECT c FROM Customer c WHERE c.customerId= :customerId", Customer.class).setParameter("customerId",customerId).getSingleResult();
    }

    @Override
    public List<Customer> getByName(String name) throws RecordNotFoundException {
        return entityManager.createQuery("SELECT c FROM Customer c WHERE c.companyName= :name", Customer.class).setParameter("name",name).getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        entityManager.merge(customerToUpdate);
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        Customer customer = entityManager.find(Customer.class, oldCustomer.getCustomerId());
        entityManager.remove(customer);

    }

    @Override
    public List<Customer> getAllCustomers() throws RecordNotFoundException {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        return entityManager.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.calls WHERE c.customerId = :customerId", Customer.class).setParameter("customerId",customerId).getSingleResult();
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer customer = entityManager.find(Customer.class, customerId);
        customer.addCall(newCall);
    }
}
