package warenautomat;

import java.util.Arrays;
import warenautomat.SystemSoftware;

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
    private int mGuthaben;
    private int mWechselgeld;



  /**
   * Standard-Konstruktor. <br>
   * Führt die nötigen Initialisierungen durch.
   */
  public Kasse() {
    mMuenzsaeulenErstellen(100); // jeder muenzBestand wird mit 100 erstellt
    // TODO
    
  }

  private void mMuenzsaeulenErstellen(int aMuenzBestand){
    this.mMuenzsaeulen = new MuenzSaeule[ANZAHL_MUENZSAEULEN];
    for (int il = 0; il < ANZAHL_MUENZSAEULEN; il++){
        this.mMuenzsaeulen[il] = new MuenzSaeule(aMuenzBestand, MUENZ_TYPEN[il]);
        // SystemSoftware.zeigeMuenzenInGui(MUENZ_TYPEN[il] / 100., aMuenzBestand);
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
    int lMuenzBetrag = HelperClasses.konvertiereInGanzzahl(aMuenzenBetrag);
    boolean lVal = contains(MUENZ_TYPEN, lMuenzBetrag);
      if (!lVal){
        return -200;
      }
    if (einnehmen(aMuenzenBetrag)){
      SystemSoftware.zeigeMuenzenInGui(aMuenzenBetrag, this.mMuenzsaeulen[0].getZaehler());
      return aAnzahl;
    }
    else if (!einnehmen(aMuenzenBetrag)){
      return this.mMuenzsaeulen[this.mIndexAktuelleMuenzsaeule].getFreieKapazitaet();
    }
    else{
      return 0;
    }
  }

  /**
   * Diese Methode wird aufgerufen nachdem das Personal beim Geldauffüllen den
   * Knopf "Bestätigen" gedrückt hat
   * (siehe Use-Case "Wechselgeldbestand (Münzbestand) verwalten"). <br>
   * Verbucht die Münzen gemäss dem vorangegangenen Aufruf der Methode 
   * <code>verwalteMuenzbestand()</code>.
   */
  public void verwalteMuenzbestandBestaetigung() {
    this.mMuenzsaeulen[this.mIndexAktuelleMuenzsaeule].neueMuenzenVerbuchen();
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
    int lBetragRappen = HelperClasses.konvertiereInGanzzahl(pMuenzenBetrag);
    
    if (lBetragRappen % 10 == 0){
        if (this.mMuenzsaeulen[0].neueMuenzen(lBetragRappen / 10)){
          // SystemSoftware.zeigeMuenzenInGui(pMuenzenBetrag, this.mMuenzsaeulen[0].getZaehler());
          this.mIndexAktuelleMuenzsaeule = 0;
          this.mGuthaben += lBetragRappen;
          this.mWechselgeld += this.mGuthaben;
          SystemSoftware.zeigeBetragAn(this.mGuthaben / 100.);
          return true;
        }
    } 
    else if (lBetragRappen % 20 == 0){
        if(this.mMuenzsaeulen[1].neueMuenzen(lBetragRappen / 20)){
          this.mIndexAktuelleMuenzsaeule = 1;
          this.mGuthaben += lBetragRappen;
          this.mWechselgeld += this.mGuthaben;
          SystemSoftware.zeigeBetragAn(this.mGuthaben / 100.);
          return true;
        }
    }
    else if (lBetragRappen % 50 == 0){
        if (this.mMuenzsaeulen[2].neueMuenzen(lBetragRappen / 50)){
          this.mIndexAktuelleMuenzsaeule = 2;
          this.mGuthaben += lBetragRappen;
          this.mWechselgeld += this.mGuthaben;
          SystemSoftware.zeigeBetragAn(this.mGuthaben / 100.);
          return true;
        }
    }
    else if (lBetragRappen % 100 == 0){
        if (this.mMuenzsaeulen[3].neueMuenzen(lBetragRappen / 100)){
          this.mIndexAktuelleMuenzsaeule = 3;
          this.mGuthaben += lBetragRappen;
          this.mWechselgeld += this.mGuthaben;
          SystemSoftware.zeigeBetragAn(this.mGuthaben / 100.);
          return true;
        }
    }
    else if (lBetragRappen % 200 == 0){
        if (this.mMuenzsaeulen[4].neueMuenzen(lBetragRappen / 200)){
          this.mIndexAktuelleMuenzsaeule = 4;
          this.mGuthaben += lBetragRappen;
          this.mWechselgeld += this.mGuthaben;
          SystemSoftware.zeigeBetragAn(this.mGuthaben / 100.);
          return true;
        }    
    }
        return false;
  }

  /**
   * Bewirkt den Auswurf des Restbetrages.
   */
  public void gibWechselGeld() {
    SystemSoftware.auswerfenWechselGeld(HelperClasses.konvertiereInDouble(this.mWechselgeld));
    // TODO
    
  }

  public boolean kaufAusfuehren(Ware aWare){
    int lGeldwechsel =  this.mGuthaben - aWare.getPreis();
    if (lGeldwechsel < 0){
      System.out.println("zu wenig geld vorhanden !");
      return false;
    }
    else{
      this.mWechselgeld = lGeldwechsel;
      return true;
    }
  }

  /**
   * Gibt den Gesamtbetrag der bisher verkauften Waren zurück. <br>
   * Analyse: Abgeleitetes Attribut.
   * 
   * @return Gesamtbetrag der bisher verkauften Waren.
   */
  public double gibBetragVerkaufteWaren() {
    
    return 0.0; // TODO
    
  }

}
