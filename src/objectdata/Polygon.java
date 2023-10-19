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
     * Přídá námi zvolený bod na konec ArrayListu
     * @param point
     */
    public void addPoint(Point point) {
        this.points.add(point);
    }

    /**
     * Přídá námi zvolený bod na námi zvolený index v ArrayListu
     * @param index
     * @param point
     */
    public void addPointAtIndex(int index, Point point) {
        this.points.add(index, point);
    }

    /**
     * Odebere bod na námi zvoleném indexu
     * @param index
     */
    public void removePoint(int index){
        this.points.remove(index);
    }

}
