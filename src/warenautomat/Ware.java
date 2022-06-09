package warenautomat;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class Ware {
    public static double mReduktionsFaktor;
    public static double mRundungsGenauigkeit;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private String mName;
    private int mPreis;
    private LocalDate mDate;
    

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
        return this.mPreis * (int) Math.round(mReduktionsFaktor * 100);
        
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

    public static void main(String[] args) {
        String datum = "03.04.2007";
        String name = "Mars";
        int preis = 2;
        Ware lWare = new Ware(name, preis, LocalDate.parse(datum, FORMATTER));
        boolean result = lWare.isHaltbarkeitUeberschritten();
        System.out.println(LocalDate.parse(datum, FORMATTER));
        System.out.println( result);
        System.out.println(SystemSoftware.gibAktuellesDatum());
        // double test = Ware.mReduktionsFaktor;

    }

}
