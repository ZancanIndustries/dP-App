package zi.dpapp.models;

public class Streamer {
    private int _id;
    private String nome;
    private String logo;
    private boolean live;

    public Streamer(int _id, String nome, String logo, boolean live) {
        this._id = _id;
        this.nome = nome;
        this.logo = logo;
        this.live = live;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
