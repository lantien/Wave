package com.lantien.bediss.wave;

/**
 * Created by bediss on 30/03/2018.
 */

public class User {

    private String lastname;
    private String firstname;
    private String birthday;

    public User() {}

    public User(String nomPara, String prenomPara, String birthdayPara) {
        // ...
        lastname = nomPara;
        firstname = prenomPara;
        birthday = birthdayPara;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getBirthday() {
        return birthday;
    }
}
