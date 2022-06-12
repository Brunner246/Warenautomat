package warenautomat;

public class MuenzKonverter {

    public static int konvertiere(double aMuenzbetrag){
        return (int) Math.round(aMuenzbetrag * 100);
    }    
}
