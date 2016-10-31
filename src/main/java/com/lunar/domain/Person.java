package com.lunar.domain;

import java.util.Date;

/**
 * Created by gadominas on 10/30/16.
 */
public class Person {
    private String firstname;
    private String lastname;
    private Date dob;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
