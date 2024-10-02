package com.av.avmessenger.Class;

public class Autorisation {
    private int idDemande, idUser;
    private String raison, actionDem, dateDebut, dateFin, email;

    public Autorisation(int idDemande, int idUser, String raison, String actionDem,
                        String dateDebut, String dateFin, String email) {
        this.idDemande = idDemande;
        this.idUser = idUser;
        this.raison = raison;
        this.actionDem = actionDem;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.email = email;
    }

    public String getRaison() { return raison; }
    public String getEmail() { return email; }
    public String getDateDebut(){ return dateDebut;}
    public String getDateFin(){ return dateFin;}

    public int getIdDemande() {
        return idDemande;
    }
}
