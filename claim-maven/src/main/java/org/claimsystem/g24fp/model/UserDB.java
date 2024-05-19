package org.claimsystem.g24fp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.claimsystem.g24fp.model.user.Customer;
import org.claimsystem.g24fp.model.user.Provider;
import org.claimsystem.g24fp.model.user.User;

import java.sql.*;
public class UserDB {

    private Connection conn;

    private static String[] currentUser; // [username, password]

    private ObservableList<Customer> customers;
    public UserDB() {
        conn = DBConnection.getConnection();
        customers = FXCollections.observableArrayList();
    }

    // CUSTOMER
    public ResultSet login(String username, String password) {
        String loginSQL = "SELECT * FROM user_info WHERE id = ? AND password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(loginSQL);
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }
        return null;
    }
    public ObservableList<Customer> getAllCustomer() {
        String sql = "SELECT * FROM customer join user_info on customer.user_name = user_info.id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer customerData = new Customer(rs.getString("id"), rs.getString("cust_name"), rs.getString("cust_type"), rs.getString("user_name"), rs.getString("policy_holder"), rs.getString("policy_owner"), rs.getString("dep_relationship"));
                customerData.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                customerData.setLastVisitTime(rs.getTimestamp("last_visit").toLocalDateTime());
                customers.add(customerData);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }
        return customers;
    }
    public ObservableList<Customer> getBeneficiaries() {
        String userName = currentUser[0]; // get the userName
        String searchByUser = "SELECT id, cust_type FROM customer WHERE user_name = ?";
        String pHolderB = "SELECT * FROM customer WHERE policy_holder = ?";
        String pOwnerB = "SELECT * FROM customer WHERE policy_owner = ?";
        String depB = "SELECT * FROM customer WHERE user_name = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(searchByUser)) {
            ps1.setString(1, userName);
            try (ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next()) {
                    String custType = rs1.getString("cust_type");
                    String custId = rs1.getString("id");
                    PreparedStatement ps2;
                    if (custType.equals("policy holder")) {
                        ps2 = conn.prepareStatement(pHolderB);
                        ps2.setString(1, custId);
                    } else if (custType.equals("policy owner")) {
                        ps2 = conn.prepareStatement(pOwnerB);
                        ps2.setString(1, custId);
                    } else {
                        ps2 = conn.prepareStatement(depB);
                        ps2.setString(1, userName);
                    }
                    getCustomer(ps2);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    public void addCustomer(Customer customer) {
        // if customer still does not have an account, create one
        String userSQL = "INSERT INTO user_info (id) VALUES (?)";
        String customerSQL = "INSERT INTO customer (id, cust_name, cust_type, user_name, policy_holder, policy_owner, dep_relationship) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psUser = conn.prepareStatement(userSQL);
             PreparedStatement psCustomer = conn.prepareStatement(customerSQL)) {

            psUser.setString(1, customer.getUserName());
            psUser.executeUpdate();

            psCustomer.setString(1, customer.getCustomerID());
            psCustomer.setString(2, customer.getCustomerName());
            psCustomer.setString(3, customer.getCustType());
            psCustomer.setString(4, customer.getUserName());
            psCustomer.setString(5, customer.getPolicyHolder());
            psCustomer.setString(6, customer.getPolicyOwner());
            psCustomer.setString(7, customer.getDepRelationship());
            psCustomer.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Insert customer " + customer.getCustomerID() + " info");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCustomer(Customer customer) {
        String deleteCustomerSQL = "with deleted_customer as (DELETE FROM customer WHERE id = ? RETURNING user_name) DELETE FROM user_info WHERE id in (SELECT user_name FROM deleted_customer)";

        String deletePolicyHolderSQL = "with deleted_customer as (DELETE FROM customer WHERE id = ? or policy_holder = ? RETURNING user_name) DELETE FROM user_info WHERE id in (SELECT user_name FROM deleted_customer)";

        String deletePolicyOwnerSQL = "with deleted_customer as (DELETE FROM customer WHERE id = ? or policy_owner = ? RETURNING user_name) DELETE FROM user_info WHERE id in (SELECT user_name FROM deleted_customer)";

        try {
            PreparedStatement ps;
            if (customer.getCustType().equals("policy holder")) {
                ps = conn.prepareStatement(deletePolicyHolderSQL);
                ps.setString(2, customer.getCustomerID());
            } else if (customer.getCustType().equals("policy owner")) {
                ps = conn.prepareStatement(deletePolicyOwnerSQL);
                ps.setString(2, customer.getCustomerID());
            } else {
                ps = conn.prepareStatement(deleteCustomerSQL);
            }
            ps.setString(1, customer.getCustomerID());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Delete customer " + customer.getCustomerID() + " info");

        } catch (SQLException e) {
            throw new RuntimeException("Cannot delete customer!");
        }
    }
    public void updateCustomer(Customer updateCustomer) {
        // can update id, name, policy_holder, dep_relationship, policy owner
        // cannot update user_name, cust_type, customer_id
        // handle update last_active time
        String updateCustomerSQL = "UPDATE customer SET cust_name = ?, policy_holder = ?, policy_owner = ?, dep_relationship = ? WHERE id = ?";
        String updateLastVisitSQL = "UPDATE user_info SET last_visit = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateCustomerSQL);
        PreparedStatement ps2 = conn.prepareStatement(updateLastVisitSQL)) {
            ps.setString(1, updateCustomer.getCustomerName());
            ps.setString(2, updateCustomer.getPolicyHolder());
            ps.setString(3, updateCustomer.getPolicyOwner());
            ps.setString(4, updateCustomer.getDepRelationship());
            ps.setString(5, updateCustomer.getCustomerID());
            ps.executeUpdate();

            ps2.setTimestamp(1, Timestamp.valueOf(updateCustomer.getLastVisitTime()));
            ps2.setString(2, currentUser[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TriggerHandle.recordAction(currentUser[0], "Updated customer " + updateCustomer.getCustomerID() + " info");
        TriggerHandle.recordAction(currentUser[0], "Updated user last visit " + updateCustomer.getUsername() + " info");
    }
    public ObservableList<Customer> search(String searchWord) {
        String searchSQL = "SELECT * FROM customer WHERE id ILIKE ? OR cust_name ILIKE ? OR cust_type ILIKE ? OR user_name ILIKE ? OR policy_holder ILIKE ? OR policy_owner ILIKE ? OR dep_relationship ILIKE ? AND user_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(searchSQL)) {
            String searchTerm = "%" + searchWord + "%";
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, searchTerm);
            }
            ps.setString(8, currentUser[0]);
            getCustomer(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    public ObservableList<Customer> searchByBeneficiaries(String searchWord) {
        String searchQuery = """
        WITH select_customer AS (
            SELECT id
            FROM customer
            WHERE user_name = ?
        )
        SELECT *
        FROM customer
        WHERE (policy_owner IN (SELECT id FROM select_customer)
               OR policy_holder IN (SELECT id FROM select_customer))
              AND (id ILIKE ?\s
                   OR cust_name ILIKE ?\s
                   OR cust_type ILIKE ?\s
                   OR user_name ILIKE ?\s
                   OR policy_holder ILIKE ?\s
                   OR policy_owner ILIKE ?\s
                   OR dep_relationship ILIKE ?)
   \s""";

        try (PreparedStatement ps = conn.prepareStatement(searchQuery)) {
            ps.setString(1, currentUser[0]); // result of the current user
            String searchTerm = "%" + searchWord + "%";
            for (int i = 2; i <= 8; i++) {
                ps.setString(i, searchTerm);
            }
            getCustomer(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    private void getCustomer(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer customerData = new Customer(
                        rs.getString("id"),
                        rs.getString("cust_name"),
                        rs.getString("cust_type"),
                        rs.getString("user_name"),
                        rs.getString("policy_holder"),
                        rs.getString("policy_owner"),
                        rs.getString("dep_relationship")
                );
                customers.add(customerData);
            }
        }
    }

    public static String[] getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(String[] curUser) {
        currentUser = curUser;
    }

    // TODO: HANDLE CUSTOMER BANK

    // PROVIDER
    public ObservableList<Provider> getAllProviders() {
        ObservableList<Provider> providers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM provider";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Provider provider = new Provider(rs.getString("id"), rs.getString("prov_name"), rs.getString("manager"), rs.getString("user_name"), rs.getString("position"));
                providers.add(provider);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }
        return providers;
    }

    public ObservableList<Provider> getProviderByManager() {
        ObservableList<Provider> providers = FXCollections.observableArrayList();
        String sql = "WITH select_provider AS (SELECT id FROM provider WHERE user_name = ?) SELECT * FROM provider WHERE manager IN (SELECT id FROM select_provider)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentUser[0]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Provider provider = new Provider(rs.getString("id"), rs.getString("prov_name"), rs.getString("manager"), rs.getString("user_name"), rs.getString("position"));
                    providers.add(provider);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }
        return providers;
    }

    public void addProvider(Provider provider) {
        String userSQL = "INSERT INTO user_info (id) VALUES (?)";
        String providerSQL = "INSERT INTO provider (id, prov_name, manager, user_name, position) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psUser = conn.prepareStatement(userSQL);
             PreparedStatement psProvider = conn.prepareStatement(providerSQL)) {

            psUser.setString(1, provider.getUsername());
            psUser.executeUpdate();

            psProvider.setString(1, provider.getProviderID());
            psProvider.setString(2, provider.getProviderName());
            psProvider.setString(3, provider.getManager());
            psProvider.setString(4, provider.getUsername());
            psProvider.setString(5, provider.getPosition());
            psProvider.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Insert provider " + provider.getProviderID() + " info");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProvider(Provider updateProvider) {
        String updateProviderSQL = "UPDATE provider SET prov_name = ?, manager = ?, position = ? WHERE id = ?";
        String updateLastVisitSQL = "UPDATE user_info SET last_visit = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateProviderSQL);
             PreparedStatement ps2 = conn.prepareStatement(updateLastVisitSQL)) {
            ps.setString(1, updateProvider.getProviderName());
            ps.setString(2, updateProvider.getManager());
            ps.setString(3, updateProvider.getPosition());
            ps.setString(4, updateProvider.getProviderID());
            ps.executeUpdate();

            ps2.setTimestamp(1, Timestamp.valueOf(updateProvider.getLastVisitTime()));
            ps2.setString(2, currentUser[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProvider(Provider provider) {
        String deleteProviderSQL = "with deleted_provider as (DELETE FROM provider WHERE id = ? RETURNING user_name) DELETE FROM user_info WHERE id in (SELECT user_name FROM deleted_provider)";
        try {
            PreparedStatement ps = conn.prepareStatement(deleteProviderSQL);
            ps.setString(1, provider.getProviderID());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Delete provider " + provider.getProviderID() + " info");

        } catch (SQLException e) {
            throw new RuntimeException("Cannot delete provider!");
        }
    }

    public ObservableList<Provider> searchProvider(String searchWord) {
        ObservableList<Provider> providers = FXCollections.observableArrayList();
        String searchSQL = "SELECT * FROM provider WHERE id ILIKE ? OR prov_name ILIKE ? OR manager ILIKE ? OR user_name ILIKE ? OR position ILIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(searchSQL)) {
            String searchTerm = "%" + searchWord + "%";
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, searchTerm);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Provider provider = new Provider(rs.getString("id"), rs.getString("prov_name"), rs.getString("manager"), rs.getString("user_name"), rs.getString("position"));
                    providers.add(provider);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return providers;
    }

    public ObservableList<Provider> searchProviderByUser(String searchWord) {
        ObservableList<Provider> providers = FXCollections.observableArrayList();
        String searchSQL = """
with select_provider as (
    SELECT id
    FROM provider
    WHERE user_name = ?
) select * from customer where manager in (select id from select_provider) and (id ILIKE ?
    OR provider_name ILIKE ?
    OR manager ILIKE ?
    OR user_name ILIKE ?
    OR position ILIKE ?)

""";
        try (PreparedStatement ps = conn.prepareStatement(searchSQL)) {
            ps.setString(1, currentUser[0]);
            String searchTerm = "%" + searchWord + "%";
            for (int i = 2; i <= 5; i++) {
                ps.setString(i, searchTerm);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Provider provider = new Provider(rs.getString("id"), rs.getString("prov_name"), rs.getString("manager"), rs.getString("user_name"), rs.getString("position"));
                    providers.add(provider);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return providers;
    }


}
