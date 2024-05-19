package org.claimsystem.g24fp.controllers.customer;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.claimsystem.g24fp.controllers.LoginController;

import org.claimsystem.g24fp.controllers.admin.AdminController;
import org.claimsystem.g24fp.model.*;
import org.claimsystem.g24fp.model.user.Customer;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.TableView.TableViewSelectionModel;


public class CustomerController extends LoginController implements Initializable {
    @FXML private TableColumn<Claim, String> aPolicy;
    @FXML private Button addClaimBtn;
    @FXML private TableColumn<Claim, Double> claimAmount;
    @FXML private TableColumn<Claim, String> claimD;
    @FXML private TableColumn<Claim, String> claimID;
    @FXML private AnchorPane claim_form;
    @FXML private TableView<Claim> claim_tableView;
    @FXML private TableColumn<Customer, String> cname_tb;
    @FXML private TableColumn<Customer, String> ctype_tb;
    @FXML private TextField custSearch;
    @FXML private Button cust_createBtn;
    @FXML private Button cust_dashboard_beneficiaries;
    @FXML private Button cust_dashboard_claim;
    @FXML private Button cust_dashboard_info;
    @FXML private Button cust_dashboard_policy;
    @FXML private Button cust_deleteBtn;
    @FXML private AnchorPane cust_form;
    @FXML private Button cust_load;
    @FXML private Button cust_saveBtn;
    @FXML private TableView<Customer> cust_table;
    @FXML private Button cust_updateBtn;
    @FXML private TextField custid;
    @FXML private TableColumn<Customer, String> custid_tb;
    @FXML private TextField custname;
    @FXML private TextField custtype;
    @FXML private TableColumn<Customer, String> custusname_tb;
    @FXML private Button deleteClaimBtn;
    @FXML private AnchorPane doc_form;
    @FXML private TableColumn<Claim, String> examD;
    @FXML private TextField idF;
    @FXML private TableColumn<Claim, String> inPerson;
    @FXML private TextField inPersonF;
    @FXML private AnchorPane info_form;
    @FXML private Button loadClaimBtn;
    @FXML private Button logoutBtn;
    @FXML private TextField pholder;
    @FXML private TableColumn<Customer, String> pholder_tb;
    @FXML private TextField policyF;
    @FXML private AnchorPane policy_form;
    @FXML private TextField powner;
    @FXML private TableColumn<Customer, String> powner_tb;
    @FXML private TableColumn<Claim, String> processBy;
    @FXML private TextField processF;
    @FXML private TextField relationship;
    @FXML private TableColumn<Customer, String> relationship_tb;
    @FXML private TableColumn<Claim, Double> requestAmount;
    @FXML private TextField requestAmountF;
    @FXML private Button saveClaimBtn;
    @FXML private TableColumn<Claim, String> status;
    @FXML private Button updateClaimBtn;
    @FXML private TextField username;
    @FXML private Text currentUser;
    @FXML private TextField statusF;
    @FXML private TextField searchClaim;
    @FXML private TextField cardNF;
    @FXML private TextField cardHF;
    @FXML private Button cust_dashboard_iCard;
    @FXML private TextField expiredDF;
    @FXML private TableColumn<InsuranceCard, String> cardnum_col;
    @FXML private TableColumn<InsuranceCard, String> holder_col;
    @FXML private TableColumn<InsuranceCard, String> expDate_col;
    @FXML private TableView<InsuranceCard> card_table;
    @FXML private Button createCardBtn;
    @FXML private Button deleteCardBtn;
    @FXML private Button loadCardBtn;
    @FXML private Button saveCardBtn;
    @FXML private AnchorPane card_form;
    @FXML private TextField pid;
    @FXML private TextField pname;
    @FXML private TextField ptype;
    @FXML private TextField pcontent;
    @FXML private TextField prate;
    @FXML private Button createPBtn;
    @FXML private Button deletePBtn;
    @FXML private Button loadPBtn;
    @FXML private Button savePBtn;
    @FXML private TableView<Policy> policy_table;
    @FXML private TableColumn<Policy, String> pID;
    @FXML private TableColumn<Policy, String> pName;
    @FXML private TableColumn<Policy, String> pContent;
    @FXML private TableColumn<Policy, String> pCoverRate;
    @FXML private TableColumn<Policy, String> POwner;
    @FXML private TableColumn<Policy, String> pType;

    // CUSTOMER FORM
    public void setPrivilege() {
        // Set the privilege of the user
        // If the customer is a policy holder, they can view info + claim + beneficiaries. Cannot delete claim
        // If the customer is a policy owner, they can view info + claim + beneficiaries + insurance card
        // If the customer is a dependent, they can view info + claim. Can only load the info + claim

        // Get the current user
        Optional<Customer> currentUserOpt = new UserDB()
                .getAllCustomer()
                .filtered(customer -> customer.getUserName().equals(UserDB.getCurrentUser()[0]))
                .stream()
                .findFirst();

        // Check if a user was found
        if (currentUserOpt.isPresent()) {
            Customer currentUser = currentUserOpt.get();

            switch (currentUser.getCustType()) {
                case "policy holder":
                    cust_dashboard_info.setVisible(true);
                    cust_dashboard_claim.setVisible(true);
                    cust_dashboard_beneficiaries.setVisible(true);
                    cust_dashboard_iCard.setVisible(false);
                    cust_dashboard_policy.setVisible(false);
                    deleteClaimBtn.setVisible(false);
                    processF.setEditable(false);
                    policyF.setEditable(false);
                    break;

                case "policy owner":
                    cust_dashboard_info.setVisible(true);
                    cust_dashboard_claim.setVisible(true);
                    cust_dashboard_beneficiaries.setVisible(true);
                    cust_dashboard_iCard.setVisible(true);
                    cust_dashboard_policy.setVisible(true);
                    break;

                case "dependent":
                    cust_dashboard_info.setVisible(true);
                    cust_dashboard_claim.setVisible(true);
                    cust_dashboard_beneficiaries.setVisible(false);
                    cust_dashboard_iCard.setVisible(false);
                    cust_dashboard_policy.setVisible(false);
                    addClaimBtn.setVisible(false);
                    deleteClaimBtn.setVisible(false);
                    updateClaimBtn.setVisible(false);
                    saveClaimBtn.setVisible(false);

                    idF.setEditable(false);
                    inPersonF.setEditable(false);
                    requestAmountF.setEditable(false);
                    policyF.setEditable(false);
                    processF.setEditable(false);
                    statusF.setEditable(false);
                    break;
                    default:
                    // Handle unexpected user type
                    cust_dashboard_info.setVisible(false);
                    cust_dashboard_claim.setVisible(false);
                    cust_dashboard_beneficiaries.setVisible(false);
                    cust_dashboard_iCard.setVisible(false);
                    cust_dashboard_policy.setVisible(false);
                    showNotification("Error: Unexpected customer type", Alert.AlertType.ERROR);
                    break;
            }
        } else {
            // Handle case where no user is found
            cust_dashboard_info.setVisible(false);
            cust_dashboard_claim.setVisible(false);
            cust_dashboard_beneficiaries.setVisible(false);
            cust_dashboard_iCard.setVisible(false);
            cust_dashboard_policy.setVisible(false);
            showNotification("Error: No current user found", Alert.AlertType.ERROR);
        }
    }

    public void switchForm(ActionEvent event) {
        // switch to the login form
        if (event.getSource() == cust_dashboard_info) {
            displayDashboard(true, false, false, false, false);
            viewCustomerInfo();
        } else if (event.getSource() == cust_dashboard_claim) {
            displayDashboard(false, true, false, false, false);
        } else if (event.getSource() == cust_dashboard_beneficiaries) {
           displayDashboard(false, false, true, false, false);
        } else if (event.getSource() == cust_dashboard_iCard) {
            displayDashboard(false, false, false, true, false);
        } else if (event.getSource() == cust_dashboard_policy) {
            displayDashboard(false, false, false, false, true);
        }
    }
    private void displayDashboard(boolean info, boolean claim, boolean beneficiaries, boolean card, boolean policy) {
        info_form.setVisible(info);
        claim_form.setVisible(claim);
        cust_form.setVisible(beneficiaries);
        card_form.setVisible(card);
        policy_form.setVisible(policy);
    }
    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                Customer currentUser = new UserDB().getAllCustomer().filtered(customer -> customer.getUserName().equals(UserDB.getCurrentUser()[0])).getFirst();
                currentUser.setLastVisitTime(LocalDateTime.now());
                new UserDB().updateCustomer(currentUser);
                System.out.println(currentUser.getUserName());
                System.out.println(currentUser.getLastVisitTime());

                // exit the current stage
                logoutBtn.getScene().getWindow().hide();
                // turn back to the login form
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void viewCustomerInfo() {
        String currentUser = UserDB.getCurrentUser()[0];
        ObservableList<Customer> customers = new UserDB().getAllCustomer().filtered(c -> c.getUserName().equals(currentUser));
        Customer customer = customers.stream().findFirst().orElse(new Customer());
        InsuranceCard icard = new ClaimDB().getAllInsuranceCards().filtered(ic -> ic.getCardHolder().equals(customer.getCustomerID())).stream().findFirst().orElse(new InsuranceCard());
        TextArea textArea = null;
        // Loop through the children of info_form to find the TextArea
        for (Object obj : info_form.getChildren()) {
                    if (obj instanceof TextArea) {
                        textArea = (TextArea) obj;
                        break;
                    }
                }
        if (textArea != null) {
            // Construct the customer information string
            String customerInfo = "Customer ID: " + customer.getCustomerID() + "\n";
            customerInfo += "Customer Name: " + customer.getCustomerName() + "\n";
            customerInfo += "Customer Type: " + customer.getCustType() + "\n";
            customerInfo += "Username: " + customer.getUserName() + "\n";
            customerInfo += "Created Time: " + customer.getCreateTime() + "\n";
            customerInfo += "Last Visit Time: " + customer.getLastVisitTime() + "\n";
            customerInfo += "InsuranceCard: " + icard.getCardID()+ "\n";
            customerInfo += "Policy Holder: " + customer.getPolicyHolder() + "\n";
            customerInfo += "Policy Owner: " + customer.getPolicyOwner() + "\n";
            customerInfo += "Dependent Relationship: " + customer.getDepRelationship() + "\n";
            // display customer insurance card
            // display current bank

            // Set the customer information in the TextArea
            textArea.setText(customerInfo);
            textArea.setEditable(false);
        } else {
            System.err.println("TextArea not found in info_form children.");
        }
    }
    public void viewBeneficiaries() {
        cust_table.refresh(); // always update the table view
        ObservableList<Customer> customers = new UserDB().getBeneficiaries();
        Platform.runLater(() -> getOneCustomer(customers));
    }

    private void getOneCustomer(ObservableList<Customer> customers) {
        AdminController.CustomerToTable(customers, custid_tb, cname_tb, ctype_tb, custusname_tb, pholder_tb, powner_tb, relationship_tb, cust_table);
    }

    

    public void crud(ActionEvent event) {
        if (event.getSource() == cust_createBtn) enterNewCustomer();
        else if (event.getSource() == cust_saveBtn) addCustomer();
        else if (event.getSource() == cust_deleteBtn) deleteCustomer();
        else if (event.getSource() == cust_load) loadCustomer();
        else if (event.getSource() == cust_updateBtn) updateCustomer();
    }
    public void loadCustomer() {
        custid.setEditable(false);
        custtype.setEditable(false);
        username.setEditable(false);
        viewBeneficiaries();
    }
    // enter new customer info
    public void enterNewCustomer() {
        custid.setEditable(true);
        custtype.setEditable(true);
        username.setEditable(true);
        clearTextField();
    }
    // save to database
    public void addCustomer() {
        custid.setEditable(false);
        custtype.setEditable(false);
        username.setEditable(false);
        try {
            Customer customer = getTextField();
            new UserDB().addCustomer(customer); // add backend
            viewBeneficiaries(); // display front end
            // show notification
            // disable the text field after adding customer
            showNotification("Customer added successfully!", Alert.AlertType.INFORMATION);
            clearTextField(); // clear text field after adding customer
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
     private void showNotification(String message, Alert.AlertType type) {
        Alert notification = new Alert(type);
        notification.setTitle("Information Dialog");
        notification.setHeaderText(null);
        notification.setContentText(message);
        notification.showAndWait();
     }
    private Customer getTextField() {
        String customerID = custid.getText();
        String customerName = custname.getText();
        String customerType = custtype.getText();
        String userName = username.getText();
        String policyHolder = pholder.getText();
        String policyOwner = powner.getText();
        String depRelationship = relationship.getText();

        return new Customer(customerID, customerName, customerType, userName, policyHolder, policyOwner, depRelationship);
    }
    // delete customer
    public void deleteCustomer() {
        // choose a range of record on TableView: cust_table to delete
        // click on the delete button to delete the selected record
        try {
            ObservableList<Customer> customers = multipleSelectCustomer();
            UserDB userDB = new UserDB();
            if (customers.isEmpty()) {
                showNotification("Please select a record to delete!", Alert.AlertType.WARNING);
                return;
            }
            for (Customer customer : customers) {
                userDB.deleteCustomer(customer);
            } // remove customer info backdend
            cust_table.getItems().removeAll(customers); // remove customer info frontend
            showNotification("Customer deleted successfully!", Alert.AlertType.INFORMATION);
            viewBeneficiaries();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public ObservableList<Customer> multipleSelectCustomer() {
        TableViewSelectionModel<Customer> selectionModel = cust_table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        return selectionModel.getSelectedItems();
    }
    // update customer
    public void updateCustomer() {
        // chose a record on TableView: cust_table to update
        // enter the new value on the text field where you want to update
        // click on the update button to update the selected record
        // can update name, policy_holder, dep_relationship
        // cannot update user_name, cust_type, policy_owner, customer_id
        custid.setEditable(false);
        custtype.setEditable(false);
        username.setEditable(false);
        try {
            UserDB userDB = new UserDB();
            Customer oldCustomer = getTextField();
            oldCustomer.setCustomerName(custname.getText());
            oldCustomer.setCustType(custtype.getText());
            oldCustomer.setUserName(username.getText());
            oldCustomer.setPolicyHolder(pholder.getText());
            oldCustomer.setPolicyOwner(powner.getText());
            oldCustomer.setDepRelationship(relationship.getText());
            userDB.updateCustomer(oldCustomer);
            showNotification("Customer updated successfully!", Alert.AlertType.INFORMATION);
            cust_table.refresh();

        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setProperties(Customer customer) {
        if (customer != null) {
            custid.setText(customer.getCustomerID());
            custname.setText(customer.getCustomerName());
            custtype.setText(customer.getCustType());
            username.setText(customer.getUserName());
            pholder.setText(customer.getPolicyHolder());
            powner.setText(customer.getPolicyOwner());
            relationship.setText(customer.getDepRelationship());
        }
    }
    // search customer
    public void searchCustomer() {
        String searchWord = custSearch.getText();
        ObservableList<Customer> customers = new UserDB().searchByBeneficiaries(searchWord);
        getOneCustomer(customers);
    }

    // CLAIM FORM
    public void crudClaim(ActionEvent event) {
        if (event.getSource() == addClaimBtn) enterNewClaim();
        else if (event.getSource() == saveClaimBtn) addClaim();
        else if (event.getSource() == deleteClaimBtn) deleteClaim();
        else if (event.getSource() == loadClaimBtn) loadClaim();
        else if (event.getSource() == updateClaimBtn) updateClaim();
    }
    public void loadClaim() {
        // disable the text field
        idF.setEditable(false);
        inPersonF.setEditable(false);
        statusF.setEditable(false);
        Customer currentUser = new UserDB().getAllCustomer().filtered(c -> c.getUserName().equals(UserDB.getCurrentUser()[0])).getFirst();
        if (currentUser.getCustType().equals("policy holder")) {
            processF.setEditable(false);
            requestAmountF.setEditable(false);
        }
        // get load button
        claim_tableView.refresh();
        ObservableList<Claim> claims = new ClaimDB().getClaimByUser();
        Platform.runLater(() -> listClaimTable(claims));
    }

    private void listClaimTable(ObservableList<Claim> claims) {
        claimID.setCellValueFactory(new PropertyValueFactory<>("claimID"));
        inPerson.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        requestAmount.setCellValueFactory(new PropertyValueFactory<>("requestAmount"));
        aPolicy.setCellValueFactory(new PropertyValueFactory<>("appliedPolicy"));
        claimD.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        examD.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        status.setCellValueFactory(new PropertyValueFactory<>("claimStatus"));
        claimAmount.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
        processBy.setCellValueFactory(new PropertyValueFactory<>("processBy"));
        claim_tableView.setItems(claims);
    }
    public void setClaimToTextField(Claim claim) {
        idF.setText(claim.getClaimID());
        inPersonF.setText(claim.getInsuredPerson());
        requestAmountF.setText(String.valueOf(claim.getRequestAmount()));
        policyF.setText(claim.getAppliedPolicy());
        examD.setText(claim.getExamDate().toString());
        processF.setText(claim.getProcessBy());
        statusF.setText(claim.getClaimStatus());
    }
    public void enterNewClaim() {
        idF.setEditable(true);
        inPersonF.setEditable(true);
        requestAmountF.setEditable(true);
        examD.setEditable(true);
        statusF.setEditable(true);
        Customer currentUser = new UserDB().getAllCustomer().filtered(c -> c.getUserName().equals(UserDB.getCurrentUser()[0])).getFirst();
        if (currentUser.getCustType().equals("policy holder")) {
            policyF.setEditable(false);
            processF.setEditable(false);
        } else {
            policyF.setEditable(true);
            processF.setEditable(true);
        }

        clearClaimTextField();
    }

    public void addClaim() {
        try {
            Claim claim = getClaimTextField();
            new ClaimDB().addClaim(claim);
            showNotification("Claim added successfully!", Alert.AlertType.INFORMATION);
            clearClaimTextField();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private Claim getClaimTextField() {
        String claimID = idF.getText();
        String insuredPerson = inPersonF.getText();
        double requestAmount = Double.parseDouble(requestAmountF.getText());
        String claimStatus = statusF.getText();
        String processBy = processF.getText();
        String appliedPolicy = policyF.getText();
        Claim claim = new Claim();
        claim.setClaimID(claimID);
        claim.setInsuredPerson(insuredPerson);
        claim.setRequestAmount(requestAmount);
        claim.setClaimStatus(claimStatus);
        claim.setProcessBy(processBy);
        claim.setAppliedPolicy(appliedPolicy);
        return claim;
    }
    public void deleteClaim() {
        // choose claim to delete
        // delete the document too
        try {
            ObservableList<Claim> claims = multipleSelect(claim_tableView);
            ClaimDB claimDB = new ClaimDB();
            if (claims.isEmpty()) {
                showNotification("Please select a record to delete!", Alert.AlertType.WARNING);
                return;
            }
            for (Claim claim : claims) {
                claimDB.deleteClaim(claim);
            }
            claim_tableView.getItems().removeAll(claims);
            showNotification("Claim deleted successfully!", Alert.AlertType.INFORMATION);
            claim_tableView.refresh();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    public ObservableList<Claim> multipleSelect(TableView<Claim> claim_tableView) {
        TableViewSelectionModel<Claim> selectionModel = claim_tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        return selectionModel.getSelectedItems();
    }
    public void updateClaim() {
        idF.setEditable(false);
        inPersonF.setEditable(false);
        try {
            ClaimDB claimDB = new ClaimDB();
            Claim oldDataClaim = getClaimTextField(); // get claim info from textField
            oldDataClaim.setExamDate(LocalDateTime.now()); // update time
            oldDataClaim.setAppliedPolicy(policyF.getText()); // update policy
            oldDataClaim.setRequestAmount(Double.parseDouble(requestAmountF.getText())); // update request amount
            oldDataClaim.setClaimStatus(statusF.getText()); // update status
            oldDataClaim.setProcessBy(processF.getText()); // update process by
            claimDB.updateClaim(oldDataClaim);
            showNotification("Claim updated successfully!", Alert.AlertType.INFORMATION);

            // Refresh the TableView
            ObservableList<Claim> updatedClaims = claimDB.getClaimByUser();
            claim_tableView.setItems(updatedClaims);
            claim_tableView.refresh();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public void clearTextField() {
        for (Object obj : cust_form.getChildren()) {
            if (obj instanceof TextField) {
                ((TextField) obj).clear();
            }
        }
    }

    public void clearClaimTextField() {
        for (Object obj : claim_form.getChildren()) {
            if (obj instanceof TextField) {
                ((TextField) obj).clear();
            }
        }
    }


    public void findClaim() {
        String searchWord = searchClaim.getText();
        ObservableList<Claim> claims = new ClaimDB().searchByUser(searchWord);
        Platform.runLater(() -> listClaimTable(claims));
    }

    // Insurance Card
    // create + get + delete
    public void crudCard(ActionEvent event) {
        if (event.getSource() == createCardBtn) enterNewCard();
        else if (event.getSource() == saveCardBtn) addInsurance();
        else if (event.getSource() == deleteCardBtn) deleteInsurance();
        else if (event.getSource() == loadCardBtn) loadInsurance();
    }


    private void deleteInsurance() {
        try {
            ObservableList<InsuranceCard> cards = multipleSelectCard();
            ClaimDB claimDB = new ClaimDB();

            if (cards.isEmpty()) {
                showNotification("Please select a record to delete!", Alert.AlertType.WARNING);
                return;
            }
            for (InsuranceCard card : cards) {
                claimDB.deleteInsuranceCard(card);
            }
            for (InsuranceCard card : cards) {
                claimDB.deleteInsuranceCard(card);
            }
            card_table.getItems().removeAll(cards);
            showNotification("Insurance Card deleted successfully!", Alert.AlertType.INFORMATION);
            card_table.refresh();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<InsuranceCard> multipleSelectCard() {
        TableViewSelectionModel<InsuranceCard> selectionModel = card_table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        return selectionModel.getSelectedItems();
    }

    private void enterNewCard() {
        cardNF.setEditable(true);
        cardHF.setEditable(true);
        expiredDF.setEditable(true);
        clearCardTextField();
    }

    public void loadInsurance() {
        card_table.refresh();
        ObservableList<InsuranceCard> cards = new ClaimDB().getCardByUser();
        Platform.runLater(() -> listCardTable(cards));
    }

    private void listCardTable(ObservableList<InsuranceCard> cards) {
        cardnum_col.setCellValueFactory(new PropertyValueFactory<>("cardID"));
        holder_col.setCellValueFactory(new PropertyValueFactory<>("cardHolder"));
        expDate_col.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        card_table.setItems(cards);
    }

    public void addInsurance() {
        try {
            InsuranceCard insuranceCard = getFromTextField();
            new ClaimDB().addInsuranceCard(insuranceCard);
            showNotification("Insurance Card added successfully!", Alert.AlertType.INFORMATION);
            loadInsurance();
            clearCardTextField();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearCardTextField() {
        for (Object obj : doc_form.getChildren()) {
            if (obj instanceof TextField) {
                ((TextField) obj).clear();
            }
        }
    }
    public InsuranceCard getFromTextField() {
        String cardID = cardNF.getText();
        String cardHolder = cardHF.getText();
        String expireDate = expiredDF.getText();
        return new InsuranceCard(cardID, cardHolder, expireDate);
    }

    // policy
    public void crudPolicy(ActionEvent event) {
        if (event.getSource() == createPBtn) enterNewPolicy();
        else if (event.getSource() == savePBtn) addPolicy();
        else if (event.getSource() == deletePBtn) deletePolicy();
        else if (event.getSource() == loadPBtn) loadPolicy();
    }

    // TODO: if cannot debug remove this method
    private void updatePolicy() {
        pid.setEditable(false); // cannot update policy id
        powner.setEditable(false); // cannot update policy owner
        try {
            Policy policy = getPolicyTextField();
            policy.setPolicyId(pid.getText());
            policy.setPolicyName(pname.getText());
            policy.setPolicyType(ptype.getText());
            policy.setPolicyContent(pcontent.getText());
            policy.setCoverRate(Double.parseDouble(prate.getText()));
            policy.setPolicyOwner(powner.getText());
            showNotification("Policy updated successfully!", Alert.AlertType.INFORMATION);

            ObservableList<Policy> policies = new ClaimDB().getPoliciesByUser();
            policy_table.setItems(policies);
            policy_table.refresh();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void loadPolicy() {
        policy_table.refresh();
        ObservableList<Policy> policies = new ClaimDB().getPoliciesByUser();
        Platform.runLater(() -> listPolicyTable(policies));
        policy_table.refresh();
    }

    private void listPolicyTable(ObservableList<Policy> policies) {
        pID.setCellValueFactory(new PropertyValueFactory<>("policyID"));
        pName.setCellValueFactory(new PropertyValueFactory<>("policyName"));
        pContent.setCellValueFactory(new PropertyValueFactory<>("policyContent"));
        pType.setCellValueFactory(new PropertyValueFactory<>("policyType"));
        pCoverRate.setCellValueFactory(new PropertyValueFactory<>("coverRate"));
        POwner.setCellValueFactory(new PropertyValueFactory<>("policyOwner"));
        policy_table.setItems(policies);
    }

    private void deletePolicy() {
        try {
            ObservableList<Policy> policies = multipleSelectPolicy();
            ClaimDB claimDB = new ClaimDB();

            if (policies.isEmpty()) {
                showNotification("Please select a record to delete!", Alert.AlertType.WARNING);
                return;
            }
            for (Policy policy : policies) {
                claimDB.deletePolicy(policy);
            }
            policy_table.getItems().removeAll(policies);
            showNotification("Policy deleted successfully!", Alert.AlertType.INFORMATION);
            policy_table.refresh();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<Policy> multipleSelectPolicy() {
        TableViewSelectionModel<Policy> selectionModel = policy_table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        return selectionModel.getSelectedItems();
    }

    private void addPolicy() {
        try {
            Policy policy = getPolicyTextField();
            new ClaimDB().addPolicy(policy);
            showNotification("Policy added successfully!", Alert.AlertType.INFORMATION);

            clearPolicyTextField();
        } catch (Exception e) {
            showNotification("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void clearPolicyTextField() {
        for (Object obj : policy_form.getChildren()) {
            if (obj instanceof TextField) {
                ((TextField) obj).clear();
            }
        }
    }

    private Policy getPolicyTextField() {
        String policyID = pid.getText();
        String policyName = pname.getText();
        String policyType = ptype.getText();
        String policyContent = pcontent.getText();
        double coverRate = Double.parseDouble(prate.getText());
        String policyOwner = powner.getText();
        return new Policy(policyID, policyName, policyType, policyContent, coverRate, policyOwner);
    }
    private void enterNewPolicy() {
        pid.setEditable(true);
        pname.setEditable(true);
        ptype.setEditable(true);
        pcontent.setEditable(true);
        prate.setEditable(true);
        powner.setEditable(true);
        clearPolicyTextField();
    }
    private void populatePolicyDetails(Policy policy) {
        pid.setText(policy.getPolicyID());
        pname.setText(policy.getPolicyName());
        ptype.setText(policy.getPolicyType());
        pcontent.setText(policy.getPolicyContent());
        prate.setText(Double.toString(policy.getCoverRate()));
        powner.setText(policy.getPolicyOwner());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayDashboard(true, false, false, false, false);
        setPrivilege();
        // display the list of customer immediately
//        viewCustomer()
        currentUser.setText(UserDB.getCurrentUser()[0]);
        cust_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cust_table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            setProperties((newSelection));
        });
        // Setup for claim table
        claim_tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        claim_tableView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                setClaimToTextField(newSelection);
            }
        });
        custSearch.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("ENTER")) {
                searchCustomer();
            }
        });

        searchClaim.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("ENTER")) {
                findClaim();
            }
        });

        policy_table.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                populatePolicyDetails(newValue);
            }
        });


    }
}
