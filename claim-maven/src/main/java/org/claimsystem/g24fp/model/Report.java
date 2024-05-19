package org.claimsystem.g24fp.model;

public class Report {
    private String policyOwner;
    private String year;
    private double totalClaimAmount;

    public Report() {

    }
    public Report(String policyOwner, String year, double totalClaimAmount) {
        this.policyOwner = policyOwner;
        this.year = year;
        this.totalClaimAmount = totalClaimAmount;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getTotalClaimAmount() {
        return totalClaimAmount;
    }

    public void setTotalClaimAmount(double totalClaimAmount) {
        this.totalClaimAmount = totalClaimAmount;
    }
}
