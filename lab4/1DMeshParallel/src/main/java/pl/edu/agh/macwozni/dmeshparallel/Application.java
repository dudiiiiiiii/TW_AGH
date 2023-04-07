package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.parallelism.ConcurentBlockRunner;

class Application {

    public static void main(String args[]) {
        if(args.length != 1){
            System.exit(0);
        }

        Executor e = new Executor(new ConcurentBlockRunner(), Integer.parseInt(args[0]));
        e.start();
    }
}
