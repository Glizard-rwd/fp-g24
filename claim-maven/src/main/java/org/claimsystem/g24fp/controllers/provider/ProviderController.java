package org.claimsystem.g24fp.controllers.provider;

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
import org.claimsystem.g24fp.model.Claim;
import org.claimsystem.g24fp.model.ClaimDB;
import org.claimsystem.g24fp.model.Report;
import org.claimsystem.g24fp.model.UserDB;
import org.claimsystem.g24fp.model.user.Customer;
import org.claimsystem.g24fp.model.user.Provider;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProviderController extends LoginController implements Initializable {

    @FXML private TableColumn<Claim, String> cid_col;

    @FXML private TableColumn<Claim, String> claim_col;
    @FXML private TableView<Claim> claimtb;
@FXML private TableColumn<Claim, String> custName;
    @FXML private TableColumn<Customer, String> custType;
    @FXML private TableColumn<Customer, String> custid;
    @FXML private TableView<Customer> custtb;
    @FXML private TableColumn<Claim, String> edate_col;
    @FXML private AnchorPane managerFunction;
    @FXML private TableColumn<Claim, String> pb_col;
    @FXML private TableColumn<Claim, String> person_col;
    @FXML private TableColumn<Customer, String> pholder;
    @FXML private TableColumn<Claim, String> policy_col;
    @FXML private TableColumn<Customer, String> powner;
    @FXML private Button processBtn;
    @FXML private TextField processF;
    @FXML private TableColumn<Claim, String> r_claimamount;
    @FXML private TableColumn<String, String> r_powner;
    @FXML private TableColumn<String, String> r_year;
    @FXML private TableColumn<Customer, String> relationship;
    @FXML private TableView<Report> reporttb;
    @FXML private TableColumn<Claim, String> rq_col;
    @FXML private Button sendBtn;
    @FXML private TextField sentManagerF;
    @FXML private TableColumn<Claim, String> status_col;
    @FXML private AnchorPane surveyorFunction;
    @FXML private Text account;
    @FXML private TableColumn<Customer, String> custUname;
    @FXML private Button logoutbtn;
    @FXML private TableColumn<Claim, String> claimD_col;
    @FXML private Button loadClaim;
    @FXML private Button loadCust;
    @FXML private Button loadReport;
    @FXML private TextArea provInfo;
    @FXML private Tab isurveyorMenu;
    @FXML private TextArea surveyorList;
    @FXML public Button search_claim_btn;
    @FXML public TextField search_claim_ad;
    @FXML public TextField search_cust_ad;
    private final UserDB userDB = new UserDB();
    private final ClaimDB claimDB = new ClaimDB();

    public void viewInfo() {
        Provider currentUser = userDB.getAllProviders().filtered(provider -> provider.getUsername().equals(UserDB.getCurrentUser()[0])).get(0);
        account.setText(currentUser.getUsername());
        String info = "Username: " + currentUser.getUsername() + "\n" +
                "Create Time: " + currentUser.getCreateTime() + "\n" +
                "Last Visit Time: " + currentUser.getLastVisitTime() + "\n"+
                "Manager: " + currentUser.getManager() + "\n";
        provInfo.setText(info);
    }

    public void displayButton() {
        Provider currentUser = userDB.getAllProviders().filtered(provider -> provider.getUsername().equals(UserDB.getCurrentUser()[0])).get(0);
        if (currentUser.getPosition().equals("insurance manager")) {
            surveyorFunction.setVisible(false);
        } else if (currentUser.getPosition().equals("insurance surveyor")) {
            managerFunction.setVisible(false);
            isurveyorMenu.setDisable(true);
        }
    }

    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                Provider currentUser = userDB.getAllProviders().filtered(provider -> provider.getUsername().equals(UserDB.getCurrentUser()[0])).get(0);
                currentUser.setLastVisitTime(LocalDateTime.now());
                // exit the current stage
                logoutbtn.getScene().getWindow().hide();
                // turn back to the login form
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Login.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }



    public void loadActions(ActionEvent even) {
        if (even.getSource() == loadClaim) {
            loadClaim();
        } else if (even.getSource() == loadCust) {
            loadCustomer();
        } else if (even.getSource() == loadReport) {
            loadReport();
        } else if (even.getSource() == sendBtn) {
            sendClaimToManager();
        } else if (even.getSource() == processBtn) {
            processClaim();
        }
    }

    private void loadReport() {
        // load report
        ObservableList<Report> reports = claimDB.getAnnualClaimAmount();
        Platform.runLater(() -> listReportTable(reports));
        reporttb.refresh();
    }

    private void listReportTable(ObservableList<Report> reports) {
        r_powner.setCellValueFactory(new PropertyValueFactory<>("policyOwner"));
        r_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        r_claimamount.setCellValueFactory(new PropertyValueFactory<>("totalClaimAmount"));
        reporttb.setItems(reports);
    }


    public void loadClaim() {
        claimtb.refresh(); // refresh the table
        ObservableList<Claim> claims = claimDB.getAllClaims();
        Platform.runLater(() -> listClaimTable(claims));
        claimtb.refresh();
    }

    private void listClaimTable(ObservableList<Claim> claims) {
        claimToTable(claims);
    }

    public void loadCustomer() {
        ObservableList<Customer> customers = userDB.getAllCustomer();
        Platform.runLater(() -> listCustomerTable(customers));
        custtb.refresh();
    }

    private void listCustomerTable(ObservableList<Customer> customers) {
        custid.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custUname.setCellValueFactory(new PropertyValueFactory<>("username"));
        custType.setCellValueFactory(new PropertyValueFactory<>("custType"));
        powner.setCellValueFactory(new PropertyValueFactory<>("policyOwner"));
        pholder.setCellValueFactory(new PropertyValueFactory<>("policyHolder"));
        relationship.setCellValueFactory(new PropertyValueFactory<>("depRelationship"));
        custtb.setItems(customers);
    }

    public void sendClaimToManager() {
        Claim selectedClaim = claimtb.getSelectionModel().getSelectedItem();

        if (selectedClaim != null) {
            selectedClaim.setProcessBy(sentManagerF.getText());
            selectedClaim.setClaimStatus("processing");
            selectedClaim.setExamDate(LocalDateTime.now());
            claimDB.updateClaim(selectedClaim);
            loadClaim();
        }
    }

    public void processClaim() {
        Claim selectedClaim = claimtb.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            selectedClaim.setClaimStatus(processF.getText()); // type approved or denied status
            selectedClaim.setExamDate(LocalDateTime.now());
            claimDB.updateClaim(selectedClaim); // update the claim
            loadClaim();
        }
    }

    public void searchAllClaim() {
        ObservableList<Claim> claims = new ClaimDB().searchClaim(search_claim_ad.getText());
        claimToTable(claims);
        claimtb.refresh();
    }

    private void claimToTable(ObservableList<Claim> claims) {
        cid_col.setCellValueFactory(new PropertyValueFactory<>("claimID"));
        person_col.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        rq_col.setCellValueFactory(new PropertyValueFactory<>("requestAmount"));
        claim_col.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
        policy_col.setCellValueFactory(new PropertyValueFactory<>("appliedPolicy"));
        edate_col.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("claimStatus"));
        pb_col.setCellValueFactory(new PropertyValueFactory<>("processBy"));
        claimD_col.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        claimtb.setItems(claims);
    }

    public void searchAllCustomer() {
        ObservableList<Customer> customers = new UserDB().search(search_cust_ad.getText());
        listCustomerTable(customers);
        custtb.setItems(customers);
        custtb.refresh();
    }

    public void listAllSurveyor() {
        ObservableList<Provider> surveyors = userDB.getProviderByManager();
        for (Provider surveyor : surveyors) {
            surveyorList.appendText("Surveyor: " + surveyor.getProviderID() + "\n");
            surveyorList.appendText(surveyor.getUsername() + "\n");
            surveyorList.appendText("Manager: " + surveyor.getManager() + "\n");
            surveyorList.appendText("Position: " + surveyor.getPosition() + "\n");
            surveyorList.appendText("Create Time: " + surveyor.getCreateTime() + "\n");
            surveyorList.appendText("Last Visit Time: " + surveyor.getLastVisitTime() + "\n");
            surveyorList.appendText("\n");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewInfo();
        listAllSurveyor();
        displayButton();
    }

}
