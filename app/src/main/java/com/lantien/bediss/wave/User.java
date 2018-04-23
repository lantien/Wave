package com.lantien.bediss.wave;

/**
 * Created by bediss on 30/03/2018.
 */

public class User {

    private String username;
    private String name;
    private String birthday;

    public User() {}

    public User(String userPara, String nomPara, String birthdayPara) {
        // ...
        username = userPara;
        name = nomPara;
        birthday = birthdayPara;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }
}
