package zi.dpapp.models;

/**
 * Created by giacomo.zancan on 27/12/2017.
 */

public class Evento {
    private String canale;
    private String ora;

    public Evento(String canale, String ora) {
        this.canale = canale;
        this.ora = ora;
    }

    public String getCanale() {
        return canale;
    }

    public void setCanale(String canale) {
        this.canale = canale;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }
}
