package CW2_zd;

public class SemaforBroken {
    private boolean _stan = true;
    private int _czeka=0;

    public SemaforBroken() {}

    public synchronized void P(){
        if(!_stan){
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
        if(_stan){
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

// ZAD 2 ODP

// Zwykły semafor powoduje poprawną synchronizację i wynik końcowy jest zawsze równy 0 natomiast semafor z "ifem" daje różne wyniki

// Może nastąpic sytuacja że dwa wątki chcą wejść do jednego monitora ( oba chcą wykonać operację P dekrementacji)
// więc ustawiają się w kolejce i któryśwchodzi do wewnątrz wykonuje operację i zwalnia monito, teraz drugi wchodzi widzi że if nie jest spełniony więc czeka
// kiedy jakikolwiek inny wątek wykona metodę notify obecny wątek zostanie wybudzony warunek juz nie zostanie sprawdzony i zmieni wartośc semafor na 0 niezależnie co miał w sobie.

// metoda wait może też powodować spontaniczne wybudzenie wątku - link do dokumentacji:
// https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Object.html?fbclid=IwAR3DsKHGMIZhf3D1LMv1R_nuu0KH3lpiDoABsSdvqMPTb-69mXhEIxrN9lw#wait%28long%29
// A thread can also wake up without being notified, interrupted, or timing out, a so-called spurious wakeup.