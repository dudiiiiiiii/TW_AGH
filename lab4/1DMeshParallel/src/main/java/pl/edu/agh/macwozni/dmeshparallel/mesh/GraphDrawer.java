package pl.edu.agh.macwozni.dmeshparallel.mesh;

import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class GraphDrawer implements PDrawer<Vertex> {

@Override
public void draw(Vertex v) {
    System.out.println();
    while(v.getNorth() != null){
        v = v.getNorth();
    }
    while(v.getWest() != null){
        v = v.getWest();
    }

    boolean end = false;

    while (!end) {
        Vertex lower = v;
        StringBuilder low = new StringBuilder();
        while (lower != null) {
            System.out.print(lower.getLabel() + (lower.getEast() != null ? "---" : "  "));
            if(lower.getSouth() != null){
                low.append(" |    ");
            }
            lower = lower.getEast();

        }

        System.out.print("\n");
        System.out.println(low);
        if(v != null && v.getSouth() == null){
            end = true;
        }else {
            assert v != null;
            v = v.getSouth();
        }
    }
}
}
