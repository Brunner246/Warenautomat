package warenautomat;
// import java.time.LocalDate;

public class Fach {
    
    private Ware mWare; // [0..1    -   0..1]
    private Drehteller mDrehteller;

    public Fach(Drehteller aDrehteller){
        this.mDrehteller = aDrehteller;
    }

    public void setWare(Ware aWare){
        this.mWare = aWare;
    }

    public Ware getWare(){
        return this.mWare;
    }

    public boolean istLeer(){
        if (this.mWare != null){
            return false;
        }
        else{
            return true;
        }
    }

    public void entferneWare(){
        this.mWare = null;
    }

    public Drehteller getDrehteller(){
        return this.mDrehteller;
    }

// public static void main(String[] args) {

//     Fach lFach = new Fach();
//     String datum = "03.04.2007";
//     String name = "Mars";
//     int preis = 250;
//     Ware lWare = new Ware(name, preis, LocalDate.parse(datum, Ware.FORMATTER));
//     lFach.setWare(lWare);
    
//     System.out.println(lFach.istLeer());
//     boolean result = lWare.isHaltbarkeitUeberschritten();
//     System.out.println(LocalDate.parse(datum, Ware.FORMATTER));
//     System.out.println( result);
//     System.out.println(SystemSoftware.gibAktuellesDatum());
//     // double test = Ware.mReduktionsFaktor;
//     System.out.println(lWare.getPreis());

//     System.out.println(lFach.mWare.getDate());
//     }
}