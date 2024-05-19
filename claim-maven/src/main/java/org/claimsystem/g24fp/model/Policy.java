package org.claimsystem.g24fp.model;

import org.claimsystem.g24fp.model.user.Customer;

public class Policy {
    private String policyId;
    private String policyName;
    private String policyType;
    private String policyContent;
    private double coverRate;
    private String policyOwner;

    public Policy() {

    }
    // Constructor
    public Policy(String policyId, String policyName, String policyType, String policyContent, double coverRate, String policyOwner) {
        this.policyId = policyId;
        this.policyName = policyName;
        this.policyType = policyType;
        this.policyContent = policyContent;
        this.coverRate = coverRate;
        this.policyOwner = policyOwner;
    }

    // Getters and Setters
    public String getPolicyID() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getPolicyContent() {
        return policyContent;
    }

    public void setPolicyContent(String policyContent) {
        this.policyContent = policyContent;
    }

    public double getCoverRate() {
        return coverRate;
    }

    public void setCoverRate(double coverRate) {
        this.coverRate = coverRate;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    // toString method
    @Override
    public String toString() {
        return "Policy{" +
                "policyId='" + policyId + '\'' +
                ", policyName='" + policyName + '\'' +
                ", policyType='" + policyType + '\'' +
                ", policyContent='" + policyContent + '\'' +
                ", coverRate=" + coverRate +
                ", policyOwner='" + policyOwner + '\'' +
                '}';
    }
}

