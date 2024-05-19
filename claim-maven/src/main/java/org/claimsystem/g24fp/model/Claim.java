package org.claimsystem.g24fp.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Claim {
    private String claimID;
    private String insuredPerson;
    private double requestAmount;
    private String appliedPolicy;
    private LocalDateTime claimDate;
    private LocalDateTime examDate;
    private String claimStatus;
    private double claimAmount;
    private String processBy;
    public Claim() {

    }

    public Claim(String id, String insuredPerson, double requestAmount, String status, double claimAmount) {
        this.claimID = id;
        this.insuredPerson = insuredPerson;
        this.requestAmount = requestAmount;
        this.claimStatus = status;
        this.claimAmount = claimAmount;
        this.claimDate = LocalDateTime.now();
        this.examDate = LocalDateTime.now();
    }
    public Claim(String claimID, String insuredPerson,  double requestAmount, String appliedPolicy, LocalDateTime claimDate, LocalDateTime examDate, String claimStatus, double claimAmount, String processBy) {
        this.claimID = claimID;
        this.insuredPerson = insuredPerson;
        this.requestAmount = requestAmount;
        this.appliedPolicy = appliedPolicy;
        this.claimDate = claimDate;
        this.examDate = examDate;
        this.claimStatus = claimStatus;
        this.claimAmount = claimAmount;
        this.processBy = processBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return Objects.equals(claimID, claim.claimID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(claimID);
    }

    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public String getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(String insuredPerson) {
        this.insuredPerson = insuredPerson;
    }


    public double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(double requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getAppliedPolicy() {
        return appliedPolicy;
    }

    public void setAppliedPolicy(String appliedPolicy) {
        this.appliedPolicy = appliedPolicy;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getProcessBy() {
        return processBy;
    }

    public void setProcessBy(String processBy) {
        this.processBy = processBy;
    }


}