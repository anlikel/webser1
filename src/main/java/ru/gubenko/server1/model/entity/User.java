package ru.gubenko.server1.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.Collection;


@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable=false)
    private String username;

    @Column(unique=true,nullable = false)
    private String password;

    @Email
    @Column(unique=true,nullable=true)
    private String email;

    @Column(nullable=true)
    @Pattern(
            regexp = "^\\+[0-9\\s\\-()]{5,20}$",
            message = "Телефон должен быть в формате +XXX XXX-XX-XX"
    )
    private String phone;

    @Column(name="registration_date",nullable=false,updatable = false)
    private LocalDateTime registrationDate;

    private boolean enabled;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name="users_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Collection<Role> roles;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
