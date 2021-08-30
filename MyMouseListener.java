import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MyMouseListener {
    public static interface MouseObserver{
        public void onMouseClick(int x, int y);
        public void onMouseWheel(int wheelRotation);
        public void onMouseMove(int x, int y);
        public void onMouseDown(int x, int y);
        public void onMouseUp(int x, int y);
    }

    public MyMouseListener(Component c, MouseObserver m){
        c.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event) {
                m.onMouseClick(event.getX(), event.getY());
                System.out.println("Detected event: Mouse clicked");
            }
            @Override
            public void mouseMoved(MouseEvent event){
                m.onMouseMove(event.getX(), event.getY());
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
            public void mouseWheelMoved(MouseWheelEvent event){
                System.out.println("Detected event: Mouse wheel moved");
                m.onMouseWheel(event.getWheelRotation());
            }
        });
    }
}