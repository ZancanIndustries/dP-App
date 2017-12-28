package zi.dpapp.models;

/**
 * Created by giacomo.zancan on 22/12/2017.
 */

public class User {
    private String uid;
    private String nome;
    private String twitchNick;
    private String role;

    public User() {
    }

    public User(String nome, String twitchNick, String role) {
        this.nome = nome;
        this.twitchNick = twitchNick;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTwitchNick() {
        return twitchNick;
    }

    public void setTwitchNick(String twitchNick) {
        this.twitchNick = twitchNick;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
