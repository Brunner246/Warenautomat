
package warenautomat;

public class Drehteller {

  public static final int ANZAHL_FAECHER = 16;

  private Fach[] mFaecher; // [16    -   0..1]
  private boolean mIstOffen;
  private int mFachNr;
  private int mDrehtellerNr;
  private Automat mAutomat;
  
  public Drehteller(int aDrehtellerNr, Automat aAutomat){
    this.mAutomat = aAutomat;
    this.mFaecher = new Fach[ANZAHL_FAECHER];
    for (int il = 0; il < mFaecher.length; il++){
      this.mFaecher[il] = new Fach(this);
    }
    this.mDrehtellerNr = aDrehtellerNr;
  }

  public void drehen(){
    this.mFachNr ++;
    if (this.mFachNr >= ANZAHL_FAECHER){
      this.mFachNr = 0;
    }
  }

  public int getFachNr(){
    return this.mFachNr;
  }

  public void setAktuellesFach(int aFachNr){
    this.mFachNr = aFachNr;
  }

  public Fach getAktuellesFach(){
    // int lFachNr = mFachNr--;
    return mFaecher[mFachNr];
  }

  public void oeffnen(){
    Fach lFach = getAktuellesFach();
    if (lFach != null){
      this.mIstOffen = true;
    }
    this.mIstOffen = false;
  }

  public void fuelleFach(Ware aWare){
    getAktuellesFach().setWare(aWare);
    aktualisiereAnzeige();
  }

  public void schliesseFach(){
    if (this.mIstOffen){
      this.mIstOffen = false;
      System.out.println("Fach geschossen!");
    }
    else{
      this.mIstOffen = true;
    }
  }

  public int getTotalWarenWert(){
    int lWarenWert = 0;
    for (Fach lFach: mFaecher){
      if (!lFach.istLeer()){
        lWarenWert+= lFach.getWare().getPreis();
      }
    }
    return lWarenWert;
  }

  public void aktualisiereAnzeige(){
    Ware lWare = getAktuellesFach().getWare();
    if (getAktuellesFach().istLeer()){
      //SystemSoftware.zeigeWareInGui(this.mFachNr, null, null);
      System.out.println("ich bin leer");
      SystemSoftware.zeigeVerfallsDatum(this.mDrehtellerNr, 0);
      return;
    }
    //SystemSoftware.zeigeWareInGui(this.mFachNr, lWare.getName(), lWare.getDate());
    if (lWare.isHaltbarkeitUeberschritten()){
      SystemSoftware.zeigeVerfallsDatum(this.mDrehtellerNr, 2);
      return;
    }
    SystemSoftware.zeigeVerfallsDatum(this.mDrehtellerNr, 1);
    
  }

  public Automat getAutomat(){
    return this.mAutomat;
  }

  // public int getIdentischeWare(String aName){
  //   int lWarenWert = 0;
  //   for (Fach lFach: mFaecher){
  //     if (!lFach.istLeer() && aName.equals(lFach.getWare().getName())){
  //       lWarenWert+= lFach.getWare().getPreis();
  //     }
  //   }
  // }
}


/*
 * public int gibTotalenWarenWert() {
		int wert = 0;
		for (Fach fach : mFach) {
			if (fach.istWareImFach()) {
				wert += fach.getWare().getPrice();
			}
		}
		return wert;
	}

	public int gibWarenMenge(String warenName) {
		int menge = 0;
		for (Fach fach : mFach) {
			if ((fach.istWareImFach()) && (warenName.equals(fach.getWare().getName())) && !fach.getWare().istAbgelaufen()) {
				menge += 1;
			}
		}
		return menge;
	}
 */