import java.awt.*;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrianglePaint extends JPanel {

    private static final double SQRT_3_2 = 0.86602540378;

    private static final Color BLACK     = new Color(0,0,0);
    private static final Color BLUE      = new Color(25, 60, 100);
    private static final Color BLUE_GREY = new Color(100, 130, 170);
    private static final Color WHITE     = new Color(255,255,255);

    public MyKeyListener keys;

    private byte[][] data = new byte[256][256];

    private double scale = 5.0;
    private double xOffset = 0.0;
    private double yOffset = 0.0;

    public TrianglePaint() // constructor - sets up the class
    {
        setSize(1600, 900);
        setBackground(Color.WHITE);
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
        g.setStroke(new BasicStroke(2));

        Color color;

        for(int i = 0; i < 256; i++){   //loop through y's
            int x1 = (int)((i + 0.0) * scale),
                x2 = (int)((i + 0.5) * scale),
                x3 = (int)((i + 1.0) * scale),
                x4 = (int)((i + 1.5) * scale);
            for(int j = 0; j < 256; j++){
                int y1 = (int)(((j >> 1) + 0) * scale * SQRT_3_2),
                    y2 = (int)(((j >> 1) + 1) * scale * SQRT_3_2);

                switch(data[i][j]){
                    case 1:     color = WHITE; break;
                    case 2:     color = BLUE_GREY; break;
                    case 3:     color = BLUE; break;
                    default:    color = BLACK; break;
                }
                g.setColor(color);
                switch(j & 3){
                    case 0:
                        g.drawPolygon(new int[]{x1,x2,x3}, new int[]{y1,y2,y1}, 3);
                        break;
                    case 1:
                        g.drawPolygon(new int[]{x2,x3,x4}, new int[]{y2,y1,y2}, 3);
                        break;
                    case 2:
                        g.drawPolygon(new int[]{x2,x3,x4}, new int[]{y1,y2,y1}, 3);
                        break;
                    case 3:
                        g.drawPolygon(new int[]{x1,x2,x3}, new int[]{y2,y1,y2}, 3);
                        break;
                    default: break;
                }
            }
        }

    }
}