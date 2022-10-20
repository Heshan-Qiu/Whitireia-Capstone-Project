package org.nzarra;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Account {

    private @Id @GeneratedValue Long id;
    private String username;
    private String password;
    private String role;
    private Boolean active;
    private String fullName;

    protected Account() {}

    public Account(String username, String password, String role, Boolean active, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
        this.fullName = fullName;
    }

    public static final String ROLE_ADMINISTRATOR = "Administrator";
    public static final String ROLE_COMPETITION_ADMINISTRATOR = "Competition Administrator";
    public static final String ROLE_JUDGE = "Judge";
    public static final String ROLE_MARSHALL = "Marshall";
    public static final String ROLE_SCRUTINEER = "Scrutineer";

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Account user = (Account) obj;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) &&
                Objects.equals(role, user.role) && Objects.equals(active, user.active) &&
                Objects.equals(fullName, user.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, role, active, fullName);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "', active=" + active +
                ", fullName='" + fullName + "'}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String type) {
        this.role = type;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean status) {
        this.active = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
