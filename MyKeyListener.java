import java.util.*;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class MyKeyListener {
    private TreeMap<Integer,Boolean> keys;
    private TreeMap<Integer,ArrayList<Callback>> key_callbacks;

    public static interface Callback{
        public void onKeyPressed();
    }

    public MyKeyListener(Component c){
        keys = new TreeMap<>();

        c.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e){
                keys.put(e.getKeyCode(),true);
            }
            public void keyReleased(KeyEvent e){
                keys.put(e.getKeyCode(),false);
            }
            public void keyTyped(KeyEvent e){
                for(Callback f : key_callbacks.get(e.getKeyCode())){
                    f.onKeyPressed();
                }
            }
        });
    }

    public void addKeyCallback(int keyCode, Callback f){
        key_callbacks.get(keyCode).add(f);
    }

    public boolean isKeyPressed(int c){
        return keys.containsKey(c) ? keys.get(c) : false;
    }
}
