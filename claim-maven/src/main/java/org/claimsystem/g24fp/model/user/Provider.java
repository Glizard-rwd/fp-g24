package org.claimsystem.g24fp.model.user;

public class Provider extends User{

    private String providerID;
    private String providerName;
    private String manager;
    private String username;
    private String position;


    public Provider(String username) {
        super();
        this.username = username;
        this.providerID = "default";
        this.providerName = "default";
    }

    public Provider(String username, String providerID, String providerName, String username1) {
        super(username);
        this.providerID = providerID;
        this.providerName = providerName;
        this.username = username1;
    }

    public Provider(String providerID, String providerName, String manager, String username, String position) {
        super(username);
        this.providerID = providerID;
        this.providerName = providerName;
        this.manager = manager;
        this.username = username;
        this.position = position;
    }

    @Override
    public void viewInfo() {
        System.out.println("PROVIDER INFO: ");
        super.viewInfo();
        System.out.println("Provider ID: " + providerID);
        System.out.println("Provider Name: " + providerName);
    }

    @Override
    public String toString() {
        return "Provider{" +
                "providerID='" + providerID + '\'' +
                ", providerName='" + providerName + '\'' +
                '}';
    }
    // getter and setter
    

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }
}

