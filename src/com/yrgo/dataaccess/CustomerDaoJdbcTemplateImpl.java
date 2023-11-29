package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao{
    private static final String UPDATE_SQL = "UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID=?";
    private static final String INSERT_SQL = "INSERT INTO CUSTOMER (COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?)";
    private static final String GET_CUSTOMER_BY_ID_SQL = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER WHERE CUSTOMER_ID=?";
    private static final String GET_CUSTOMER_BY_NAME_SQL = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER WHERE COMPANY_NAME=?";
    private static final String GET_ALL_CUSTOMERS = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER";
    private static final String DELETE_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";

    private static final String GET_NOTES_BY_CUSTOMER_ID_SQL = "SELECT CALL_ID, CUSTOMER_ID, TIME_DATE, NOTES FROM CALLS WHERE CUSTOMER_ID=?";
    private static final String ADD_CALL_SQL = "INSERT INTO CALLS (CUSTOMER_ID, TIME_DATE, NOTES) VALUES (?,?,?)";
    private JdbcTemplate template;


    public CustomerDaoJdbcTemplateImpl(JdbcTemplate template){
        this.template = template;
        createTables();
    }

    private void createTables()	{
        try{
            this.template.update("CREATE TABLE CUSTOMER (CUSTOMER_ID integer generated by default as identity (start with 1), COMPANY_NAME VARCHAR(255), EMAIL VARCHAR(255), TELEPHONE VARCHAR(20), NOTES VARCHAR(255))");
            this.template.update("CREATE TABLE CALLS (CALL_ID integer generated by default as identity (start with 1), CUSTOMER_ID VARCHAR(255), TIME_DATE DATE, NOTES VARCHAR(255))");
        }catch (org.springframework.jdbc.BadSqlGrammarException e){
            System.out.println("Assuming tables exists");
        }
    }

    @Override
    public void create(Customer customer) {
        template.update(INSERT_SQL, customer.getCompanyName(), customer.getEmail(), customer.getTelephone(), customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return template.queryForObject(GET_CUSTOMER_BY_ID_SQL, new CustomerMapper(), customerId);
    }

    @Override
    public List<Customer> getByName(String name) throws RecordNotFoundException{
        return template.query(GET_CUSTOMER_BY_NAME_SQL, new CustomerMapper(), name);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        template.update(UPDATE_SQL, customerToUpdate.getCompanyName(), customerToUpdate.getEmail(), customerToUpdate.getTelephone(), customerToUpdate.getNotes(), customerToUpdate.getCustomerId());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        template.update(DELETE_SQL, oldCustomer.getCustomerId());
    }

    @Override
    public List<Customer> getAllCustomers() throws RecordNotFoundException {
        return template.query(GET_ALL_CUSTOMERS, new CustomerMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = template.queryForObject(GET_CUSTOMER_BY_ID_SQL, new CustomerMapper(), customerId);
        List<Call> calls = template.query(GET_NOTES_BY_CUSTOMER_ID_SQL, new CallMapper(), customerId);
        customer.setCalls(calls);

        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        template.update(ADD_CALL_SQL, customerId, newCall.getTimeAndDate(), newCall.getNotes());
    }
}

class CustomerMapper implements RowMapper<Customer> {
    public Customer mapRow(ResultSet rs, int rowNumber) throws SQLException {
        String customerId = rs.getString("CUSTOMER_ID");
        String companyName = rs.getString("COMPANY_NAME");
        String email = rs.getString("EMAIL");
        String telephone = rs.getString("TELEPHONE");
        String notes = rs.getString("NOTES");

        return new Customer(customerId, companyName, email, telephone, notes);
    }
}class CallMapper implements RowMapper<Call> {
    public Call mapRow(ResultSet rs, int rowNumber) throws SQLException {
        String callId = rs.getString("CALL_ID");
        String customerId = rs.getString("CUSTOMER_ID");
        Date timeDate = rs.getDate("TIME_DATE");
        String notes = rs.getString("NOTES");


        return new Call(notes, timeDate);
    }
}
