package zi.dpapp.models;

/**
 * Created by Giacomo on 12/10/2017.
 */

public class Giorni {
    private String giorno;
    private String data;
    private String eventi;

    public Giorni(String giorno, String data, String eventi) {
        this.giorno = giorno;
        this.data = data;
        this.eventi = eventi;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEventi() {
        return eventi;
    }

    public void setEventi(String eventi) {
        this.eventi = eventi;
    }
}
