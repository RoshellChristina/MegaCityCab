package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.service.customer.CustomerServiceImpl;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.megacitycab.dao.CustomerDAO;
import org.megacitycab.model.Customer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @Mock
    private CustomerDAO customerDAOMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl();

        // Inject the mock CustomerDAO into CustomerServiceImpl using reflection
        Field daoField = customerService.getClass().getDeclaredField("customerDAO");
        daoField.setAccessible(true);
        daoField.set(customerService, customerDAOMock);
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setUsername("testuser");

        // Call the method under test
        customerService.addCustomer(customer);

        // Verify that the DAO method was called with the provided customer
        verify(customerDAOMock, times(1)).addCustomer(customer);
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setUserId(2);
        customer.setUsername("updateuser");

        customerService.updateCustomer(customer);
        verify(customerDAOMock, times(1)).updateCustomer(customer);
    }

    @Test
    public void testDeleteCustomer() {
        int customerId = 3;
        customerService.deleteCustomer(customerId);
        verify(customerDAOMock, times(1)).deleteCustomer(customerId);
    }

    @Test
    public void testGetCustomerById() {
        int customerId = 4;
        Customer customer = new Customer();
        customer.setUserId(customerId);
        customer.setUsername("getuser");

        when(customerDAOMock.getCustomerById(customerId)).thenReturn(customer);

        Customer result = customerService.getCustomerById(customerId);
        assertNotNull(result);
        assertEquals(customerId, result.getUserId());
        assertEquals("getuser", result.getUsername());
        verify(customerDAOMock, times(1)).getCustomerById(customerId);
    }

    @Test
    public void testGetAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setUserId(5);
        customer1.setUsername("user1");

        Customer customer2 = new Customer();
        customer2.setUserId(6);
        customer2.setUsername("user2");

        List<Customer> customers = Arrays.asList(customer1, customer2);
        when(customerDAOMock.getAllCustomers()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerDAOMock, times(1)).getAllCustomers();
    }

    @Test
    public void testGetCustomerByUsername() {
        String username = "findme";
        Customer customer = new Customer();
        customer.setUserId(7);
        customer.setUsername(username);

        when(customerDAOMock.getCustomerByUsername(username)).thenReturn(customer);

        Customer result = customerService.getCustomerByUsername(username);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(customerDAOMock, times(1)).getCustomerByUsername(username);
    }
}
