package starving;

import java.util.concurrent.Semaphore;

public class Table {
    final int n;
    boolean[] forks;
    final Semaphore semaphore = new Semaphore(1);

    public Table(int n) {
        this.n = n;
        this.forks = new boolean[n];

        for(int i=0; i < n; i++){
            forks[i] = true;
        }
    }
}
