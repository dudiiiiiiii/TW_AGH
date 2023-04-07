package CW2_zd;

public class Counter {
    private int count = 0;
    private Semafor semafor;
    private SemaforBroken semaforBroken;

    public static void main(String args[]){
        System.out.println("Semafor good: ");
        new Counter().raceGood();
        System.out.println("Semafor broken: ");
        new Counter().raceBroken();

    }

    public void raceGood(){
        this.semafor = new Semafor();

        Thread inc[] = new Thread[1];
        inc[0] = new Thread(() -> {
            for(int i=0; i < 10000000; i++) increase();
        });
        Thread dec[] = new Thread[1];
        dec[0] = new Thread(() -> {
            for(int i=0; i < 10000000; i++) decrease();
        });

        inc[0].start();
        dec[0].start();

        try{
            inc[0].join();
            dec[0].join();
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(count);
    }

    public void raceBroken(){
        this.semaforBroken = new SemaforBroken();

        Thread inc[] = new Thread[1];
        inc[0] = new Thread(() -> {
            for(int i=0; i < 10000000; i++) increaseB();
        });
        Thread dec[] = new Thread[1];
        dec[0] = new Thread(() -> {
            for(int i=0; i < 10000000; i++) decreaseB();
        });

        inc[0].start();
        dec[0].start();

        try{
            inc[0].join();
            dec[0].join();
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(count);
    }

    public void increase(){
        semafor.P();
        count++;
        semafor.V();
    }

    public void decrease(){
        semafor.P();
        count--;
        semafor.V();
    }

    public void increaseB(){
        semaforBroken.P();
        count++;
        semaforBroken.V();
    }

    public void decreaseB(){
        semaforBroken.P();
        count--;
        semaforBroken.V();
    }
}
