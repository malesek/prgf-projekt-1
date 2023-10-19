package rasterop;

import objectdata.Polygon;
import rasterdata.Raster;

public class PolygonRasterizer extends FilledLineRasterizer{
    public PolygonRasterizer(Raster raster) {
        super(raster);
    }

    /**
     * Metoda pro vykreslení/překreslení polygonu
     * @param polygon
     */
    public void drawPolygon(Polygon polygon) {
        for (int i = 1; i < polygon.getPoints().size(); i++) {
            rasterize(polygon.getPoints().get(i), polygon.getPoints().get(i-1), 0xffff00);
        }
        rasterize(polygon.getPoints().get(0), polygon.getPoints().get(polygon.getPoints().size()-1), 0xffff00);
    }
}
