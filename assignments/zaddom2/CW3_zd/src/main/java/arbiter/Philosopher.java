package arbiter;

import java.util.Scanner;

public class Philosopher extends Thread {
    private Table table;
    private final int id;
    private final int mealsAmount;
    private double fullTime = 0;

    public Philosopher(Table table, int id, int mealsAmount) {
        this.table = table;
        this.id = id;
        this.mealsAmount = mealsAmount;
    }

    private void getForks() throws InterruptedException {
        boolean success = false;
        // waiting doubles after failure (BEB)
        int waitTime = 1;

        table.takePlace(this.id);

        while(!success) {
            Thread.sleep(waitTime);

            if(table.forks[id] && table.forks[(id+1)%table.n]){
                success = true;
                table.forks[id] = false;
                table.forks[(id+1)%table.n] = false;
            } else {
                waitTime *= 2;
            }
        }
    }

    private void releaseForks() throws InterruptedException {

        table.forks[id] = true;
        table.forks[(id+1)%table.n] = true;
        table.leavePlace(this.id);
    }

    @Override
    public void run() {
        long tmpStartTime = 0;
        try {
            for(int k = 0 ; k < mealsAmount; ++k){
                tmpStartTime = System.currentTimeMillis();

                getForks();

                fullTime += (System.currentTimeMillis() - tmpStartTime);

                releaseForks();
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        System.out.println("Arbiter");
        int philNum=0;
        int mealsNum = 0;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input philNum: ");
        philNum = scanner.nextInt();
        System.out.println("Input meals: ");
        mealsNum = scanner.nextInt();

        Table table = new Table(philNum);

        Philosopher[] philosophers = new Philosopher[philNum];

        for(int i=0; i < philNum; i++){
            philosophers[i] = new Philosopher(table, i, mealsNum);
        }

        for(int i=0; i < philNum; i++){
            philosophers[i].start();
        }

        for(int i=0; i < philNum; i++){
            philosophers[i].join();
        }

        for(int i=0; i < philNum; i++){
            System.out.println("Philosopher number " + i + " avg wait time = " + (philosophers[i].fullTime/mealsNum));
        }

    }

}
