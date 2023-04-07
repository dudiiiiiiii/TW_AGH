package arbiter;

import java.util.concurrent.Semaphore;

public class Table {
    final int n;
    boolean[] forks;
    boolean[] sitting;
    final Semaphore semaphore = new Semaphore(1);
    Semaphore emptyChairs;

    public Table(int n) {
        this.n = n;
        this.forks = new boolean[n];
        this.sitting = new boolean[n];
        int i1 = n / 2;
        this.emptyChairs = new Semaphore(2);

        for(int i=0; i < n; i++){
            forks[i] = true;
            sitting[i] = false;
        }
    }

    public void takePlace(int id) throws InterruptedException {
        emptyChairs.acquire();
        semaphore.acquire();

        System.out.println("siadam do stołu: "+id);
        sitting[id] = true;
        semaphore.release();
    }

    public void leavePlace(int id) throws InterruptedException {
        semaphore.acquire();

        sitting[id] = false;
        System.out.println("wstaję: "+id);

        emptyChairs.release();
        semaphore.release();
        Thread.sleep(1);
    }


}