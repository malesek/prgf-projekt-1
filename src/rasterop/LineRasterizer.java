package rasterop;

import objectdata.Line;
import objectdata.Point;
import rasterdata.Raster;

import java.awt.Color;

/**
 * Represents algorithm for drawing lines
 */
public abstract class LineRasterizer {
    Raster raster;
    int color;
    
    public LineRasterizer(Raster raster){
        this.raster = raster;
    }
    
    public void setColor(int color) {
        this.color = color;
    }

    public void rasterize(double x1, double y1, double x2, double y2, int color){
        setColor(color);
        drawLine(x1,y1,x2,y2);
    }
    public void rasterize(Point p1, Point p2, int color){
        rasterize(p1.x, p1.y, p2.x, p2.y, color);
    }

    protected void drawLine(double x1, double y1, double x2, double y2) {

    }
}
