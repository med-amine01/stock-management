package Classes;

public class Lc {
    private int idlc;
    private int idcmd;
    private int idpiece;
    private int qtelc;
    private double prixlc;

    public Lc(int idlc, int idcmd, int idpiece, int qtelc, double prixlc) {
        this.idlc = idlc;
        this.idcmd = idcmd;
        this.idpiece = idpiece;
        this.qtelc = qtelc;
        this.prixlc = prixlc;
    }

    public int getIdlc() {
        return idlc;
    }

    public void setIdlc(int idlc) {
        this.idlc = idlc;
    }

    public int getIdcmd() {
        return idcmd;
    }

    public void setIdcmd(int idcmd) {
        this.idcmd = idcmd;
    }

    public int getIdpiece() {
        return idpiece;
    }

    public void setIdpiece(int idpiece) {
        this.idpiece = idpiece;
    }

    public int getQtelc() {
        return qtelc;
    }

    public void setQtelc(int qtelc) {
        this.qtelc = qtelc;
    }

    public double getPrixlc() {
        return prixlc;
    }

    public void setPrixlc(double prixlc) {
        this.prixlc = prixlc;
    }
}
