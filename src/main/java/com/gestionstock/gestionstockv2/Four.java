package com.gestionstock.gestionstockv2;

public class Four {
    private int idfour;
    private String nomfour;
    private String addfour;
    private String telfour;
    private String mailfour;

    public Four(int idfour, String nomfour, String addfour, String telfour, String mailfour) {
        this.idfour = idfour;
        this.nomfour = nomfour;
        this.addfour = addfour;
        this.telfour = telfour;
        this.mailfour = mailfour;
    }

    public int getIdfour() {
        return idfour;
    }

    public void setIdfour(int idfour) {
        this.idfour = idfour;
    }

    public String getNomfour() {
        return nomfour;
    }

    public void setNomfour(String nomfour) {
        this.nomfour = nomfour;
    }

    public String getAddfour() {
        return addfour;
    }

    public void setAddfour(String addfour) {
        this.addfour = addfour;
    }

    public String getTelfour() {
        return telfour;
    }

    public void setTelfour(String telfour) {
        this.telfour = telfour;
    }

    public String getMailfour() {
        return mailfour;
    }

    public void setMailfour(String mailfour) {
        this.mailfour = mailfour;
    }
}
