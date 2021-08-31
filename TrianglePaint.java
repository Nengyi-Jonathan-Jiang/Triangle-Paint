import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrianglePaint extends JPanel implements MyMouseListener.MouseObserver{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private static final double SQRT_3_2 = 0.86602540378;
    private static final int SIZE = 32;

    private static final Color BLACK     = new Color(0,0,0);
    private static final Color BLUE      = new Color(25, 60, 100);
    private static final Color BLUE_GREY = new Color(100, 130, 170);
    private static final Color WHITE     = new Color(255,255,255);
    private static final Color GRAY      = new Color(127,127,127);

    public MyKeyListener keys;

    private byte[][] data = new byte[SIZE][SIZE * 2];

    private double scale = 33.3;

    private boolean isMouseDown = false;
    private int mouseX = 0;
    private int mouseY = 0;

    public TrianglePaint() // constructor - sets up the class
    {
        setSize(WIDTH,HEIGHT);
        setBackground(WHITE);
        setVisible(true);

        //Key press detection
        keys = new MyKeyListener(this);

        //Repaint window at 100 FPS
        new Timer(10, e -> repaint()).start();
    }

    @Override
    public void paint(Graphics window) {
        super.paint(window);
        Graphics2D g = (Graphics2D)window;
        g.setStroke(new BasicStroke(1));
        
        for(int i = 0; i < SIZE; i++){
            //All possible x-coords, I calculate them once
            int x1 = (int)((i - 1.) * scale),
                x2 = (int)((i - .5) * scale),
                x3 = (int)((i + 0.) * scale),
                x4 = (int)((i + .5) * scale),
                x5 = (int)((i + 1.) * scale);
            a: for(int j = 0; j < SIZE * 2; j++){
                int y1 = (int)(((j >> 1) + 0) * scale * SQRT_3_2),
                    y2 = (int)(((j >> 1) + 1) * scale * SQRT_3_2);

                switch(data[i][j]){ //set color
                    case 1:     g.setColor(BLACK); break;
                    case 2:     g.setColor(BLUE_GREY); break;
                    case 3:     g.setColor(BLUE); break;
                    default: 
                        if(!keys.isKeyPressed(KeyEvent.VK_V)){
                            g.setColor(GRAY);
                            switch(j & 3){
                                case 0:
                                    g.drawPolygon(new int[]{y1,y2,y1}, new int[]{x1,x2,x3}, 3);
                                    break;
                                case 1:
                                    g.drawPolygon(new int[]{y2,y1,y2}, new int[]{x2,x3,x4}, 3);
                                    break;
                                case 2:
                                    g.drawPolygon(new int[]{y1,y2,y1}, new int[]{x2,x3,x4}, 3);
                                    break;
                                case 3:
                                    g.drawPolygon(new int[]{y2,y1,y2}, new int[]{x3,x4,x5}, 3);
                                    break;
                                default: break;
                            }
                        }
                        continue a;
                }
                switch(j & 3){
                    case 0:
                        g.fillPolygon(new int[]{y1,y2,y1}, new int[]{x1,x2,x3}, 3);
                        break;
                    case 1:
                        g.fillPolygon(new int[]{y2,y1,y2}, new int[]{x2,x3,x4}, 3);
                        break;
                    case 2:
                        g.fillPolygon(new int[]{y1,y2,y1}, new int[]{x2,x3,x4}, 3);
                        break;
                    case 3:
                        g.fillPolygon(new int[]{y2,y1,y2}, new int[]{x3,x4,x5}, 3);
                        break;
                    default: break;
                }
            }
        }
    }

    @Override
    public void onMouseClick(int x, int y) {}
    @Override
    public void onMouseWheel(int wheelRotation) {
        scale *= 1. - .01 * wheelRotation;
    }

    @Override
    public void onMouseMove(int x, int y) {
        double Y = x / scale / SQRT_3_2, X = y / scale - 0.5 * (Y % 2) + 1.;
        mouseX = (int)X;
        mouseY = 2 * (int)Y + (int)((X % 1.0) + (Y % 1.0));
    }

    @Override
    public void onMouseDown(int x, int y) {
        isMouseDown = true;
        data[mouseX][mouseY] = (byte)((data[mouseX][mouseY] + 1) & 3);
    }
    
    @Override
    public void onMouseUp(int x, int y) {
        isMouseDown = false;
    }
}