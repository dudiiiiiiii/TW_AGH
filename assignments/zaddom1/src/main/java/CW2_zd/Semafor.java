package CW2_zd;

public class Semafor {
    private boolean _stan = true;
    private int _czeka=0;

    public Semafor() {}

    public synchronized void P(){
        while(!_stan){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        _stan = false;
        _czeka = 1;
        this.notifyAll();
    }

    public synchronized void V(){
        while(_stan){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        _stan = true;
        _czeka = 0;
        this.notifyAll();
    }
}
