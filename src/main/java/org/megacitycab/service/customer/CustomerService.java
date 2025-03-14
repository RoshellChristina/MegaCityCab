package org.megacitycab.service.customer;



import org.megacitycab.model.Customer;

import java.util.List;


public interface CustomerService {
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerId);
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
    Customer getCustomerByUsername(String username);

    Customer login(String username, String passwordHash);
}

