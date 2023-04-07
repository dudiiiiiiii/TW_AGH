package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.mesh.GraphDrawer;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PE;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PH;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PI;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PS;
import pl.edu.agh.macwozni.dmeshparallel.parallelism.BlockRunner;
import pl.edu.agh.macwozni.dmeshparallel.production.IProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

import java.util.LinkedList;
import java.util.List;

public class Executor extends Thread {
    
    private final BlockRunner runner;
    private final int N;
    
    public Executor(BlockRunner _runner, int n){
        this.runner = _runner;
        N = n;
    }

@Override
public void run() {
    List<IProduction<Vertex>> lastCreated = new LinkedList();

    PDrawer<Vertex> drawer = new GraphDrawer();

    Vertex s = new Vertex(null, null, null, null, "S");

    drawer.draw(s);

    PI pi = new PI(s, drawer);
    this.runner.addThread(pi);
    this.runner.startAll();

    drawer.draw(s);
    lastCreated.add(pi);

    // top left half of rectangle
    for (int i = 1; i < N; i++) {
        List<IProduction<Vertex>> psList = new LinkedList();
        List<IProduction<Vertex>> phList = new LinkedList();

        IProduction<Vertex> pe =
                new PE(lastCreated.get(0).getObj(), drawer);

        for (IProduction<Vertex> prod : lastCreated) {
            Vertex v = prod.getObj();

            psList.add(new PS(v, drawer));

            if (v.getNorth() != null &&
                    v.getNorth().getEast() == null)
                phList.add(new PH(v.getNorth(),drawer));
        }

        this.runner.addThread(pe);
        for (IProduction<Vertex> ps : psList)
            this.runner.addThread(ps);
        for (IProduction<Vertex> ph : phList)
            this.runner.addThread(ph);

        this.runner.startAll();

        lastCreated.clear();
        lastCreated.add(pe);
        lastCreated.addAll(psList);
        drawer.draw(s);
    }

    // bottom right half of rectangle
    IProduction<Vertex> westmost = lastCreated.remove(0);

    IProduction<Vertex> southmost = null;

    for (int i = N - 2; i >= 0; i--) {
        List<IProduction<Vertex>>
                psList = new LinkedList(),
                phList = new LinkedList();

        for (IProduction<Vertex> prod : lastCreated) {
            Vertex v = prod.getObj();

            if (v.getNorth() != null &&
                    v.getNorth().getEast() == null)
                phList.add(new PH(v.getNorth(), drawer));
        }

        // last row connections
        if (southmost != null)
            phList.add(new PH(southmost.getObj(), drawer));

        southmost = lastCreated.remove(i);

        for (IProduction<Vertex> prod : lastCreated)
            psList.add(new PS(prod.getObj(), drawer));

        for (IProduction<Vertex> ps : psList)
            this.runner.addThread(ps);
        for (IProduction<Vertex> pj : phList)
            this.runner.addThread(pj);

        this.runner.addThread(westmost = new PS(westmost.getObj(), drawer));

        this.runner.startAll();
        drawer.draw(s);

        lastCreated.clear();
        lastCreated.addAll(psList);
    }

    assert southmost != null;
    this.runner.addThread(new PH(southmost.getObj(), drawer));

    this.runner.startAll();
    drawer.draw(s);

    //done
    System.out.println("done");
}
}
