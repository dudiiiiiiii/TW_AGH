public class Starving {
    private int[] forks = {1,1,1,1,1};
    private int[] res = {0,0,0,0,0};

    public static void main(String[] args){
        Starving tmp = new Starving();

        Thread[] philosofers = new Thread[5];
        for(int i=0; i < 5; i++){
            int finalI = i;
            philosofers[i] = new Thread(() -> {
                try {
                    tmp.philosofer_life(finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for(int i =0; i < 5; i++){
            philosofers[i].start();
        }

        tmp.showRes();

    }

    public void philosofer_life(int i) throws InterruptedException {
        int eating = 0;
        for(int j = 0; j < 10000; j++){
            eating = this.takeForks(i);
            if(eating==1) this.eat(i);
        }
    }

    public int takeForks(int i){
        if(forks[i] == 1){
            forks[i] = 0;
            if(forks[(i+1)%5] == 1){
                forks[(i+1)%5] = 0;
                return 1;
            }
            else {
                forks[i] = 1;
            }
        }
        return 0;
    }

   public void eat(int i) throws InterruptedException {
//        System.out.println("eating nr: "+ i);
//        Thread.sleep(100);
        this.forks[i] = 1;
        this.forks[(i+1)%5] = 1;
        this.res[i] += 1;
   }

   public void showRes(){
       for(int k = 0; k< 5; k++)
           System.out.println(k+": "+this.res[k]);

//       for(int k = 0; k< 5; k++)
//           System.out.println(k+": "+this.forks[k]);
   }



}
