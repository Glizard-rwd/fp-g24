package org.claimsystem.g24fp.model.user;

import java.time.LocalDateTime;

public abstract class User {
    protected String username;
    protected String password;
    protected LocalDateTime createTime;
    protected LocalDateTime lastVisitTime;

    public User() {
        this.username = "default";
        this.password = "default";
        this.createTime = LocalDateTime.now();
        this.lastVisitTime = LocalDateTime.now();
    }

    public User(String username) {
        this.username = username;
        this.password = "hidden";
        this.createTime = LocalDateTime.now();
        this.lastVisitTime = LocalDateTime.now();
    }

    public void viewInfo() {
        System.out.println("USER INFO: ");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Create Time: " + createTime);
        System.out.println("Last Visit Time: " + lastVisitTime);
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getLastVisitTime() {
        return lastVisitTime;
    }
    public void setLastVisitTime(LocalDateTime lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }
    @Override
    public abstract String toString();
}
