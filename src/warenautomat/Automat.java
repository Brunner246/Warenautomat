package warenautomat;

import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Der Automat besteht aus 7 Drehtellern welche wiederum je aus 16 Fächer
 * bestehen. <br>
 * Der erste Drehteller und das jeweils erste Fach haben jeweils die Nummer 1
 * (nicht 0!). <br>
 * Im Weitern hat der Automat eine Kasse. Diese wird vom Automaten instanziert.
 */
public class Automat {
  
  private static final int NR_DREHTELLER = 7;
  private Drehteller[] drehteller;
  private Kasse kasse;
  private ArrayList<Kauf> mVerkaufteWare = new ArrayList<Kauf>();
  private double mTotalerWarenWert = 0.;
  /**
   * Der Standard-Konstruktor. <br>
   * Führt die nötigen Initialisierungen durch (u.a. wird darin die Kasse
   * instanziert).
   */
  public Automat() {
		this.kasse = new Kasse(this);
    this.drehteller = new Drehteller[7];
    for (int il = 0; il < NR_DREHTELLER; il++){
			int lDrehtellerNr = il + 1;
      this.drehteller[il] = new Drehteller(lDrehtellerNr, this);
    }
  }

  /**
   * Füllt ein Fach mit Ware. <br>
   * Wenn das Service-Personal den Automaten füllt, wird mit einem
   * Bar-Code-Leser zuerst die Ware gescannt. <br>
   * Daraufhin wird die Schiebe-Tür geöffnet. <br>
   * Das Service-Personal legt die neue Ware ins Fach und schliesst das Fach. <br>
   * Die Hardware resp. System-Software ruft die Methode
   * <code> Automat.neueWareVonBarcodeLeser() </code> auf.
   * 
   * @param pDrehtellerNr Der Drehteller bei welchem das Fach hinter der
   *          Schiebe-Türe gefüllt wird. <br>
   *          Nummerierung beginnt mit 1 (nicht 0)!
   * @param pWarenName Der Name der neuen Ware.
   * @param pPreis Der Preis der neuen Ware.
   * @param pVerfallsDatum Das Verfallsdatum der neuen Ware.
   */
  public void neueWareVonBarcodeLeser(int pDrehtellerNr, String pWarenName, double pPreis, LocalDate pVerfallsDatum) {
		SystemSoftware.zeigeWarenPreisAn(pDrehtellerNr, pPreis);
		Drehteller lDrehteller = getDrehteller(pDrehtellerNr);
    // prüfen, ob Fach leer ist
    if (this.getDrehteller(pDrehtellerNr).getAktuellesFach().istLeer()){
			lDrehteller.setAktuellesFach(lDrehteller.getFachNr());
    }
		int lPreis = HelperClasses.konvertiereInGanzzahl(pPreis);
    Ware lNeueWare = new Ware(pWarenName, lPreis, pVerfallsDatum);
    SystemSoftware.zeigeWareInGui(pDrehtellerNr, pWarenName, pVerfallsDatum);
		lDrehteller.fuelleFach(lNeueWare);
    lPreis = lNeueWare.getPreis();
    mTotalerWarenWert += HelperClasses.konvertiereInDouble(lPreis);
    kasse.anzeigenSetzen(); // zeige Display
		return;
  }

  /**
   * Gibt die Objekt-Referenz auf die <em> Kasse </em> zurück.
   */
  public Kasse gibKasse() {
    return kasse;
  }

  public Drehteller getDrehteller(int aDrehtellerNr){
    aDrehtellerNr --; // um eins reduzieren
    return this.drehteller[aDrehtellerNr];
  }

  /**
   * Wird von der System-Software jedesmal aufgerufen wenn der gelbe Dreh-Knopf
   * gedrückt wird. <br>
   * Die Applikations-Software führt die Drehteller-Anzeigen nach (Warenpreis,
   * Verfallsdatum). <br>
   * Das Ansteuern des Drehteller-Motors übernimmt die System-Software (muss
   * nicht von der Applikations-Software gesteuert werden.). <br>
   * Die System-Software stellt sicher, dass <em> drehen </em> nicht durchgeführt wird
   * wenn ein Fach offen ist.
   */
  public void drehen() {
    SystemSoftware.dreheWarenInGui();
		for (int il = 0; il < NR_DREHTELLER; il++){
			int lDrehtellerNr = il + 1;
			Drehteller lDrehteller = getDrehteller(lDrehtellerNr);
			lDrehteller.drehen();
			Ware lWare = lDrehteller.getAktuellesFach().getWare();
			if (lWare == null){
				SystemSoftware.zeigeWarenPreisAn(lDrehtellerNr, 0.);
				SystemSoftware.zeigeVerfallsDatum(lDrehtellerNr, 0);
			}
			else{
				SystemSoftware.zeigeWarenPreisAn(lDrehtellerNr, HelperClasses.konvertiereInDouble(lWare.getPreis()));
				SystemSoftware.zeigeVerfallsDatum(lDrehtellerNr, lWare.getZustandDisplay());
        if (lWare.getPreis() > kasse.getGuthabenKunde()){
          SystemSoftware.zeigeZuWenigGeldAn();
        }
			}
		}
  }

  /**
   * Beim Versuch eine Schiebetüre zu öffnen ruft die System-Software die
   * Methode <code> oeffnen() </code> der Klasse <em> Automat </em> mit der
   * Drehteller-Nummer als Parameter auf. <br>
   * Es wird überprüft ob alles o.k. ist: <br>
   * - Fach nicht leer <br>
   * - Verfallsdatum noch nicht erreicht <br>
   * - genug Geld eingeworfen <br>
   * - genug Wechselgeld vorhanden <br>
   * Wenn nicht genug Geld eingeworfen wurde, wird dies mit
   * <code> SystemSoftware.zeigeZuWenigGeldAn() </code> signalisiert. <br>
   * Wenn nicht genug Wechselgeld vorhanden ist wird dies mit
   * <code> SystemSoftware.zeigeZuWenigWechselGeldAn() </code> signalisiert. <br>
   * Wenn o.k. wird entriegelt (<code> SystemSoftware.entriegeln() </code>) und
   * positives Resultat zurückgegeben, sonst negatives Resultat. <br>
   * Es wird von der System-Software sichergestellt, dass zu einem bestimmten
   * Zeitpunkt nur eine Schiebetüre offen sein kann.
   * 
   * @param pDrehtellerNr Der Drehteller bei welchem versucht wird die
   *          Schiebe-Türe zu öffnen. <br>
   *          Nummerierung beginnt mit 1 (nicht 0)!
   * @return Wenn alles o.k. <code> true </code>, sonst <code> false </code>.
   */
  public boolean oeffnen(int aDrehtellerNr) {
		Fach lFach = getDrehteller(aDrehtellerNr).getAktuellesFach();
    if (lFach.istLeer()){
			return false;
		}
		else if (lFach.getWare().isHaltbarkeitUeberschritten()){
			SystemSoftware.zeigeVerfallsDatum(aDrehtellerNr, 2);
			System.out.print("Preis wurde reduziert...\n");
		}
    if (kasse.getGuthabenKunde() < lFach.getWare().getPreis()){
      SystemSoftware.zeigeZuWenigGeldAn();
      return false;
    }
    int lRueckGeld = (kasse.getGuthabenKunde() - lFach.getWare().getPreis());
    if (lRueckGeld > 0){
      if (!pruefeRueckGeldMenge(lRueckGeld)){
        SystemSoftware.zeigeZuWenigWechselGeldAn();
        return false;
      }
    }
    kasse.kaufAusfuehren(lFach.getWare());
    Kauf lKauf = new Kauf(lFach.getWare());
    mVerkaufteWare.add(lKauf);
    SystemSoftware.entriegeln(aDrehtellerNr);
    lFach.setWare(null);
    SystemSoftware.zeigeVerfallsDatum(aDrehtellerNr, 0);

    return true; 
  }

  private boolean pruefeRueckGeldMenge(int aRueckgeld){
      int linMuenzSaeule10 = 0;
      int linMuenzSaeule20 = 0;
      int linMuenzSaeule50 = 0;
      int linMuenzSaeule100 = 0;
      int linMuenzSaeule200 = 0;
      for (int il = 0; il < Kasse.ANZAHL_MUENZSAEULEN; il++){
        if (kasse.getMuenzSaeule(il).getMuenzTyp() == 10){
          linMuenzSaeule10 += kasse.getMuenzSaeule(il).getAnzahlMuenzen() * kasse.getMuenzSaeule(il).getMuenzTyp();
        }
        else if (kasse.getMuenzSaeule(il).getMuenzTyp() == 20){
          linMuenzSaeule20 += kasse.getMuenzSaeule(il).getAnzahlMuenzen() * kasse.getMuenzSaeule(il).getMuenzTyp();
        }
        else if (kasse.getMuenzSaeule(il).getMuenzTyp() == 50){
          linMuenzSaeule50 += kasse.getMuenzSaeule(il).getAnzahlMuenzen() * kasse.getMuenzSaeule(il).getMuenzTyp();
        }
        else if (kasse.getMuenzSaeule(il).getMuenzTyp() == 100){
          linMuenzSaeule100 += kasse.getMuenzSaeule(il).getAnzahlMuenzen() * kasse.getMuenzSaeule(il).getMuenzTyp();
        }
        else if (kasse.getMuenzSaeule(il).getMuenzTyp() == 200){
          linMuenzSaeule200 += kasse.getMuenzSaeule(il).getAnzahlMuenzen() * kasse.getMuenzSaeule(il).getMuenzTyp();
        }
       
        }
      while (aRueckgeld != 0){
        if (aRueckgeld >= 200 && linMuenzSaeule200 != 0){
          kasse.getMuenzSaeule(4).neueMuenzen(-1, false);
          aRueckgeld -= 200;
          SystemSoftware.auswerfenWechselGeld(2.00);
        }
        else if (aRueckgeld >= 100 && linMuenzSaeule100 != 0){
          kasse.getMuenzSaeule(3).neueMuenzen(-1, false);
          aRueckgeld -= 100;
          SystemSoftware.auswerfenWechselGeld(1.00);
        }
        else if (aRueckgeld >= 50 && linMuenzSaeule50 != 0){
          kasse.getMuenzSaeule(2).neueMuenzen(-1, false);
          aRueckgeld -= 50;
          SystemSoftware.auswerfenWechselGeld(0.50);
        }
        else if (aRueckgeld >= 20 && linMuenzSaeule20 != 0){
          kasse.getMuenzSaeule(1).neueMuenzen(-1, false);
          aRueckgeld -= 20;
          SystemSoftware.auswerfenWechselGeld(0.20);
        }
        else if (aRueckgeld >= 10 && linMuenzSaeule10 != 0){
          kasse.getMuenzSaeule(0).neueMuenzen(-1, false);
          aRueckgeld -= 10;
          SystemSoftware.auswerfenWechselGeld(0.10);
        }
        else{
          return false;
        }
    }     
    return true;
  }

  /**
   * Gibt den aktuellen Wert aller im Automaten enthaltenen Waren in Franken
   * zurück. <br>
   * Analyse: <br>
   * Abgeleitetes Attribut. <br>
   * 
   * @return Der totale Warenwert des Automaten.
   */
  public double gibTotalenWarenWert() {
    return mTotalerWarenWert; 
    
  }

  /**
   * Gibt die Anzahl der verkauften Ware <em> pName </em> seit (>=)
   * <em> pDatum </em> zurück.
   * 
   * @param pName Der Name der Ware nach welcher gesucht werden soll.
   * @param pDatum Das Datum seit welchem gesucht werden soll.
   * @return Anzahl verkaufter Waren.
   */
  public int gibVerkaufsStatistik(String pName, LocalDate pDatum) {
    int lCounter = 0; //TODO
    for (int il = 0; il < mVerkaufteWare.size(); il++){
      Kauf lK = mVerkaufteWare.get(il);
      if (lK.getDate().isAfter(pDatum) && lK.getWare().getName().equals(pName)){
        lCounter++;
      }
    }
    return lCounter; 
  }
  
}
