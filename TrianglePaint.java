import java.awt.*;

import javax.swing.JPanel;
import javax.swing.Timer;

public class TrianglePaint extends JPanel implements MyMouseListener.MouseObserver{

    private static final double SQRT_3 = 1.73205080757;
    private static final int SIZE = 32;

    private static final Color BLACK     = new Color(0,0,0);
    private static final Color BLUE      = new Color(25, 60, 100);
    private static final Color BLUE_GREY = new Color(100, 130, 170);
    private static final Color WHITE     = new Color(255,255,255);

    public MyKeyListener keys;

    private byte[][] data = new byte[SIZE][SIZE * 2];

    private double scale = 64.0;

    public TrianglePaint() // constructor - sets up the class
    {
        setSize(1600, 900);
        setBackground(BLACK);
        setVisible(true);

        //Key press detection
        keys = new MyKeyListener(this);

        //Repaint window at 100 FPS
        new Timer(10, e -> repaint()).start();

        data[0][0] = 1;
        data[0][1] = 2;
        data[0][2] = 3;
        data[0][3] = 1;
        data[0][4] = 2;
        data[0][5] = 3;
    }

    @Override
    public void paint(Graphics window) {
        super.paint(window);
        Graphics2D g = (Graphics2D)window;
        g.setStroke(new BasicStroke(2));
        
        Color color;

        for(int i = 0; i < SIZE; i++){   //loop through x's
            int x1 = (int)((2 * i + 0) * scale / 2),
                x2 = (int)((2 * i + 1) * scale / 2),
                x3 = (int)((2 * i + 2) * scale / 2),
                x4 = (int)((2 * i + 3) * scale / 2),
                x5 = (int)((2 * i + 4) * scale / 2);
            a: for(int j = 0; j < SIZE * 2; j++){
                int y1 = (int)(((j >> 1) + 0) * scale),// * SQRT_3),
                    y2 = (int)(((j >> 1) + 1) * scale);// * SQRT_3);

                switch(data[i][j]){
                    case 1:     color = WHITE; break;
                    case 2:     color = BLUE_GREY; break;
                    case 3:     color = BLUE; break;
                    default: continue a;
                }
                g.setColor(color);
                switch(j & 3){
                    case 0:
                        g.fillPolygon(new int[]{x1,x2,x3}, new int[]{y1,y2,y1}, 3);
                        break;
                    case 1:
                        g.fillPolygon(new int[]{x2,x3,x4}, new int[]{y2,y1,y2}, 3);
                        break;
                    case 2:
                        g.fillPolygon(new int[]{x2,x3,x4}, new int[]{y1,y2,y1}, 3);
                        break;
                    case 3:
                        g.fillPolygon(new int[]{x3,x4,x5}, new int[]{y2,y1,y2}, 3);
                        break;
                    default: break;
                }
            }
        }
    }

    @Override
    public void onMouseClick(int x, int y) {
        double X = x / scale, Y = y / scale;// / SQRT_3;

        int i = (int)(X);
        int j = 2 * (int)(Y);

        System.out.println("(" + x + "," + y + ") | (" + (int)X + ", " + (int)Y + ") | (" + i + ", " + j + ")");

        data[i][j] = (byte)((data[i][j] + 1) & 3);
    }

    @Override
    public void onMouseWheel(int wheelRotation) {
        scale *= 1. + .1 * wheelRotation;
    }

    @Override
    public void onMouseMove(int x, int y) {}
    @Override
    public void onMouseDown(int x, int y) {}
    @Override
    public void onMouseUp(int x, int y) {}
}