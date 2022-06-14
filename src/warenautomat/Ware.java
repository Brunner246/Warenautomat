package warenautomat;
import java.time.LocalDate;
// import java.util.Date;
import java.time.format.DateTimeFormatter;

public class Ware {
    public static final double REDUKTIONS_FAKTOR = 0.2; // TODO check reduction factor !!
    public static final double RUNDUNGS_GENAUIGKEIT = 2;
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
        System.out.println("Preis für reduzierte Ware");
        return this.mPreis - (int) (2 * Math.round((REDUKTIONS_FAKTOR * this.mPreis) / 2));
        
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
        return 2;
    }

    public void setKauf(Kauf aKauf){
        this.mKauf = aKauf;
    }

    public Kauf getKauf(){
        return this.mKauf;
    }

    // public static void main(String[] args) {
    //     String datum = "03.04.2007";
    //     String name = "Mars";
    //     int preis = 250;
    //     Ware lWare = new Ware(name, preis, LocalDate.parse(datum, FORMATTER));
    //     boolean result = lWare.isHaltbarkeitUeberschritten();
    //     System.out.println(LocalDate.parse(datum, FORMATTER));
    //     System.out.println( result);
    //     System.out.println(SystemSoftware.gibAktuellesDatum());
    //     // double test = Ware.mReduktionsFaktor;
    //     System.out.println(lWare.getPreis());
    // }

}
