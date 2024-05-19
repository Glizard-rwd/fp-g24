package org.claimsystem.g24fp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class ClaimDB {
    private final Connection connection;
    private ObservableList<Claim> claimList;
    private final String[] currentUser = UserDB.getCurrentUser();


    public ClaimDB() {
        connection = DBConnection.getConnection();
        claimList = FXCollections.observableArrayList();
    }

    public ObservableList<Claim> getAllClaims() {
        String query = "SELECT * FROM claim";
        try {
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                claimList.add(new Claim(rs.getString("id"), rs.getString("insured_person"), rs.getDouble("request_amount"), rs.getString("applied_policy"), rs.getTimestamp("claim_date").toLocalDateTime(), rs.getTimestamp("exam_date").toLocalDateTime(), rs.getString("claim_status"), rs.getDouble("claim_amount"), rs.getString("process_by")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claimList;
    }

    public ObservableList<Claim> getClaimByUser() {
        String findID = "SELECT id from customer where user_name = ?;";
        String getClaimSQL = """
                with select_customer as (
                    select claim.*, customer.policy_owner, customer.policy_holder, customer.user_name from claim join customer on claim.insured_person = customer.id
                ) select * from select_customer where insured_person = ? or policy_owner = ? or policy_holder = ?;
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(findID);
            ps.setString(1, currentUser[0]);
            ResultSet rs = ps.executeQuery();
            String id = "";
            if (rs.next()) {
                id = rs.getString("id");
            }
            System.out.println(id);
            PreparedStatement ps1 = connection.prepareStatement(getClaimSQL);
            ps1.setString(1, id);
            ps1.setString(2, id);
            ps1.setString(3, id);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                claimList.add(new Claim(rs1.getString("id"), rs1.getString("insured_person"), rs1.getDouble("request_amount"), rs1.getString("applied_policy"), rs1.getTimestamp("claim_date").toLocalDateTime(), rs1.getTimestamp("exam_date").toLocalDateTime(), rs1.getString("claim_status"), rs1.getDouble("claim_amount"), rs1.getString("process_by")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claimList;
    }

    public void addClaim(Claim claim) {
        // find the policy.coverage
        String addClaimSQL = "INSERT INTO claim (id, insured_person, request_amount, applied_policy, claim_status, process_by) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement ps = connection.prepareStatement(addClaimSQL);
            ps.setString(1, claim.getClaimID());
            ps.setString(2, claim.getInsuredPerson());
            ps.setDouble(3, claim.getRequestAmount());
            ps.setString(4, claim.getAppliedPolicy());
            ps.setString(5, claim.getClaimStatus());
            ps.setString(6, claim.getProcessBy());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Add claim " + claim.getClaimID() + " info");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClaim(Claim claim) {
        //TODO: delete the document associated with the claim
        String deleteSQL = "DELETE FROM claim WHERE id = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(deleteSQL);
            ps.setString(1, claim.getClaimID());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Delete claim " + claim.getClaimID() + " info");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClaim(Claim claim) {
        // can update only request amount, applied policy, claim status, process by
        // cannot update claim amount, claim date, exam date
        String updateSQL = "UPDATE claim SET request_amount = ?, applied_policy = ?, claim_status = ?, process_by = ? WHERE id = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(updateSQL);
            ps.setDouble(1, claim.getRequestAmount());
            ps.setString(2, claim.getAppliedPolicy());
            ps.setString(3, claim.getClaimStatus());
            ps.setString(4, claim.getProcessBy());
            ps.setString(5, claim.getClaimID());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Update claim " + claim.getClaimID() + " info");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Claim> searchClaim(String searchWord) {
        String SQL = "SELECT * FROM claim" +
                "            WHERE id ILIKE ?" +
                "                OR insured_person ILIKE ?" +
                "                OR CAST(request_amount AS TEXT) ILIKE ?" +
                "                OR applied_policy ILIKE ?" +
                "                OR claim_status ILIKE ?" +
                "                OR process_by ILIKE ?" +
                "                OR CAST(claim_date AS TEXT) ILIKE ?" +
                "                OR CAST(exam_date AS TEXT) ILIKE ?" +
                "                OR CAST(claim_amount AS TEXT) ILIKE ?" +
                "            ;";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            String searchTerm = "%" + searchWord + "%";
            for (int i = 1; i <= 9; i++) {
                ps.setString(i, searchTerm);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                claimList.add(new Claim(rs.getString("id"), rs.getString("insured_person"), rs.getDouble("request_amount"), rs.getString("applied_policy"), rs.getTimestamp("claim_date").toLocalDateTime(), rs.getTimestamp("exam_date").toLocalDateTime(), rs.getString("claim_status"), rs.getDouble("claim_amount"), rs.getString("process_by")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claimList;
    }

    public ObservableList<Claim> searchByUser(String searchWord) {
        String searchQuery = """
        WITH select_customer AS (
            SELECT id FROM customer WHERE user_name = ?
        )
        SELECT c1.*, c.user_name 
        FROM claim c1 
        JOIN customer c ON c1.insured_person = c.id 
        WHERE 
            (c1.insured_person IN (SELECT id FROM select_customer) 
            OR c.policy_holder IN (SELECT id FROM select_customer) 
            OR c.policy_owner IN (SELECT id FROM select_customer))
            AND (
                c1.id ILIKE ? 
                OR c1.insured_person ILIKE ? 
                OR CAST(c1.request_amount AS TEXT) ILIKE ? 
                OR c1.applied_policy ILIKE ? 
                OR c1.claim_status ILIKE ? 
                OR c1.process_by ILIKE ? 
                OR CAST(c1.claim_date AS TEXT) ILIKE ? 
                OR CAST(c1.exam_date AS TEXT) ILIKE ? 
                OR CAST(c1.claim_amount AS TEXT) ILIKE ?
            );
    """;
        try {
            PreparedStatement ps = connection.prepareStatement(searchQuery);
            String searchTerm = "%" + searchWord + "%";
            ps.setString(1, UserDB.getCurrentUser()[0]);
            for (int i = 2; i <= 10; i++) {
                ps.setString(i, searchTerm);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                claimList.add(new Claim(rs.getString("id"), rs.getString("insured_person"), rs.getDouble("request_amount"), rs.getString("applied_policy"), rs.getTimestamp("claim_date").toLocalDateTime(), rs.getTimestamp("exam_date").toLocalDateTime(), rs.getString("claim_status"), rs.getDouble("claim_amount"), rs.getString("process_by")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claimList;

    }
    public ObservableList<Report> getAnnualClaimAmount() {
        // annual claim amount group by policy owner
        ObservableList<Report> annualClaimAmountList = FXCollections.observableArrayList();
        String annualClaimAmount = """
                SELECT
                    c.policy_owner,
                    EXTRACT(YEAR FROM cl.claim_date) AS claim_year,
                    SUM(cl.claim_amount) AS total_claim_amount
                FROM
                    claim cl
                JOIN
                    customer c ON cl.insured_person = c.id
                WHERE
                    c.policy_owner IS NOT NULL
                GROUP BY
                    c.policy_owner, EXTRACT(YEAR FROM cl.claim_date)
                ORDER BY
                    c.policy_owner, claim_year;
                """;

        try {
            ResultSet rs = connection.createStatement().executeQuery(annualClaimAmount);
            while (rs.next()) {
                annualClaimAmountList.add(new Report(rs.getString("policy_owner"), rs.getString("claim_year"), rs.getDouble("total_claim_amount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return annualClaimAmountList;
    };


    // CRUD insurance card
    public void addInsuranceCard(InsuranceCard insuranceCard) {
        String addInsuranceCardSQL = "INSERT INTO insurance_card (id, card_holder, expiration_date) VALUES (?, ?, ?);";
        try {
            PreparedStatement ps = connection.prepareStatement(addInsuranceCardSQL);
            ps.setString(1, insuranceCard.getCardID());
            ps.setString(2, insuranceCard.getCardHolder());
            ps.setDate(3, Date.valueOf(insuranceCard.getExpireDate()));
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Add insurance card " + insuranceCard.getCardID() + " info");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<InsuranceCard> getAllInsuranceCards() {
        ObservableList<InsuranceCard> insuranceCards = FXCollections.observableArrayList();
        String getInsuranceCardSQL = "select ic.id, ic.card_holder, ic.expiration_date from insurance_card ic;";
        try {
            ResultSet rs = connection.createStatement().executeQuery(getInsuranceCardSQL);
            while (rs.next()) {
                insuranceCards.add(new InsuranceCard(rs.getString("id"), rs.getString("card_holder"), rs.getString("expiration_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insuranceCards;
    }

    public ObservableList<InsuranceCard> getCardByUser() {
        // get all the insurance card of the current user
        // and their dependents or beneficiaries
        String currentUser = UserDB.getCurrentUser()[0];
        String findID = "SELECT id from customer where user_name = ?;";
        String getCardSQL = """
                with select_customer as (
                    select insurance_card.*, customer.policy_owner, customer.policy_holder, customer.user_name from insurance_card join customer on insurance_card.card_holder = customer.id
                ) select * from select_customer where card_holder = ? or policy_owner = ? or policy_holder = ?;
                """;

        ObservableList<InsuranceCard> insuranceCards = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement(findID);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            String id = "";
            if (rs.next()) {
                id = rs.getString("id");
            }
            System.out.println(id);
            PreparedStatement ps1 = connection.prepareStatement(getCardSQL);
            ps1.setString(1, id);
            ps1.setString(2, id);
            ps1.setString(3, id);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                insuranceCards.add(new InsuranceCard(rs1.getString("id"), rs1.getString("card_holder"), rs1.getString("expiration_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insuranceCards;
    }
    public void deleteInsuranceCard(InsuranceCard card) {
        String deleteSQL = "DELETE FROM insurance_card WHERE id = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(deleteSQL);
            ps.setString(1, card.getCardID());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Delete insurance card " + card.getCardID() + " info");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<Policy> getAllPolicies() {
        ObservableList<Policy> policies = FXCollections.observableArrayList();
        String getPolicySQL = "select * from policy;";
        try {
            ResultSet rs = connection.createStatement().executeQuery(getPolicySQL);
            while (rs.next()) {
                Policy policy = new Policy(rs.getString("id"), rs.getString("policy_name"), rs.getString("policy_type"), rs.getString("content"), rs.getDouble("cover_rate"), rs.getString("policy_owner"));
                policies.add(policy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policies;
    }
    public ObservableList<Policy> getPoliciesByUser() {
        String currentUser = UserDB.getCurrentUser()[0];
        String findID = "SELECT id from customer where user_name = ?;";
        String getPolicySQL = """
                with select_customer as (
                    select policy.*, customer.user_name from policy join customer on policy.policy_owner = customer.id
                ) select * from select_customer where policy_owner = ?;
                """;

        ObservableList<Policy> policies = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement(findID);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            String id = "";
            if (rs.next()) {
                id = rs.getString("id");
            }
            System.out.println(id);
            PreparedStatement ps1 = connection.prepareStatement(getPolicySQL);
            ps1.setString(1, id);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                policies.add(new Policy(rs1.getString("id"), rs1.getString("policy_name"), rs1.getString("policy_type"), rs1.getString("policy_content"), rs1.getDouble("cover_rate"), rs1.getString("policy_owner")));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return policies;
    }
    public void addPolicy(Policy policy) {
        String findID = "SELECT id from customer where user_name = ?;";
        String addPolicySQL = "INSERT INTO policy (id, policy_name, policy_type, policy_content, cover_rate, policy_owner) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            String id = "";
            PreparedStatement ps = connection.prepareStatement(findID);
            ps.setString(1, currentUser[0]);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getString("id");
            }
            PreparedStatement ps1 = connection.prepareStatement(addPolicySQL);
            ps1.setString(1, policy.getPolicyID());
            ps1.setString(2, policy.getPolicyName());
            ps1.setString(3, policy.getPolicyType());
            ps1.setString(4, policy.getPolicyContent());
            ps1.setDouble(5, policy.getCoverRate());
            ps1.setString(6, id);
            ps1.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Add policy " + policy.getPolicyID() + " info");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void updatePolicy(Policy policy) {
        String updateSQL = "UPDATE policy SET policy_name = ?, policy_type = ?, policy_content = ?, cover_rate = ?, policy_owner = ? WHERE id = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(updateSQL);
            ps.setString(1, policy.getPolicyName());
            ps.setString(2, policy.getPolicyType());
            ps.setString(3, policy.getPolicyContent());
            ps.setDouble(4, policy.getCoverRate());
            ps.setString(5, policy.getPolicyOwner());
            ps.setString(6, policy.getPolicyID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePolicy(Policy policy) {
        String deleteSQL = "DELETE FROM policy WHERE id = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(deleteSQL);
            ps.setString(1, policy.getPolicyID());
            ps.executeUpdate();
            TriggerHandle.recordAction(currentUser[0], "Delete policy " + policy.getPolicyID() + " info");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: HANDLE DOCUMENTS


}