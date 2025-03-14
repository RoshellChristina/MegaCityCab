package org.megacitycab.service.customer;


import org.megacitycab.dao.CustomerDAO;
import org.megacitycab.dao.CustomerDAOImpl;
import org.megacitycab.model.Customer;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public void addCustomer(Customer customer) {
        customerDAO.addCustomer(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerDAO.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(int customerId) {
        customerDAO.deleteCustomer(customerId);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerDAO.getCustomerByUsername(username);
    }

    @Override
    public Customer login(String username, String password) {
        Customer customer = customerDAO.loginCustomer(username, password); // Fetch customer from DAO
        if (customer == null) {
            throw new RuntimeException("Invalid username or password");
        }
        return customer; // Return the customer if found
    }
}

