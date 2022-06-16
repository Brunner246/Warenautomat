package warenautomat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ware {
    public static final double REDUKTIONS_FAKTOR = .25; // check reduction factor -> gem. Martin 25% vom ursprünglichen Wert
    public static final double RUNDUNGS_GENAUIGKEIT = 0.10;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private String mName;
    private int mPreis;
    private LocalDate mDate;
    private Kauf mKauf;
    
    /** 
     * @param aName Name der Ware
     * @param aPreis Preis in Rappen !!
     * @param aDate Haltbarkeitsdatum
     */
    public Ware(String aName, int aPreis, LocalDate aDate){
        this.mName = aName;
        this.mPreis = aPreis;
        this.mDate = aDate;
    }

    public String getName(){
        return this.mName;
    }

    public int getPreis(){
        if (isHaltbarkeitUeberschritten() == false){
            return this.mPreis;
        }
        double lReduktion = HelperClasses.konvertiereInDouble(this.mPreis);
        System.out.print("Preis vor Reduktion : " + String.format("%.2f", lReduktion));
        lReduktion *= REDUKTIONS_FAKTOR;
        
        lReduktion = HelperClasses.rundeAufZehner(lReduktion, RUNDUNGS_GENAUIGKEIT);
        int lPreisReduziert = HelperClasses.konvertiereInGanzzahl(lReduktion);
        System.out.println(" Preis für reduzierte Ware " + String.format("%.2f", lReduktion));
        return lPreisReduziert;  
    }

    public LocalDate getDate(){
        return this.mDate;
    }

    public boolean isHaltbarkeitUeberschritten(){
        if (SystemSoftware.gibAktuellesDatum().isAfter(this.mDate)){
            return true;
        }
        else{
            return false;
        }
    }

    public int getZustandDisplay(){
        if (!isHaltbarkeitUeberschritten()){
            return 1;
        }
        else if (isHaltbarkeitUeberschritten()){
            return 2;
        }
    return 0;
    }

    public void setKauf(Kauf aKauf){
        this.mKauf = aKauf;
    }

    public Kauf getKauf(){
        return this.mKauf;
    }

    public void setPreis(int aPreis){
        this.mPreis = aPreis;
    }

}
