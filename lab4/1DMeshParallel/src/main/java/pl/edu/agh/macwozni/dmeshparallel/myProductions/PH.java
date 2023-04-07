package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PH extends AbstractProduction<Vertex> {

    public PH(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex _p) {
        Vertex tmp = _p.getNorth().getEast().getSouth();
        System.out.println("-> PH");
        _p.setEast(tmp);
        tmp.setWest(_p);

        return _p;
    }
}
