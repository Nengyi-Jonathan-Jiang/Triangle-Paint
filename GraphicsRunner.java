import javax.swing.JFrame;

public class GraphicsRunner extends JFrame {

    public GraphicsRunner() {
        super("Triangle Paint");

        setSize(TrianglePaint.WIDTH,TrianglePaint.HEIGHT);

        TrianglePaint c = new TrianglePaint();
        c.setKeyListener(new MyKeyListener(this));
        new MyMouseListener(c,c);
        getContentPane().add(c);

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}