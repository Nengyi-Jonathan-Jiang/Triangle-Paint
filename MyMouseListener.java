import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

public class MyMouseListener {
    public enum Button{
        LEFT_CLICK,
        RIGHT_CLICK,
        MIDDLE_CLICK,
        NO_CLICK
    }
    private Button getButton(MouseEvent event){
        int b = event.getButton();
        return b == 1 ? Button.LEFT_CLICK : b == 2 ? Button.MIDDLE_CLICK : b == 3 ? Button.RIGHT_CLICK : Button.NO_CLICK;
    }

    public static interface MouseObserver{
        public void onMouseClick(int x, int y, Button b);
        public void onMouseWheel(int wheelRotation);
        public void onMouseMove(int x, int y);
        public void onMouseDown(int x, int y, Button b);
        public void onMouseUp(int x, int y, Button b);
    }

    public MyMouseListener(Component c, MouseObserver m){
        c.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent event) {
                m.onMouseClick(event.getX(), event.getY(), getButton(event));
            }
            @Override
            public void mousePressed(MouseEvent event){
                m.onMouseDown(event.getX(), event.getY(), getButton(event));
            }
            @Override
            public void mouseReleased(MouseEvent event){
                m.onMouseUp(event.getX(), event.getY(), getButton(event));
            }

            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        c.addMouseMotionListener(new MouseMotionListener(){
            
            @Override
            public void mouseMoved(MouseEvent event){
                m.onMouseMove(event.getX(), event.getY());
            }
            @Override
            public void mouseDragged(MouseEvent event){
                m.onMouseMove(event.getX(), event.getY());
            }
        });
        c.addMouseWheelListener(new MouseWheelListener(){
            @Override
            public void mouseWheelMoved(MouseWheelEvent event){
                m.onMouseWheel(event.getWheelRotation());
            }
        });
    }
}