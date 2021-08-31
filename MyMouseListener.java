import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

public class MyMouseListener {
    public static interface MouseObserver{
        public void onMouseClick(int x, int y);
        public void onMouseWheel(int wheelRotation);
        public void onMouseMove(int x, int y);
        public void onMouseDown(int x, int y);
        public void onMouseUp(int x, int y);
    }

    public MyMouseListener(Component c, MouseObserver m){
        c.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent event) {
                m.onMouseClick(event.getX(), event.getY());
            }
            @Override
            public void mousePressed(MouseEvent event){
                m.onMouseDown(event.getX(), event.getY());
            }
            @Override
            public void mouseReleased(MouseEvent event){
                m.onMouseUp(event.getX(), event.getY());
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