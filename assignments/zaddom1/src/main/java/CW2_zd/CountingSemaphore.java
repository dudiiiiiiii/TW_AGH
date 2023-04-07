package CW2_zd;

public class CountingSemaphore {
    private long state;
    private Semafor access;
    private Semafor stateSem;

    public CountingSemaphore(long state){
        if(state < 0){
            System.exit(-1);
        }
        this.state = state;
        access = new Semafor();
        stateSem = new Semafor();
    }

    public void V(){
        access.P();
        state++;
        if(state==1) stateSem.V();
        access.V();
    }

    public void P(){
        stateSem.P();
        access.P();
        state--;
        if(state > 0) stateSem.V();
        access.V();
    }
}


// ODP ZAD 3

// Semafor binarny jest szczególnym przypadkiem semafora ogólnego.
// Semafor ogólny ma odgórnie ustawioną pewną liczbę operacji opuszczenia semafora natomiast dla binarnego tą liczbą jest 1.
// Stanu w obu nie można opusćić poniżej 0.
