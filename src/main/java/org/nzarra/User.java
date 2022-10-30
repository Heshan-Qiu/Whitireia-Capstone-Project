package org.nzarra;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    private boolean active;

    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn (name = "username"))
    private List<String> roles;

    protected User() {}

    public User(String username, String password, boolean active, String fullName, List<String> roles) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.fullName = fullName;
        this.roles = roles;
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

        User user = (User) obj;
        return Objects.equals(username, user.username) && Objects.equals(active, user.active) &&
                Objects.equals(fullName, user.fullName) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, active, fullName, roles);
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', active=" + active + ", fullName='" + fullName + "', roles=" + roles + "}";
    }

    public Integer getId() {
        return id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean status) {
        this.active = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
