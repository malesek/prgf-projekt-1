package rasterop;

import rasterdata.Raster;

import static java.lang.Math.abs;

public class SpecificFilledLineRasterizer extends LineRasterizer {

    public SpecificFilledLineRasterizer(Raster raster) {
        super(raster);
    }
    /**
     * DDA algoritmus využítý pro vykreslení horizontální, vertikální nebo diagonální plné čáry
     * @param x1 x prvního bodu; hodnoty od 0 do šířky Rasteru
     * @param y1 y prvního bodu; hodnoty od 0 do výšky Rasteru
     * @param x2 x druhého bodu; hodnoty od 0 do šířky Rasteru
     * @param y2 y prvního bodu; hodnoty od 0 do výšky Rasteru
     */
    public void drawLine(double x1, double y1, double x2, double y2) {
        //výpočet směrnice přímky
        double k = Math.round((y2 - y1) / (x2 - x1));

        //výpočet posunutí přímky na ose x
        double q = y1 - k * x1;

        //výběr řídící osy
        if ((abs((float) y2 - y1)) < (abs((float) x2 - x1))) {
            //prohození proměnných pokud x2 je menší než x1
            if (x2 < x1) {
                double tmp = x1;
                x1 = x2;
                x2 = tmp;
            }

            //cyklus pro vykreslení bodů úsečky
            for (double x = x1; x < x2; x++) {
                double y = k * x + q;
                raster.setPixel((int) Math.round(x), (int) Math.round(y), color);
            }
        } else {
            //prohození proměnných pokud y2 je menší než y1
            if (y2 < y1) {
                double tmp = y1;
                y1 = y2;
                y2 = tmp;
            }

            //cyklus pro vykreslení bodů úsečky
            for (double y = y1; y < y2; y++) {
                raster.setPixel((int) Math.round(x1), (int) Math.round(y), color);
            }
        }
    }
}
