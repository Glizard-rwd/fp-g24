package org.claimsystem.g24fp;


import javafx.collections.ObservableList;
import org.claimsystem.g24fp.model.UserDB;
import org.claimsystem.g24fp.model.user.Customer;


public class Main {
    public static void main(String[] args) {
        UserDB.setCurrentUser(new String[]{"powner1", "12345"});
//        ObservableList<Customer> customers = new UserDB().getAllCustomer();
//        Customer customer = customers.stream().filter(c -> c.getCustomerID().equals("c9876544")).findFirst().orElse(null);
//        customer.viewInfo();
        ObservableList<Customer> customers1 = new UserDB().search("Harry");
        customers1.forEach(Customer::viewInfo);
    }
}