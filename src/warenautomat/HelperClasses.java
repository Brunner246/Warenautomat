package warenautomat;

class UnitKonverter {

    public static int konvertiereInGanzzahl(double aMuenzbetrag){
        return (int) Math.round(aMuenzbetrag * 100);
    }    
    public static double konvertiereInDouble(int aBetrag){
        double lBetrag = aBetrag / 100.;
        return  lBetrag;
    }
    public static double rundeAufZehner(double aValue, double aRoundingScale) {
        return aRoundingScale * Math.round(aValue / aRoundingScale);
      }
}

// class Muenztyp{
//     public static enum Muenzen {
//         MUENZE_10,
//         MUENZE_20,
//         MUENZE_50,
//         MUENZE_100,
//         MUENZE_200,
//     }
// }
