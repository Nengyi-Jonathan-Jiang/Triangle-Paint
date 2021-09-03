import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrianglePaint extends JPanel implements MyMouseListener.MouseObserver, MyKeyListener.KeyObserver{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private enum States{IDLE, DRAWING, ERASING, DRAW_BLENDED_EDGE}
    private States currState = States.IDLE;

    private double time = 0;

    private static final double SQRT_3_2 = 0.86602540378;
    private static final int SIZE = 64;

    private static final Color[] COLORS = new Color[]{
        new Color( 30, 30, 30),
        new Color(130,130,130),
        new Color(230,230,230),
        new Color( 25, 60,100),
        new Color(100,130,170),
        new Color( 90,  0,  0),
        new Color(222,127,127),
        new Color(175,227,141),
        new Color( 66,135, 33),
    };

    public MyKeyListener keys;

    private int[][] triangleData = new int[SIZE][SIZE * 2];
    private boolean[][] edgeData = new boolean[SIZE][SIZE * 3];

    private boolean showGrid = true;

    private double scale = 33.3;

    private int lastMouseX = -1, lastMouseY = -1, mouseX, mouseY;
    private int currColor;

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
        keys = l; l.addObserver(this);
    }

    @Override
    public void paint(Graphics window) {
        super.paint(window);
        Graphics2D g = (Graphics2D)window;
        g.setStroke(new BasicStroke(1));

        boolean blinking = ((int)(time * 15) & 3) != 0 && showGrid;

        if(showGrid){
            g.setColor(new Color(200,200,200));
            for(int i = 0; i <= SIZE; i++){
                g.drawLine((int)(i * scale * SQRT_3_2), 0, (int)(i * scale * SQRT_3_2), (int)(SIZE * scale));
            }
            for(int i = 0; i <= SIZE * 3 / 2; i++){
                g.drawLine(0, (int)(i * scale), (int)(SIZE * scale * SQRT_3_2), (int)((i - SIZE / 2) * scale));
                g.drawLine(0, (int)((i - SIZE / 2) * scale), (int)(SIZE * scale * SQRT_3_2), (int)(i * scale));
            }
            g.setColor(Color.WHITE);
            g.fillRect(0,(int)(SIZE * scale),(int)(SIZE * scale * SQRT_3_2),(int)(SIZE / 2 * scale));
            for(int i = 0; i < SIZE; i += 2){
                g.fillPolygon(new int[]{
                    (int)((i) * scale * SQRT_3_2) + 1,
                    (int)((i) * scale * SQRT_3_2) + 1,
                    (int)((i + 2) * scale * SQRT_3_2) - 1,
                }, new int[]{
                    (int)((SIZE - 1) * scale) + 1,
                    (int)(SIZE * scale) + 1,
                    (int)(SIZE * scale) + 1,
                }, 3);
            }
        }

        //Draw filled triangles (or triangle outlines)
        for(int i = 0; i < SIZE; i++){
            int x1 = (int)((i - 1.) * scale), x2 = (int)((i - .5) * scale),
                x3 = (int)((i + 0.) * scale), x4 = (int)((i + .5) * scale),
                x5 = (int)((i + 1.) * scale);
            for(int j = 0; j < SIZE * 2; j++){
                int y1 = (int)((j / 2) * scale * SQRT_3_2), y2 = (int)((j / 2 + 1) * scale * SQRT_3_2);

                if(i == mouseX && j == mouseY && blinking){ //cell is hovered over
                    if(blinking && currState != States.ERASING){
                        g.setColor(COLORS[this.currColor]);
                        fillTriAt(g,x1,x2,x3,x4,x5,y1,y2,j);
                    }
                }
                else if(triangleData[i][j] > 0){   //cell has color
                    g.setColor(COLORS[triangleData[i][j] - 1]);
                    fillTriAt(g,x1,x2,x3,x4,x5,y1,y2,j);
                }
            }
        }

        //Draw edges
        g.setColor(Color.BLACK);
        for(int i = 0; i < SIZE; i++){
            int x1 = (int)((i - 1.) * scale), x2 = (int)((i - .5) * scale),
                x3 = (int)((i + 0.) * scale), x4 = (int)((i + .5) * scale);
            for(int j = 0; j < SIZE * 3; j++){
                int y1 = (int)((j / 3) * scale * SQRT_3_2), y2 = (int)((j / 3 + 1) * scale * SQRT_3_2);
                if(edgeData[i][j]) drawEdge(g,x1,x2,x3,x4,y1,y2,j);
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

    public static class Edge{
        public int x1,y1,x2,y2,ex,ey;
        public Edge(int x1,int y1,int x2,int y2,int ex,int ey){
            this.x1 = x1; this.x2 = x2;
            this.y1 = y1; this.y2 = y2;
            this.ex = ex; this.ey = ey;
        }
    }
    public Edge[] getAdjacentEdges(int x, int y){
        int y_ = (y >> 2) * 6;
        switch(y & 3){
            case 0:
                return new Edge[]{
                    new Edge(x,y,x - 1, y + 1, x + 0, y_ + 1),
                    new Edge(x,y,x - 1, y - 1, x + 0, y_ + 0),
                    new Edge(x,y,x + 0, y + 1, x + 0, y_ + 2),
                };
            case 1:
                return new Edge[]{
                    new Edge(x,y,x + 0, y + 1, x + 0, y_ + 3),
                    new Edge(x,y,x + 0, y - 1, x + 0, y_ + 2),
                    new Edge(x,y,x + 1, y - 1, x + 1, y_ + 1),
                };
            case 2:
                return new Edge[]{
                    new Edge(x,y,x + 0, y - 1, x + 0, y_ + 3),
                    new Edge(x,y,x - 1, y + 1, x + 0, y_ + 4),
                    new Edge(x,y,x + 0, y + 1, x + 0, y_ + 5),
                };
            case 3:
                return new Edge[]{
                    new Edge(x,y,x + 1, y - 1, x + 1, y_ + 4),
                    new Edge(x,y,x + 0, y - 1, x + 0, y_ + 5),
                    new Edge(x,y,x + 1, y + 1, x + 1, y_ + 6),
                };
            default: return new Edge[]{};
        }
    }
    public void drawEdge(Graphics2D g, int x1, int x2, int x3, int x4, int y1, int y2, int j){
        switch(j % 6){
            case 0:
                g.drawLine(y1,x1,y1,x3);
                break;
            case 1:
                g.drawLine(y1,x1,y2,x2);
                break;
            case 2:
                g.drawLine(y2,x2,y1,x3);
                break;
            case 3:
                g.drawLine(y1,x2,y1,x4);
                break;
            case 4:
                g.drawLine(y1,x2,y2,x3);
                break;
            case 5:
                g.drawLine(y2,x3,y1,x4);
                break;
            default: break;
        }
    }

    //#region MouseObserver interface implementations

    @Override
    public void onMouseMove(int x, int y) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        double Y = x / scale / SQRT_3_2, X = y / scale - 0.5 * (Y % 2) + 1;
        mouseX = (int)X;
        mouseY = 2 * (int)Y + ((X % 1.0) + (Y % 1.0) >= 1 ? 1 : 0);

        switch(currState){
            case IDLE: break;
            case DRAW_BLENDED_EDGE:
                drawBlendedEdge();
                if(!keys.isKeyPressed(KeyEvent.VK_CONTROL)) currState = States.DRAWING;
                break;
            case DRAWING:
                drawNormal();
                if(keys.isKeyPressed(KeyEvent.VK_CONTROL)) currState = States.DRAW_BLENDED_EDGE;
                break;
            case ERASING:
                erase();
                break;
        }
    }

    //#region drawing functions
    private void drawBlendedEdge(){
        triangleData[mouseX][mouseY] = currColor + 1;
        for(Edge e : getAdjacentEdges(mouseX, mouseY)){
            edgeData[e.ex][e.ey] = triangleData[e.x2][e.y2] != currColor + 1;
        }
    }
    private void drawNormal(){
        triangleData[mouseX][mouseY] = currColor + 1;
        if(mouseX != lastMouseX || mouseY != lastMouseY){
            for(Edge e : getAdjacentEdges(mouseX, mouseY)){
                if(e.x2 == lastMouseX && e.y2 == lastMouseY){
                    edgeData[e.ex][e.ey] = false;
                }
                else{
                    edgeData[e.ex][e.ey] = true;
                }
            }
        }
    }

    private void erase(){
        triangleData[mouseX][mouseY] = 0;
        for(Edge e : getAdjacentEdges(mouseX, mouseY)){
            if(triangleData[e.x1][e.y1] + triangleData[e.x2][e.y2] != 0){
                edgeData[e.ex][e.ey] = true;
            }
            else{
                edgeData[e.ex][e.ey] = false;
            }
        }
    }
    //#endregion

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
                currState = States.DRAW_BLENDED_EDGE;
                onMouseMove(x,y);
                break;
            case MIDDLE_CLICK: break;
            case RIGHT_CLICK:
                currState = States.ERASING;
                onMouseMove(x,y);
                break;
            case NO_CLICK: break;
        }
    }
    
    @Override
    public void onMouseUp(int x, int y, MyMouseListener.Button b) {
        currState = States.IDLE;
        lastMouseX = lastMouseY = -1;
    }

    //#endregion

    //#region KeyObserver interface implementations

    @Override
    public void onKeyPressed(int keyCode) {
        switch(keyCode){
            case KeyEvent.VK_EQUALS:
                scale *= 1.01;
                break;
            case KeyEvent.VK_MINUS:
                scale *= 0.99;
                break;
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
            case KeyEvent.VK_8:
                currColor = 7; break;
            case KeyEvent.VK_9:
                currColor = 8; break;
            
            case KeyEvent.VK_R:
                for(boolean[] i : edgeData) for(int j = 0; j < i.length; j++) i[j] = false;
                for(int[] i : triangleData) for(int j = 0; j < i.length; j++) i[j] = 0;
                break;
            case KeyEvent.VK_V:
                showGrid = !showGrid; break;
            
            default:break;
        }
    }

    @Override
    public void onKeyReleased(int keyCode){}
    @Override
    public void onKeyTyped(int keyCode){}

    //#endregion
}