package warenautomat;

public class MuenzSaeule {
    private static final int MAX_KAPAZITAET = 100;
    private int mMuenzTyp = 0;
    private int mZaehler = 0; // vorhandener Münzbestand
    private int mZaehlerRueckgeld = 0;
    private Kasse mKasse;

    public MuenzSaeule(int lMuenzBestand, int lMuenzTyp, Kasse aKasse){
        this.mMuenzTyp = lMuenzTyp;
        this.mZaehler = lMuenzBestand;
        this.mKasse = aKasse;
    }

    // public void setzeMuenzBetrag(int aBetrag){
    //     if ((MAX_KAPAZITAET - this.mZaehler) >= aBetrag){
    //         this.mZaehler += aBetrag;
    //     }
    //     else{
    //         System.out.println("Münzsaule ist voll");
    //     }
    // }

    public void neueMuenzenHinzufügen(){
        double lMuenzTyp = HelperClasses.konvertiereInDouble(this.mMuenzTyp);
        SystemSoftware.zeigeMuenzenInGui(lMuenzTyp, this.mZaehler); // HelperClasses.konvertiereInDouble(this.mMuenzTyp * this.mZaehler)
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
        double lMuenzTyp = HelperClasses.konvertiereInDouble(this.mMuenzTyp);
        
        if (this.mZaehler > 0){
            // this.mZaehler --;
            SystemSoftware.zeigeMuenzenInGui(lMuenzTyp, this.mZaehler);
            this.mZaehlerRueckgeld --;
        }
        return this.mZaehlerRueckgeld;
    }

    public int getBetrag(){
        return this.mZaehler * this.mMuenzTyp;
    }

    public boolean neueMuenzen(int aAnzahlMuenzen){
        if (aAnzahlMuenzen < 0){
            if (this.mZaehler > 0){
                this.mZaehler -= 1;
                if (this.mZaehler < 0){
                    this.mZaehler = 0;
                }
            }
            //throw new RuntimeException("negative Anzahl Münzen ist nicht erlaubt");
            return false;
        }
        if ((MAX_KAPAZITAET - this.mZaehler) <= aAnzahlMuenzen){
            System.out.println("Anzahl Münzen überschreiten vorhandene Kapazität");
            return false;
        }
        this.mZaehler += aAnzahlMuenzen;
        this.mZaehlerRueckgeld += this.mZaehler;
        neueMuenzenHinzufügen();
        return true;
    }

    public int freierPlatz(){
        return MAX_KAPAZITAET - this.mZaehler;
    }
}
