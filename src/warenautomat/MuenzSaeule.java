package warenautomat;

public class MuenzSaeule {
    /*
     * Münztypen
     * 0 = 10
     * 1 = 20
     * 2 = 50
     * 3 = 100
     * 4 = 200
     */
    private static final int MAX_KAPAZITAET = 100;
    private int mMuenzTyp;
    private int mZaehler = 0; // vorhandener Münzbestand
    // private int mAnzahl; // 
    private int mGuthaben;
    private int mWechselgeld;
    private int mFreieKapazitaet;

    public MuenzSaeule(int lMuenzBestand, int lMuenzTyp){
        this.mMuenzTyp = lMuenzTyp;
        this.mZaehler = lMuenzBestand;
        this.mFreieKapazitaet = MAX_KAPAZITAET - lMuenzBestand;
    }

    public boolean neueMuenzen(int aAnzahlMuenzen){
        if (aAnzahlMuenzen < 0){
            System.out.println("Wert darf nicht < 0 sein");
            //throw new RuntimeException("negative Anzahl Münzen ist nicht erlaubt");
            return false;
        }
        if (this.mFreieKapazitaet < aAnzahlMuenzen){
            System.out.println("Anzahl Münzen überschreiten vorhandene Kapazität");
            return false;
        }
        this.mZaehler += aAnzahlMuenzen;
        this.mFreieKapazitaet = MAX_KAPAZITAET - this.mZaehler;
        SystemSoftware.zeigeMuenzenInGui(this.mMuenzTyp/100, aAnzahlMuenzen);
        System.out.println(aAnzahlMuenzen + "Münzen hinzugefügt");
        return true;
    }

    public void setGuthaben(int aGuthaben){
        this.mGuthaben = aGuthaben;
        this.mWechselgeld = aGuthaben; // solange Kauf nicht bestätigt, Guthaben bereit halten
    }

    public int getGuthaben(){
        SystemSoftware.zeigeMuenzenInGui(this.mMuenzTyp/100, this.mZaehler);
        return this.mGuthaben;
    }

    public void setWechselgeld(int aWechselGeld){
        this.mWechselgeld = aWechselGeld;
    }

    public int getWechselgeld(){
        this.mFreieKapazitaet = MAX_KAPAZITAET - this.mWechselgeld;
        if (this.mWechselgeld < mGuthaben){
            System.out.println("kein Wechselgeld vorhanden !");
            return 0;
        }
        return this.mWechselgeld;
    }

    public int getMuenzTyp(){
        return this.mMuenzTyp;
    }

    public int getZaehler(){
        return this.mZaehler;
    }
}
