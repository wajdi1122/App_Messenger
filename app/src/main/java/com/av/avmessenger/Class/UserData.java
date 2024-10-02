package com.av.avmessenger.Class;

public class UserData {
    private String nom_user;
    private String prenom_user;
    private String email_user;
    private String password_user;
    private String status_user;
    private String telephone_user;
    private String departement_user;
    private String cin_user;
    private String adress_user;

    // Constructeur
    public UserData(String nom_user, String prenom_user, String email_user, String password_user, String status_user,
                    String telephone_user, String departement_user, String cin_user, String adress_user) {
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.password_user = password_user;
        this.status_user = status_user;
        this.telephone_user = telephone_user;
        this.departement_user = departement_user;
        this.cin_user = cin_user;
        this.adress_user = adress_user;
    }

    public String getNom() {
        return nom_user;
    }

    public void setNom(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getEmail() {
        return email_user;
    }

    public void setEmail(String email_user) {
        this.email_user = email_user;
    }

    public String getPassword() {
        return password_user;
    }

    public void setPassword_user(String password_user) {
        this.password_user = password_user;
    }

    public String getStatus() {
        return status_user;
    }

    public void setStatus(String status_user) {
        this.status_user = status_user;
    }

    public String getTelephone() {
        return telephone_user;
    }

    public void setTelephone(String telephone_user) {
        this.telephone_user = telephone_user;
    }

    public String getDepartement() {
        return departement_user;
    }

    public void setDepartement_user(String departement_user) {
        this.departement_user = departement_user;
    }

    public String getCin() {
        return cin_user;
    }

    public void setCin_user(String cin_user) {
        this.cin_user = cin_user;
    }

    public String getAdresse() {
        return adress_user;
    }

    public void setAdress_user(String adress_user) {
        this.adress_user = adress_user;
    }


// Getters et setters si nécessaires
}
