import javax.swing.JFrame;

public class GraphicsRunner extends JFrame {

    public GraphicsRunner() {
        super("Triangle Paint");

        setSize(TrianglePaint.WIDTH,TrianglePaint.HEIGHT);

        TrianglePaint c = new TrianglePaint();
        c.keys = new MyKeyListener(this);
        //new MyMouseListener(this.getContentPane(),c);
        new MyMouseListener(c,c);
        getContentPane().add(c);

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}