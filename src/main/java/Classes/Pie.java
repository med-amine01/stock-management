package Classes;

public class Pie {
    private int idpiece ;
    private String marque;
    private String modele;
    private String serie;
    private int qte;
    private double prix;
    private int idfour;
    private String etat;
    private int qteeffective;


    public Pie(int idpiece, String marque, String modele, String serie, int qte, double prix,int idfour,String etat)
    {
        this.idpiece = idpiece;
        this.marque = marque;
        this.modele = modele;
        this.serie = serie;
        this.qte = qte;
        this.prix = prix;
        this.idfour = idfour;
        this.etat = etat;
    }

    public Pie(int idpiece, String marque, String modele, String serie, int qte, double prix,int idfour,String etat,int qteeffective)
    {
        this.idpiece = idpiece;
        this.marque = marque;
        this.modele = modele;
        this.serie = serie;
        this.qte = qte;
        this.prix = prix;
        this.idfour = idfour;
        this.etat = etat;
        this.qteeffective = qteeffective;
    }


    public int getQteeffective() {
        return qteeffective;
    }

    public void setQteeffective(int qteeffective) {
        this.qteeffective = qteeffective;
    }

    public int getIdpiece() {
        return idpiece;
    }

    public void setIdpiece(int idpiece) {
        this.idpiece = idpiece;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getIdfour() {
        return idfour;
    }

    public void setIdfour(int idfour) {
        this.idfour = idfour;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
