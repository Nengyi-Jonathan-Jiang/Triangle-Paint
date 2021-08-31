import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrianglePaint extends JPanel implements MyMouseListener.MouseObserver{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private static double time = 0;

    private static final double SQRT_3_2 = 0.86602540378;
    private static final int SIZE = 32;

    private static final Color[] COLORS = new Color[]{
        new Color(  0,  0,  0),
        new Color( 25, 60,100),
        new Color(100,130,170),
    };

    public MyKeyListener keys;

    private int[][] data = new int[SIZE][SIZE * 2];

    private double scale = 33.3;

    private boolean isMouseDown = false;
    private int mouseX = 0;
    private int mouseY = 0;
    public int currColor = 0;

    public TrianglePaint() // constructor - sets up the class
    {
        setSize(WIDTH,HEIGHT);
        setBackground(Color.WHITE);
        setVisible(true);

        //Key press detection
        keys = new MyKeyListener(this);

        //Repaint window at 100 FPS
        new Timer(10, e -> {repaint(); time += 0.01;}).start();
    }

    @Override
    public void paint(Graphics window) {
        super.paint(window);
        Graphics2D g = (Graphics2D)window;
        g.setStroke(new BasicStroke(1));
        
        for(int i = 0; i < SIZE; i++){
            int x1 = (int)((i - 1.) * scale), x2 = (int)((i - .5) * scale),
                x3 = (int)((i + 0.) * scale), x4 = (int)((i + .5) * scale),
                x5 = (int)((i + 1.) * scale);
            
            for(int j = 0; j < SIZE * 2; j++){
                int y1 = (int)((j / 2) * scale * SQRT_3_2), y2 = (int)((j / 2 + 1) * scale * SQRT_3_2);

                if(i == mouseX && j == mouseY){ //cell is hovered over
                    if(((int)(time * 10) & 1) == 0){
                        g.setColor(COLORS[this.currColor]);
                        fillTriAt(g,x1,x2,x3,x4,x5,y1,y2,j);
                    }
                }
                if(data[i][j] > 0){   //cell has color
                    g.setColor(COLORS[data[i][j] - 1]);
                    fillTriAt(g,x1,x2,x3,x4,x5,y1,y2,j);
                }
                else if(!keys.isKeyPressed(KeyEvent.VK_V)){ //cell is empty, and user not pressing 'v'
                    g.setColor(Color.GRAY);
                    drawTriAt(g,x1,x2,x3,x4,x5,y1,y2,j);
                }
            }
        }
    }

    private void fillTriAt(Graphics2D g, int x1, int x2, int x3, int x4, int x5, int y1, int y2, int j){
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
    private void drawTriAt(Graphics2D g, int x1, int x2, int x3, int x4, int x5, int y1, int y2, int j){
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
        data[mouseX][mouseY] = currColor + 1;
    }
    
    @Override
    public void onMouseUp(int x, int y) {
        isMouseDown = false;
    }
}