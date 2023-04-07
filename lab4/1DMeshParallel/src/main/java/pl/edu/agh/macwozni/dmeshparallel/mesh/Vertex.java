package pl.edu.agh.macwozni.dmeshparallel.mesh;

public class Vertex {

    //label
    private String mLabel;
    //links to adjacent elements
    private Vertex mNorth;
    private Vertex mEast;
    private Vertex mSouth;
    private Vertex mWest;

    //methods for adding links
    public Vertex(Vertex _north, Vertex _east, Vertex _south, Vertex _west, String _lab) {
        this.mNorth = _north;
        this.mEast = _east;
        this.mSouth = _south;
        this.mWest = _west;
        this.mLabel = _lab;
    }
    //empty constructor

    public Vertex() {
    }


    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String _lab){ this.mLabel = _lab; }

    public Vertex getNorth() {
        return mNorth;
    }

    public void setNorth(Vertex mNorth) {
        this.mNorth = mNorth;
    }

    public Vertex getEast() {
        return mEast;
    }

    public void setEast(Vertex mEast) {
        this.mEast = mEast;
    }

    public Vertex getSouth() {
        return mSouth;
    }

    public void setSouth(Vertex mSouth) {
        this.mSouth = mSouth;
    }

    public Vertex getWest() {
        return mWest;
    }

    public void setWest(Vertex mWest) {
        this.mWest = mWest;
    }
}
