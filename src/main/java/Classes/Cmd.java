package Classes;

public class Cmd {

    private int idcmd;
    private int idemp;
    private String cinClient;
    private String datecmd;
    private double montant;
    private String etatcmd;

    public Cmd(int idcmd, int idemp, String cinClient, String datecmd, double montant, String etatcmd) {
        this.idcmd = idcmd;
        this.idemp = idemp;
        this.cinClient = cinClient;
        this.datecmd = datecmd;
        this.montant = montant;
        this.etatcmd = etatcmd;
    }


    public Cmd(int idcmd, int idemp, String datecmd, double montant, String etatcmd) {
        this.idcmd = idcmd;
        this.idemp = idemp;
        this.datecmd = datecmd;
        this.montant = montant;
        this.etatcmd = etatcmd;
    }


    public String getCinClient() {
        return cinClient;
    }

    public void setCinClient(String cinClient) {
        this.cinClient = cinClient;
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
