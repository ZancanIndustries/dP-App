package zi.dpapp.models;

/**
 * Created by giacomo.zancan on 21/12/2017.
 */

public class Team {
    private int _id;
    private String nome;
    private String gioco;
    private int logo;

    public Team(int _id, String nome, String gioco, int logo) {
        this._id = _id;
        this.nome = nome;
        this.gioco = gioco;
        this.logo = logo;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGioco() {
        return gioco;
    }

    public void setGioco(String gioco) {
        this.gioco = gioco;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
