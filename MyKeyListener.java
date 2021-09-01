import java.util.*;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class MyKeyListener {
    private int i;
    private TreeMap<Integer,Boolean> keys;
    private TreeMap<Integer,KeyObserver> observers;

    public static interface KeyObserver{
        public void onKeyPressed(int keyCode);
        public void onKeyReleased(int keyCode);
        public void onKeyTyped(int keyCode);
    }

    public MyKeyListener(Component c){
        keys = new TreeMap<>();
        observers = new TreeMap<Integer,KeyObserver>();

        c.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e){
                keys.put(e.getKeyCode(),true);
                for(KeyObserver o : observers.values()){
                    o.onKeyPressed(e.getKeyCode());
                }
            }
            public void keyReleased(KeyEvent e){
                keys.put(e.getKeyCode(),false);
                for(KeyObserver o : observers.values()){
                    o.onKeyReleased(e.getKeyCode());
                }
            }
            public void keyTyped(KeyEvent e){
                for(KeyObserver o : observers.values()){
                    o.onKeyTyped(e.getKeyCode());
                }
            }
        });
    }

    public int addObserver(KeyObserver o){
        observers.put(i,o);
        return i++;
    }

    public boolean isKeyPressed(int c){
        return keys.containsKey(c) ? keys.get(c) : false;
    }
}
