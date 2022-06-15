
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
    int lTotalerWarenWert = 0;
    for (Fach lFach : mFaecher){
      if (!lFach.istLeer()){
        lTotalerWarenWert += lFach.getWare().getPreis();
      }
    }
    return lTotalerWarenWert;
  }

  public void aktualisiereAnzeige(){
    Ware lWare = getAktuellesFach().getWare();
    SystemSoftware.zeigeVerfallsDatum(this.mDrehtellerNr, lWare.getZustandDisplay());    
  }

  public Automat getAutomat(){
    return this.mAutomat;
  }
}
