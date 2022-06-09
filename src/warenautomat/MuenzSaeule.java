package warenautomat;

public class MuenzSaeule {
    private static final int MAX_KAPAZITAET = 100;
    private int mMuenzTyp;
    private int mZaehler = 0;
    private int mGuthabenZaehler;
    private int mWechselGeldZaehler;

    public MuenzSaeule(int lMuenzBestand, int lMuenzTyp){
        this.mMuenzTyp = lMuenzTyp;
        this.mZaehler = lMuenzBestand;
    }
}
