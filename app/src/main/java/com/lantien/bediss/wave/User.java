package com.lantien.bediss.wave;

/**
 * Created by bediss on 30/03/2018.
 */

public class User {

    private String username;
    private String name;
    private String birthday;
    private String country;
    private String bio;
    private String currentlyPlaying;
    private String location;
    private String website;

    public User() {}

    public User(String userPara, String nomPara, String birthdayPara, String countryPara,
                String bioPara, String currentPara, String locPara, String websitePara) {

        username = userPara;
        name = nomPara;
        birthday = birthdayPara;
        country = countryPara;
        bio = bioPara;
        currentlyPlaying= currentPara;
        location = locPara;
        website = websitePara;
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

    public String getCountry() {
        return country;
    }

    public String getBio() {
        return bio;
    }

    public String getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }
}
