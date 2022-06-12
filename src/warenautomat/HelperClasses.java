package warenautomat;

class HelperClasses {

    public static int konvertiere(double aMuenzbetrag){
        return (int) Math.round(aMuenzbetrag * 100);
    }    
}

class Muenztyp{
    public static enum Muenzen {
        MUENZE_10,
        MUENZE_20,
        MUENZE_50,
        MUENZE_100,
        MUENZE_200,
    }
}
