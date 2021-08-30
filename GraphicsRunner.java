import javax.swing.JFrame;

public class GraphicsRunner extends JFrame {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;

    public GraphicsRunner() {
        super("Triangle Paint");

        setSize(WIDTH, HEIGHT);

        TrianglePaint c = new TrianglePaint();
        c.keys = new MyKeyListener(this);
        new MyMouseListener(this,c);
        getContentPane().add(c);

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}