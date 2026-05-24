package com.customerapp.model;

public class Customer {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String createdAt;

    public Customer() {}

    public Integer getId()        { return id; }
    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getPhone()      { return phone; }
    public String getPassword()   { return password; }
    public String getCreatedAt()  { return createdAt; }

    public void setId(Integer id)            { this.id = id; }
    public void setName(String name)         { this.name = name; }
    public void setEmail(String email)       { this.email = email; }
    public void setPhone(String phone)       { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() { return name; }
}