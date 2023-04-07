package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PE extends AbstractProduction<Vertex> {

    public PE(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex _p) {
        Vertex _pNew = new Vertex(null, null, null, _p, "[M]");
        System.out.println("-> PE");
        _p.setEast(_pNew);

        return _pNew;
    }
}
