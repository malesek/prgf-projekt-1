import objectdata.Point;
import objectdata.Polygon;
import rasterdata.RasterBufferedImage;
import rasterop.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class Application {

    private JFrame frame;
    private JPanel panel;
    private RasterBufferedImage raster;

    private FilledLineRasterizer filledLineRasterizer;
    private DottedLineRasterizer dottedLineRasterizer;
    private SpecificFilledLineRasterizer specificFilledLineRasterizer;
    private SpecificDottedLineRasterizer specificDottedLineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private Point point1;
    private Point point2;
    private Polygon polygon;
    private int index;
    private boolean deletePoint;

    //pokud je 1 tak kreslím úsečku tažením (defaultní hodnota) -- zapnu stiskem X
    //pokud je 2 tak kreslím vodorovné, svislé nebo úhlopříčné úsečky -- zapnu stiskem SHIFT
    //pokud je 3 tak kreslím polygon -- zapnu stiskem P
    private int drawMode;

    /**
     * Inicializační metoda využívaná i pro uvedení všech proměnných do původního stavu
     */
    public void initializer(){
        filledLineRasterizer = new FilledLineRasterizer(raster);
        dottedLineRasterizer = new DottedLineRasterizer(raster);
        specificFilledLineRasterizer = new SpecificFilledLineRasterizer(raster);
        specificDottedLineRasterizer = new SpecificDottedLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(raster);
        polygon = new Polygon();
        point1 = null;
        point2 = null;
        index = -1;
        deletePoint = false;
    }
    public Application(int width, int height) {
        //Inicializace okna
        frame = new JFrame();
        raster = new RasterBufferedImage(width, height);
        raster.setClearColor(0x2f2f2f);

        initializer();
        drawMode = 1;

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }

        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //zde se rozhoduje v jakém módu člověk bude na základě kliknutí na tlačítko
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_X :
                        fullClear();
                        drawMode = 1;
                        break;
                    case KeyEvent.VK_SHIFT :
                        fullClear();
                        drawMode = 2;
                        break;
                    case KeyEvent.VK_P :
                        fullClear();
                        drawMode = 3;
                        break;
                    case KeyEvent.VK_C :
                        fullClear();
                        break;
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    raster.setPixel(e.getX(), e.getY(), 0xffff00);
                    point1 = new Point(e.getX(), e.getY());
                        //ošetření aby se do polygonu přidal první bod pouze při prvním kliknutí
                        if (polygon.getPoints().size() == 0) {

                            polygon.addPoint(new Point(e.getX(), e.getY()));
                        }
                }
                if(e.getButton() == MouseEvent.BUTTON2){
                    if(drawMode == 3){
                        point2 = findClosestPoint(polygon.getPoints(), e.getX(), e.getY());
                        polygon.removePoint(index);
                        deletePoint = true;
                        draw();
                    }
                }
                if(e.getButton() == MouseEvent.BUTTON3){
                    if(point1 != null && point2 != null){
                        Point tmp;
                        if(drawMode != 3){
                            //podmínka pro najití nejbližšího bodu v úsečce
                            if(Math.abs(point1.x - e.getX()) > Math.abs(point2.x - e.getX()) || Math.abs(point1.y - e.getY()) > Math.abs(point2.y - e.getY())){
                                point2 = new Point(e.getX(), e.getY());
                            }
                            else{
                                tmp = point1;
                                point1 = point2;
                                point2 = tmp;

                                point2 = new Point(e.getX(), e.getY());
                            }
                        }
                        else{
                            point2 = findClosestPoint(polygon.getPoints(), e.getX(), e.getY());
                            polygon.removePoint(index);
                        }
                    }
                }
                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!deletePoint){
                    if(point1 != null || polygon.getPoints().size() != 0){
                        draw();
                        panel.repaint();
                    }
                }
                else deletePoint = false;
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                    if(point1 != null || polygon.getPoints().size() != 0){
                        point2 = new Point(e.getX(), e.getY());
                        drawDrag();
                        panel.repaint();
                    }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });


    }

    /**
     * Metoda pro najití nejbližšího bodu v módu kreslení polygonu
     * @param points body v polygonu
     * @param clickX poloha x po kliknutí myší
     * @param clickY poloha y po kliknutí myší
     * @return
     */
    public Point findClosestPoint(ArrayList<Point> points, double clickX, double clickY) {
        double minDistance = Double.MAX_VALUE;
        Point closestPoint = null;

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            double distance = Math.sqrt(Math.pow((clickX - point.x), 2) + Math.pow((clickY - point.y), 2));
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
                closestPoint = point;
            }
        }
        return closestPoint;
    }

    /**
     * Method for drawing done lines after mouseReleased
     * Metoda pro kreslení hotových čar po zvednutí tlačítka myši
     */
    public void draw(){
        clear();
        if(drawMode == 1){
            filledLineRasterizer.rasterize(point1.x, point1.y, point2.x, point2.y, 0xffff00);
        }
        else if (drawMode == 2) {
            specificFilledLineRasterizer.rasterize(point1.x, point1.y, point2.x, point2.y, 0xffff00);
        } else{
            if(index == -1 && !deletePoint){
                polygon.addPoint(new Point(point2.x, point2.y));
                polygonRasterizer.drawPolygon(polygon);
            } else if (deletePoint) {
                polygonRasterizer.drawPolygon(polygon);

            } else {
                polygon.addPointAtIndex(index, new Point(point2.x, point2.y));
                polygonRasterizer.drawPolygon(polygon);
                index = -1;
            }

        }
    }
    /**
     * Metoda pro kreslení tečkovaných čar při táhnutí myší
     */
    public void drawDrag(){
        clear();
        if(drawMode == 1) {
            dottedLineRasterizer.rasterize(point1.x, point1.y, point2.x, point2.y, 0xffff00);
        }
        else if (drawMode == 2){
            specificDottedLineRasterizer.rasterize(point1.x, point1.y, point2.x, point2.y, 0xffff00);
        }
        else{
            //zde řeším zda táhne člověk pravým tlačítkem a pokud ano ošetřuji chyby
            if(index == -1){
                dottedLineRasterizer.rasterize(polygon.getPoints().get(0), point2, 0xffff00);
                dottedLineRasterizer.rasterize(polygon.getPoints().get(polygon.getPoints().size()-1), point2, 0xffff00);
                polygonRasterizer.drawPolygon(polygon);
            }
            else if(index == 0){
                dottedLineRasterizer.rasterize(polygon.getPoints().get(polygon.getPoints().size()-1), point2, 0xffff00);
                dottedLineRasterizer.rasterize(polygon.getPoints().get(index), point2, 0xffff00);
                polygonRasterizer.drawPolygon(polygon);
            }
            else{
                dottedLineRasterizer.rasterize(polygon.getPoints().get(index-1 < 0 ? polygon.getPoints().size()-1 : index-1), point2, 0xffff00);
                dottedLineRasterizer.rasterize(polygon.getPoints().get(index > polygon.getPoints().size()-1 ? 0 : index), point2, 0xffff00);
                polygonRasterizer.drawPolygon(polygon);
            }

        }
    }

    /**
     * Vyčístí raster a všechny proměnné (kromě drawMode abysme zůstali v kreslícím módu, ve kterém jsme byli)
     */
    public void fullClear(){
        clear();
        initializer();
        panel.repaint();
    }

    /**
     * Vymaže pouze raster
     */
    public void clear(){
        raster.clear();
        raster.getGraphics().drawString("Pro kreslení úsečky stiskněte X", 5, 15);
        raster.getGraphics().drawString("Pro kreslení vodorovné, svislé nebo úhlopříčné úsečky stiskněte SHIFT", 5, 25);
        raster.getGraphics().drawString("Pro kreslení polygonu stiskněte P", 5, 35);
    }

    public void start() {
        clear();
        panel.repaint();
    }

    public void present(Graphics graphics){
        raster.repaint(graphics);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application(800, 600).start());
    }

}