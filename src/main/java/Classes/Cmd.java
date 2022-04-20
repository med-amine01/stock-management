package Classes;

public class Cmd {

    private int idcmd;
    private int idemp;
    private int idclient;
    private String datecmd;
    private double montant;
    private String etatcmd;

    public Cmd(int idcmd, int idclient,int idemp, String datecmd, double montant, String etatcmd) {
        this.idcmd = idcmd;
        this.idemp = idemp;
        this.idclient = idclient;
        this.datecmd = datecmd;
        this.montant = montant;
        this.etatcmd = etatcmd;
    }


    public int getIdcmd() {
        return idcmd;
    }

    public void setIdcmd(int idcmd) {
        this.idcmd = idcmd;
    }

    public int getIdemp() {
        return idemp;
    }

    public void setIdemp(int idemp) {
        this.idemp = idemp;
    }

    public int getIdclient() {
        return idclient;
    }

    public void setIdclient(int idclient) {
        this.idclient = idclient;
    }

    public String getDatecmd() {
        return datecmd;
    }

    public void setDatecmd(String datecmd) {
        this.datecmd = datecmd;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getEtatcmd() {
        return etatcmd;
    }

    public void setEtatcmd(String etatcmd) {
        this.etatcmd = etatcmd;
    }
}
