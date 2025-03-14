package org.megacitycab.dao;


import org.megacitycab.model.Customer;

import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerId);
    Customer getCustomerById(int customerId);
    Customer getCustomerByUsername(String username);
    List<Customer> getAllCustomers();
    
    int getUserCount();

    Customer loginCustomer(String username, String password);
}
