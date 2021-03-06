package warenautomat;

import java.util.Arrays;
// import warenautomat.SystemSoftware;

/**
 * Die Kasse verwaltet das eingenommene Geld sowie das Wechselgeld. <br>
 * Die Kasse hat fünf Münz-Säulen für: <br>
 * - 10 Rappen <br>
 * - 20 Rappen <br>
 * - 50 Rappen <br>
 * - 1 Franken <br>
 * - 2 Franken <br>
 */
public class Kasse {
    public static final int ANZAHL_MUENZSAEULEN = 5;
    public static final int[] MUENZ_TYPEN = {10, 20, 50, 100, 200};
    private MuenzSaeule[] mMuenzsaeulen;
    private int mIndexAktuelleMuenzsaeule;
    private int mGuthabenKunde = 0;
    private int mWechselgeld = 0;
    private Automat mAutomat;
    private int mPreisTotalVerkaufteWare = 0;

  /**
   * Standard-Konstruktor. <br>
   * Führt die nötigen Initialisierungen durch.
   */
  public Kasse(Automat aAutomat) {
    mMuenzsaeulenErstellen(2);
    this.mAutomat = aAutomat;
  }

  private void mMuenzsaeulenErstellen(int aMuenzBestand){
    this.mMuenzsaeulen = new MuenzSaeule[ANZAHL_MUENZSAEULEN];
    for (int il = 0; il < ANZAHL_MUENZSAEULEN; il++){
        this.mMuenzsaeulen[il] = new MuenzSaeule(aMuenzBestand, MUENZ_TYPEN[il], this);
    }
  }

  public void anzeigenSetzen(){
    for (int il = 0; il < ANZAHL_MUENZSAEULEN; il++){
        this.mMuenzsaeulen[il].neueMuenzenHinzufügen();
    }
  }

  public static boolean contains(final int[] arr, final int key) {
    return Arrays.stream(arr).anyMatch(i -> i == key);
  }

  /**
   * Diese Methode wird aufgerufen nachdem das Personal beim Verwalten des
   * Wechselgeldbestand die Münzart und die Anzahl der Münzen über die
   * Tastatur eingegeben hat 
   * (siehe Use-Case "Wechselgeldbestand (Münzbestand) verwalten").
   * 
   * @param pMuenzenBetrag Der Betrag der Münzart in Franken.
   * @param pAnzahl Die Anzahl der Münzen. Bei der Entnahme von Münzen als
   *                entsprechender negativer Wert.
   * @return Anzahl der Münzen welche hinzugefügt resp. entnommen werden (bei
   *         Entnahme als negativer Wert). <br>
   *         Im Normalfall entspricht dieser Wert dem Übergabeparameter 
   *         <code>pAnzahl</code>. <br> 
   *         Er kann kleiner sein falls beim Hinzufügen in der Münzsäule zu 
   *         wenig Platz vorhanden ist oder wenn bei der Entnahme ein grössere 
   *         Anzahl angegeben wurde als tatsächlich in der Münzsäule vorhanden 
   *         ist. <br>
   *         Wenn ein nicht unterstützter Münzbetrag übergeben wurde: -200
   */
  public int verwalteMuenzbestand(double aMuenzenBetrag, int aAnzahl) {
    int lMuenzBetrag = UnitKonverter.konvertiereInGanzzahl(aMuenzenBetrag);
    boolean lVal = contains(MUENZ_TYPEN, lMuenzBetrag);
      if (!lVal){
        return -200;
      }
      for (int il = 0; il < MUENZ_TYPEN.length; il++){
        if (MUENZ_TYPEN[il] == lMuenzBetrag){
          if (aAnzahl >= 0){
          if (mMuenzsaeulen[il].freierPlatz() > 0){
            
              if (aAnzahl <= mMuenzsaeulen[il].freierPlatz()){
                mMuenzsaeulen[il].neueMuenzen(aAnzahl, true);
                return aAnzahl;
              }
            }
          }
            else{
              // aAnzahl -= mMuenzsaeulen[il].freierPlatz();
              mMuenzsaeulen[il].neueMuenzen(aAnzahl, true);
              return aAnzahl;
            }
          }
        }
      System.out.println("kein Platz vorhanden");
    return 0;
  }

  /**
   * Diese Methode wird aufgerufen nachdem das Personal beim Geldauffüllen den
   * Knopf "Bestätigen" gedrückt hat
   * (siehe Use-Case "Wechselgeldbestand (Münzbestand) verwalten"). <br>
   * Verbucht die Münzen gemäss dem vorangegangenen Aufruf der Methode 
   * <code>verwalteMuenzbestand()</code>.
   */
  public void verwalteMuenzbestandBestaetigung() {
    this.mMuenzsaeulen[this.mIndexAktuelleMuenzsaeule].neueMuenzenHinzufügen();
  }
 
  /**
   * Diese Methode wird aufgerufen wenn ein Kunde eine Münze eingeworfen hat. <br>
   * Führt den eingenommenen Betrag entsprechend nach. <br>
   * Stellt den nach dem Einwerfen vorhandenen Betrag im Kassen-Display dar. <br>
   * Eingenommenes Geld steht sofort als Wechselgeld zur Verfügung. <br>
   * Die Münzen werden von der Hardware-Kasse auf Falschgeld, Fremdwährung und
   * nicht unterstützte Münzarten geprüft, d.h. diese Methode wird nur
   * aufgerufen wenn ein Münzeinwurf soweit erfolgreich war. <br>
   * Ist die Münzsäule voll (d.h. 100 Münzen waren vor dem Einwurf bereits darin
   * enthalten), so wird mittels
   * <code> SystemSoftware.auswerfenWechselGeld() </code> unmittelbar ein
   * entsprechender Münz-Auswurf ausgeführt. <br>
   * Hinweis: eine Hardware-Münzsäule hat jeweils effektiv Platz für 101 Münzen.
   * 
   * @param pMuenzenBetrag Der Betrag der neu eingeworfenen Münze in Franken.
   * @return <code> true </code>, wenn er Einwurf erfolgreich war. <br>
   *         <code> false </code>, wenn Münzsäule bereits voll war.
   */
  public boolean einnehmen(double pMuenzenBetrag) {
    int lBetragRappen = UnitKonverter.konvertiereInGanzzahl(pMuenzenBetrag);
    for (int il = 0; il < MUENZ_TYPEN.length; il++){
      if (MUENZ_TYPEN[il] == lBetragRappen){
        if (mMuenzsaeulen[il].freierPlatz() <= 0){
          System.out.println("Service Techniker aufbieten um Münzsäulen zu leeren");
          SystemSoftware.auswerfenWechselGeld(pMuenzenBetrag);
          return false;
        }
        mMuenzsaeulen[il].neueMuenzen(1, false);
      }
    }
    this.mGuthabenKunde += lBetragRappen;
    this.mWechselgeld = this.mGuthabenKunde;
    SystemSoftware.zeigeBetragAn(UnitKonverter.konvertiereInDouble(this.mWechselgeld));
    return true;
  }

  public MuenzSaeule getMuenzSaeule(int aIndex){
    return mMuenzsaeulen[aIndex];
  }

  public int getGuthabenKunde(){
    return this.mGuthabenKunde;
  }

  public Automat getAutomat(){
    return this.mAutomat;
  }

  /**
   * Bewirkt den Auswurf des Restbetrages.
   */
  public void gibWechselGeld() {
    SystemSoftware.auswerfenWechselGeld(UnitKonverter.konvertiereInDouble(this.mGuthabenKunde));

    while (this.mGuthabenKunde != 0){
      if (this.mGuthabenKunde >= 200){
        mMuenzsaeulen[4].getZaehlerRueckgeld();
        this.mGuthabenKunde -= 200;
      }
      else if (this.mGuthabenKunde >= 100){
        mMuenzsaeulen[3].getZaehlerRueckgeld();
        this.mGuthabenKunde -= 100;
      }
      else if (this.mGuthabenKunde >= 50){
        mMuenzsaeulen[2].getZaehlerRueckgeld();
        this.mGuthabenKunde -= 50;
      }
      else if (this.mGuthabenKunde >= 20){
        mMuenzsaeulen[1].getZaehlerRueckgeld();
        this.mGuthabenKunde -= 20;
      }
      else if (this.mGuthabenKunde >= 10){
        mMuenzsaeulen[0].getZaehlerRueckgeld();
        this.mGuthabenKunde -= 10;
      }
      else{
        return;
      }
    }
    SystemSoftware.zeigeBetragAn(0.0);
  }

  public void kaufAusfuehren(Ware aWare){
    this.mGuthabenKunde -= aWare.getPreis();
    this.mPreisTotalVerkaufteWare += aWare.getPreis();
    SystemSoftware.zeigeBetragAn(UnitKonverter.konvertiereInDouble(this.mGuthabenKunde));
  }

  /**
   * Gibt den Gesamtbetrag der bisher verkauften Waren zurück. <br>
   * Analyse: Abgeleitetes Attribut.
   * 
   * @return Gesamtbetrag der bisher verkauften Waren.
   */
  public double gibBetragVerkaufteWaren() {
    return UnitKonverter.konvertiereInDouble( this.mPreisTotalVerkaufteWare);
  }
}
