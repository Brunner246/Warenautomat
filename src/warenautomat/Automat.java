package warenautomat;

import java.time.LocalDate;


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

  /**
   * Der Standard-Konstruktor. <br>
   * Führt die nötigen Initialisierungen durch (u.a. wird darin die Kasse
   * instanziert).
   */
  public Automat() {
		this.kasse = new Kasse();
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
		SystemSoftware.zeigeWareInGui(pDrehtellerNr, pWarenName, pVerfallsDatum);
		System.out.println(pPreis + "test");
		

		Drehteller lDrehteller = getDrehteller(pDrehtellerNr);
		
    // prüfen, ob Fach leer ist
    if (this.getDrehteller(pDrehtellerNr).getAktuellesFach().istLeer()){
			lDrehteller.setAktuellesFach(lDrehteller.getFachNr());

    }
		int lPreis = HelperClasses.konvertiereInGanzzahl(pPreis);
    Ware lNeueWare = new Ware(pWarenName, lPreis, pVerfallsDatum);

		lDrehteller.fuelleFach(lNeueWare);
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
		/*
		 * if (kasse.gibZurZeitEingenommen() < ware.getPrice()) {
				SystemSoftware.zeigeZuWenigGeldAn();
				return false;
			}

			if (!kasse.hatGenugWechselgeld(ware.getPrice())) {
				SystemSoftware.zeigeZuWenigWechselGeldAn();
				return false;
			}
		 */

		System.out.println("code fehlt...");
    return false;  // TODO
    
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
    
    return 0.0; // TODO
    
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
    
    return 0; // TODO
    
  }
  
}
