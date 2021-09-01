import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrianglePaint extends JPanel implements MyMouseListener.MouseObserver, MyKeyListener.KeyObserver{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private enum States{
        IDLE, DRAWING, ERASING
    };
    private States currState = States.IDLE;

    private static double time = 0;

    private static final double SQRT_3_2 = 0.86602540378;
    private static final int SIZE = 64;

    private static final Color[] COLORS = new Color[]{
        new Color(  0,  0,  0),
        new Color( 25, 60,100),
        new Color(100,130,170),
        new Color( 90,  0,  0),
        new Color(222,127,127),
        new Color(135,245, 66),
        new Color( 66,135, 33),
    };

    public MyKeyListener keys;

    private int[][] data = new int[SIZE][SIZE * 2];

    private double scale = 33.3;

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

    public void setKeyListener(MyKeyListener l){
        keys = l;
        l.addObserver(this);
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

                if(i == mouseX && j == mouseY && ((int)(time * 10) & 1) == 0){ //cell is hovered over
                    g.setColor(COLORS[this.currColor]);
                    fillTriAt(g,x1,x2,x3,x4,x5,y1,y2,j);
                }
                else if(data[i][j] > 0){   //cell has color
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


    //#region MouseObserver interface implementations

    @Override
    public void onMouseMove(int x, int y) {
        double Y = x / scale / SQRT_3_2, X = y / scale - 0.5 * (Y % 2) + 1.;
        mouseX = (int)X;
        mouseY = 2 * (int)Y + (int)((X % 1.0) + (Y % 1.0));

        switch(currState){
            case IDLE: break;
            case DRAWING:
                data[mouseX][mouseY] = currColor + 1;
                break;
            case ERASING:
                data[mouseX][mouseY] = 0;
                break;
        }
    }
    @Override
    public void onMouseWheel(int wheelRotation) {
        scale *= 1. - .01 * wheelRotation;
    }

    @Override
    public void onMouseClick(int x, int y, MyMouseListener.Button b) {}

    @Override
    public void onMouseDown(int x, int y, MyMouseListener.Button b) {
        switch(b){
            case LEFT_CLICK:
                currState = States.DRAWING;
                data[mouseX][mouseY] = currColor + 1;
                break;
            case MIDDLE_CLICK: break;
            case RIGHT_CLICK:
                currState = States.ERASING;
                data[mouseX][mouseY] = 0;
                break;
            case NO_CLICK: break;
            
        }
    }
    
    @Override
    public void onMouseUp(int x, int y, MyMouseListener.Button b) {
        currState = States.IDLE;
    }

    //#endregion

    //#region KeyObserver interface implementations

    @Override
    public void onKeyPressed(int keyCode) {
        switch(keyCode){
            case KeyEvent.VK_1:
                currColor = 0; break;
            case KeyEvent.VK_2:
                currColor = 1; break;
            case KeyEvent.VK_3:
                currColor = 2; break;
            case KeyEvent.VK_4:
                currColor = 3; break;
            case KeyEvent.VK_5:
                currColor = 4; break;
            case KeyEvent.VK_6:
                currColor = 5; break;
            case KeyEvent.VK_7:
                currColor = 6; break;
        }
    }

    @Override
    public void onKeyReleased(int keyCode){}
    @Override
    public void onKeyTyped(int keyCode){}

    //#endregion
}