package org.claimsystem.g24fp.controllers.admin;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.claimsystem.g24fp.controllers.LoginController;
import org.claimsystem.g24fp.model.*;
import org.claimsystem.g24fp.model.user.Customer;
import org.claimsystem.g24fp.model.user.Provider;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


public class AdminController extends LoginController implements Initializable {
    @FXML private Button cCust;
    @FXML private Button dCust;
    @FXML private Button lCust;
    @FXML private Button sCust;
    @FXML private Button uCust;
    @FXML private Button ad_search_cust;
    @FXML
    private TextField custNameF;
    @FXML
    private TextField custTypeF;
    @FXML
    private TextField custidF;
    @FXML
    private TextField unameF;
    @FXML
    private TextField pwF;
    @FXML
    private TextField phF;
    @FXML
    private TextField dep;
    @FXML
    private TableColumn<Customer, String> cid_col;

    @FXML
    private TableColumn<Customer, String> cname_col;

    @FXML
    private TableColumn<Customer, String> ctype_col;
    @FXML
    private TableView<Customer> cust_tb;
    @FXML
    private TableColumn<Customer, String> pholder_col;

    @FXML
    private TableColumn<Customer, String> powner_col;
    @FXML
    private TableColumn<Customer, String> relation_col;
    @FXML
    private TableColumn<Customer, String> uname_col;
    @FXML
    private TextField search_cust;

    public void crudCustomer(ActionEvent event) {
        if (event.getSource() == cCust) {
            addCustomer();
        } else if (event.getSource() == dCust) {
            deleteCustomer();
        } else if (event.getSource() == lCust) {
            loadCustomer();
        } else if (event.getSource() == uCust) {
            updateCustomer();
        } else if (event.getSource() == ad_search_cust) {
            searchCustomer();
        }
    }

    public void searchCustomer() {
        String search = search_cust.getText();
        ObservableList<Customer> customers = new UserDB().search(search);
        CustomerToTable(customers, cid_col, cname_col, ctype_col, uname_col, pholder_col, powner_col, relation_col, cust_tb);
    }

    public static void CustomerToTable(ObservableList<Customer> customers, TableColumn<Customer, String> cidCol, TableColumn<Customer, String> cnameCol, TableColumn<Customer, String> ctypeCol, TableColumn<Customer, String> unameCol, TableColumn<Customer, String> pholderCol, TableColumn<Customer, String> pownerCol, TableColumn<Customer, String> relationCol, TableView<Customer> custTb) {
        cidCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cnameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        ctypeCol.setCellValueFactory(new PropertyValueFactory<>("custType"));
        unameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        pholderCol.setCellValueFactory(new PropertyValueFactory<>("policyHolder"));
        pownerCol.setCellValueFactory(new PropertyValueFactory<>("policyOwner"));
        relationCol.setCellValueFactory(new PropertyValueFactory<>("depRelationship"));
        custTb.setItems(customers);
    }

    private void updateCustomer() {
        try {
            String customerID = custidF.getText();
            String customerName = custNameF.getText();
            String custType = custTypeF.getText();
            String userName = unameF.getText();
            String policyHolder = phF.getText();
            String policyOwner = pwF.getText();
            String depRelationship = dep.getText();
            Customer customer = new Customer(customerID, customerName, custType, userName, policyHolder, policyOwner, depRelationship);
            new UserDB().updateCustomer(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Customer> customers = new UserDB().getAllCustomer();
        Platform.runLater(() -> CustomerToTable(customers, cid_col, cname_col, ctype_col, uname_col, pholder_col, powner_col, relation_col, cust_tb));
        cust_tb.refresh();

    }

    private void loadCustomer() {
        ObservableList<Customer> customers = new UserDB().getAllCustomer();
        Platform.runLater(() -> CustomerToTable(customers, cid_col, cname_col, ctype_col, uname_col, pholder_col, powner_col, relation_col, cust_tb));
    }

    private void deleteCustomer() {
        Customer customer = cust_tb.getSelectionModel().getSelectedItem();
        new UserDB().deleteCustomer(customer);
        ObservableList<Customer> customers = new UserDB().getAllCustomer();
        Platform.runLater(() -> CustomerToTable(customers, cid_col, cname_col, ctype_col, uname_col, pholder_col, powner_col, relation_col, cust_tb));
    }

    private void addCustomer() {
        String customerID = custidF.getText();
        String customerName = custNameF.getText();
        String custType = custTypeF.getText();
        String userName = unameF.getText();
        String policyHolder = phF.getText();
        String policyOwner = pwF.getText();
        String depRelationship = dep.getText();
        Customer customer = new Customer(customerID, customerName, custType, userName, policyHolder, policyOwner, depRelationship);
        new UserDB().addCustomer(customer);

    }

    @FXML private TableColumn<Claim, String> claimID_col;
    @FXML private TableColumn<Claim, String> insuredPerson_col;
    @FXML private TableColumn<Claim, Double> requestAmount_col;
    @FXML private TableColumn<Claim, String> appliedPolicy_col;
    @FXML private TableColumn<Claim, String> claimDate_col;
    @FXML private TableColumn<Claim, String> examDate_col;
    @FXML private TableColumn<Claim, String> claimStatus_col;
    @FXML private TableColumn<Claim, Double> claimAmount_col;
    @FXML private TableColumn<Claim, String> processBy_col;
    @FXML private TableView<Claim> claim_tb;

    public void loadClaim() {
        ObservableList<Claim> claims = new ClaimDB().getAllClaims();
        Platform.runLater(() -> claimToTable(claims));
    }

    private void claimToTable(ObservableList<Claim> claims) {
        claimID_col.setCellValueFactory(new PropertyValueFactory<>("claimID"));
        insuredPerson_col.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        requestAmount_col.setCellValueFactory(new PropertyValueFactory<>("requestAmount"));
        appliedPolicy_col.setCellValueFactory(new PropertyValueFactory<>("appliedPolicy"));
        claimDate_col.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        examDate_col.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        claimStatus_col.setCellValueFactory(new PropertyValueFactory<>("claimStatus"));
        claimAmount_col.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
        processBy_col.setCellValueFactory(new PropertyValueFactory<>("processBy"));
        claim_tb.setItems(claims);
    }

    @FXML private TextField searchClaimF;
    public void searchClaim() {
        String search = searchClaimF.getText();
        ObservableList<Claim> claims = new ClaimDB().searchClaim(search);
        claimToTable(claims);
    }

    @FXML private Button cProv;
    @FXML private Button dProv;
    @FXML private Button lProv;
    @FXML private Button uProv;

    public void crudProvider(ActionEvent event) {
        if (event.getSource() == cProv) {
            createProvider();
        } else if (event.getSource() == dProv) {
            deleteProvider();
        } else if (event.getSource() == lProv) {
            loadProvider();
        } else if (event.getSource() == uProv) {
            updateProvider();
        }
    }



    private void updateProvider() {
        String providerID = providF.getText();
        String providerName = provNameF.getText();
        String providerPosition = provPositionF.getText();
        String manager = managerF.getText();
        String userName = provUname.getText();
        Provider provider = new Provider(providerID, providerName, manager, userName, providerPosition);
        new UserDB().updateProvider(provider);
        ObservableList<Provider> providers = new UserDB().getAllProviders();
        Platform.runLater(() -> ProviderToTable(providers));
    }

    private void loadProvider() {
        ObservableList<Provider> providers = new UserDB().getAllProviders();
        Platform.runLater(() -> ProviderToTable(providers));
    }

    @FXML private TableView<Provider> prov_tb;
    @FXML private TableColumn<Provider, String> pid_col;
    @FXML private TableColumn<Provider, String> pname_col;
    @FXML private TableColumn<Provider, String> pposition_col;
    @FXML private TableColumn<Provider, String> manager_col;
    @FXML private TableColumn<Provider, String> provUname_col;

    private void ProviderToTable(ObservableList<Provider> providers) {
        pid_col.setCellValueFactory(new PropertyValueFactory<>("providerID"));
        pname_col.setCellValueFactory(new PropertyValueFactory<>("providerName"));
        pposition_col.setCellValueFactory(new PropertyValueFactory<>("position"));
        manager_col.setCellValueFactory(new PropertyValueFactory<>("manager"));
        provUname_col.setCellValueFactory(new PropertyValueFactory<>("username"));
        prov_tb.setItems(providers);
    }

    private void deleteProvider() {
        Provider provider = prov_tb.getSelectionModel().getSelectedItem();
        new UserDB().deleteProvider(provider);
        ObservableList<Provider> providers = new UserDB().getAllProviders();
        Platform.runLater(() -> ProviderToTable(providers));
    }

    @FXML private TextField providF;
    @FXML private TextField provNameF;
    @FXML private TextField provPositionF;
    @FXML private TextField managerF;
    @FXML private TextField provUname;


    public void createProvider() {
        String providerID = providF.getText();
        String providerName = provNameF.getText();
        String providerPosition = provPositionF.getText();
        String manager = managerF.getText();
        String userName = provUname.getText();
        Provider provider = new Provider(providerID, providerName, manager, userName, providerPosition);
        new UserDB().addProvider(provider);
    }

    @FXML private TextArea historyR;
    public void checkHistory() {
        ObservableList<History> histories = TriggerHandle.loadHistory();
        for (History history : histories) {
            historyR.appendText("History ID: " + history.getId() + "\n");
            historyR.appendText("Current User: " + history.getCurrent_user() + "\n");
            historyR.appendText("Created At: " + history.getCreated_at() + "\n");
            historyR.appendText("Description: " + history.getDescription() + "\n\n");
            historyR.appendText("---------------------------------------------------\n");
        }
        historyR.setEditable(false);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cust_tb.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                Customer customer = cust_tb.getSelectionModel().getSelectedItem();
                custidF.setText(customer.getCustomerID());
                custNameF.setText(customer.getCustomerName());
                custTypeF.setText(customer.getCustType());
                unameF.setText(customer.getUserName());
                phF.setText(customer.getPolicyHolder());
                pwF.setText(customer.getPolicyOwner());
                dep.setText(customer.getDepRelationship());
            }
        });

        prov_tb.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                Provider provider = prov_tb.getSelectionModel().getSelectedItem();
                providF.setText(provider.getProviderID());
                provNameF.setText(provider.getProviderName());
                provPositionF.setText(provider.getPosition());
                managerF.setText(provider.getManager());
                provUname.setText(provider.getUsername());
            }
        });

        checkHistory();


    }
}