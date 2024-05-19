package org.claimsystem.g24fp.model;

public class History {
    private String id;
    private String created_at;
    private String current_user;
    private String description;


    public History(String id, String current_user, String created_at, String description) {
        this.id = id;
        this.current_user = current_user;
        this.description = description;
        this.created_at = created_at;
    }

    public String getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }
}
