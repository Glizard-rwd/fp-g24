package org.claimsystem.g24fp.model.user;



public class Customer extends User {
    private String customerID;
    private String customerName;
    private String custType;
    private String userName;
    private String policyHolder;
    private String policyOwner;
    private String depRelationship; // dependent relationship if a customer is a dependent
    public Customer() {
        super();
        this.customerID = "default";
        this.customerName = "default";
    }

    public Customer(String customerID, String customerName) {
        super();
        this.customerID = customerID;
        this.customerName = customerName;
    }

    public Customer(String customerID, String customerName, String custType, String userName, String policyHolder, String policyOwner, String depRelationship) {
        super(userName);
        this.customerID = customerID;
        this.customerName = customerName;
        this.custType = custType;
        this.userName = userName;
        this.policyHolder = policyHolder;
        this.policyOwner = policyOwner;
        this.depRelationship = depRelationship;
    }

    public void viewInfo() {
        System.out.println("CUSTOMER INFO: ");
        super.viewInfo();
        System.out.println("Customer ID: " + customerID);
        System.out.println("Customer Name: " + customerName);
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public String getDepRelationship() {
        return depRelationship;
    }

    public void setDepRelationship(String depRelationship) {
        this.depRelationship = depRelationship;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}



