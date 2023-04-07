package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PS extends AbstractProduction<Vertex> {

    public PS(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex _p) {
        Vertex _pNew = new Vertex(_p, null, null, null, "[M]");
        System.out.println("-> PS");
        _p.setSouth(_pNew);

        return _pNew;
    }
}