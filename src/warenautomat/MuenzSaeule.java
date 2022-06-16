package warenautomat;

public class MuenzSaeule {
    private static final int MAX_KAPAZITAET = 101;

    private int mMuenzTyp = 0;
    private int mZaehler = 0;
    private int mZaehlerRueckgeld = 0;
    private Kasse mKasse;

    public MuenzSaeule(int lMuenzBestand, int lMuenzTyp, Kasse aKasse){
        this.mMuenzTyp = lMuenzTyp;
        this.mZaehler = lMuenzBestand;
        this.mKasse = aKasse;
    }

    public void neueMuenzenHinzufügen(){
        double lMuenzTyp = UnitKonverter.konvertiereInDouble(this.mMuenzTyp);
        SystemSoftware.zeigeMuenzenInGui(lMuenzTyp, this.mZaehler);
    }

    public void auswerfen(int aAnzahl){
        if (aAnzahl < this.mZaehler){
            this.mZaehler -= aAnzahl;   
        }
    }

    public int getMuenzTyp(){
        return this.mMuenzTyp;
    }

    public int getAnzahlMuenzen(){
        return this.mZaehler;
    }

    public int getZaehlerRueckgeld(){
        double lMuenzTyp = UnitKonverter.konvertiereInDouble(this.mMuenzTyp);
        
        if (this.mZaehler > 0){
            // this.mZaehler --;
            
            this.mZaehlerRueckgeld --;
            this.mZaehler --;
            SystemSoftware.zeigeMuenzenInGui(lMuenzTyp, this.mZaehler);
        }
        return this.mZaehlerRueckgeld;
    }

    public int getBetrag(){
        return this.mZaehler * this.mMuenzTyp;
    }

    public boolean neueMuenzen(int aAnzahlMuenzen, boolean aServiceMode){
        if (aAnzahlMuenzen < 0){
            if (this.mZaehler > 0){
                if (aServiceMode){
                    this.mZaehler --; 
                }
                neueMuenzenHinzufügen();
                if (this.mZaehler < 0){
                    this.mZaehler = 0;
                }
            }
            return false;
        }
        if ((MAX_KAPAZITAET - this.mZaehler) <= aAnzahlMuenzen){
            System.out.println("Anzahl Münzen überschreiten vorhandene Kapazität");
            return false;
        }
        this.mZaehler += aAnzahlMuenzen;
        this.mZaehlerRueckgeld += aAnzahlMuenzen;
        neueMuenzenHinzufügen();
        return true;
    }

    public int freierPlatz(){
        return MAX_KAPAZITAET - this.mZaehler;
    }

    public Kasse getKasse(){
        return this.mKasse;
    }
}
