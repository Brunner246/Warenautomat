
package warenautomat;

public class Drehteller {

  public static final int ANZAHL_FAECHER = 16;

  private Fach[] mFaecher; // [16    -   0..1]
  private boolean mIstOffen;
  private int mFachNr;
  
  public Drehteller(){
    this.mFaecher = new Fach[ANZAHL_FAECHER];
    for (int il = 0; il < mFaecher.length; il++){
      this.mFaecher[il] = new Fach();
    }
  }

  public void drehen(){
    this.mFachNr ++;
    if (this.mFachNr >= ANZAHL_FAECHER){
      this.mFachNr = 0;
    }
  }

  public Fach getAktuellesFach(){
    return mFaecher[this.mFachNr];
  }

  public void oeffnen(){
    Fach lFach = getAktuellesFach();
    if (lFach != null){
      this.mIstOffen = true;
    }
    this.mIstOffen = false;
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