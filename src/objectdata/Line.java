package objectdata;

/**
 * Represents a line in 2D space; immutable
 */
public class Line {
    public final Point p1;
    public final Point p2;

    public Line(Point _p1, Point _p2){
        p1 = _p1;
        p2 = _p2;
    }
}
