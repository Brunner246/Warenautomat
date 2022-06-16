package warenautomat;

public class BestellungsKonfiguration {

    private String mWarenName;
    private int mGrenze;
    private int mAnzahlBestellung;

    public BestellungsKonfiguration(String aWarenName, int aGrenze, int aAnzahlBestellung){
        this.mWarenName = aWarenName;
        this.mGrenze = aGrenze;
        this.mAnzahlBestellung = aAnzahlBestellung;
    }

    public String getWarenName(){
        return this.mWarenName;
    }

    public int getGrenze(){
        return this.mGrenze;
    }

    public int getAnzahlBestellung(){
        return this.mAnzahlBestellung;
    }
}
