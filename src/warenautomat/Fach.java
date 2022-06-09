package warenautomat;

public class Fach {
    
    private Ware mWare; // [0..1    -   0..1]

    public Fach(){}

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
}


// public static void main(String[] args) {

//     Fach lFach = new Fach();
//     Ware lWare = new Ware();
//     lWare = null;
//     lFach.setWare(lWare);
//     System.out.println(lFach.istLeer());
// }