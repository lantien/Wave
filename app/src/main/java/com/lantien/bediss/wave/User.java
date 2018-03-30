package com.lantien.bediss.wave;

/**
 * Created by bediss on 30/03/2018.
 */

public class User {

    private String name;
    private String nom;

    public User() {}

    public User(String nomPara, String prenomPara) {
        // ...
        nom = nomPara;
        name = prenomPara;
    }

    public String getName() {
        return name;
    }

    public String getNom() {
        return nom;
    }
}
