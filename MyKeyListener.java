import java.util.TreeMap;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class MyKeyListener {
    private TreeMap<Integer,Boolean> keys;

    public MyKeyListener(Component c){
        keys = new TreeMap<>();

        c.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e){
                keys.put(e.getKeyCode(),true);
                
            }
            public void keyReleased(KeyEvent e){
                keys.put(e.getKeyCode(),false);
            }
            public void keyTyped(KeyEvent e){}
        });
    }

    public boolean isKeyPressed(int c){
        return keys.containsKey(c) ? keys.get(c) : false;
    }
}
