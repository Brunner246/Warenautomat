package warenautomat;

public class Fach {
    
    private Ware mWare; // [0..1    -   0..1]
    private Drehteller mDrehteller;

    public Fach(Drehteller aDrehteller){
        this.mDrehteller = aDrehteller;
    }

    public void setWare(Ware aWare){
        this.mWare = aWare;
    }

    public Ware getWare(){
        return this.mWare;
    }

    public boolean istLeer(){
        if (this.mWare != null){
            return false;
        }
        else{
            return true;
        }
    }

    public void entferneWare(){
        this.mWare = null;
    }

    public Drehteller getDrehteller(){
        return this.mDrehteller;
    }
}
