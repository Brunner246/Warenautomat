package warenautomat;

import java.time.LocalDate;

public class Kauf {
    private LocalDate mDate;
    private Ware mWare;
    
    public Kauf(Ware aWare){
        this.mWare = aWare;
        mDate = SystemSoftware.gibAktuellesDatum();
    }

    public Ware getWare(){
        return this.mWare;
    }

    public LocalDate getDate(){
        return mDate;
    }
}
