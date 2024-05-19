package org.claimsystem.g24fp.model.user;

public class Admin extends User{
    public String adminID;
    public Admin() {
        super();
    }

    public Admin(String username, String adminID) {
        super(username);
        this.adminID = adminID;
    }

    @Override
    public void viewInfo() {
        System.out.println("ADMIN INFO: ");
        super.viewInfo();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                '}';
    }
}
