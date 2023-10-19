package objectdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a polygon in a 2D space;
 */
public class Polygon {
    private ArrayList<Point> points;
    public Polygon(){
        points = new ArrayList<>();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Method for getting point at exact index in ArrayList
     * @param index
     * @return Point
     */
    public Point getPoint(int index) { return points.get(index);}

    /**
     * Adds desired point at the end of ArrayList
     * @param point
     */
    public void addPoint(Point point) {
        this.points.add(point);
    }

    /**
     * Adds desired point at chosen index of ArrayList
     * @param index
     * @param point
     */
    public void addPointAtIndex(int index, Point point) {
        this.points.add(index, point);
    }

    public void removePoint(int index){
        this.points.remove(index);
    }

}
