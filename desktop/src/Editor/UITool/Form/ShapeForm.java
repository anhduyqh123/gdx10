package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Pointed.Display;
import Editor.UITool.Pointed.Pointed;
import Extend.GShape.IShape;
import Extend.GShape.Shape;
import GameGDX.GDX;
import com.badlogic.gdx.math.Vector2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ShapeForm {
    private JPanel panel1;
    private JComboBox cbShape;
    private JTextField tfSize;
    private JButton btFreeze;
    private JFameUI ui = new JFameUI();
    private Display display;
    private float w,h;

    public ShapeForm(IShape iShape)
    {
        display = new Display(iShape.GetIActor());
        JFrame jFrame = ui.NewJFrame("Shape",panel1,()->display.Close());
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        w = iShape.GetActor().getWidth();
        h = iShape.GetActor().getHeight();

        String[] types = {"Circle","Polygon"};
        ui.ComboBox(cbShape,types,iShape.shape.getClass().getSimpleName());
        cbShape.addActionListener(e->{
            int id = cbShape.getSelectedIndex();
            Shape shape = null;
            if (id==0) shape = NewCircle();
            if (id==1) shape = NewRectangle();
            iShape.shape = shape;
            GDX.PostRunnable(()->InitShape(iShape.shape));
        });
        GDX.PostRunnable(()->InitShape(iShape.shape));

        tfSize.setText(Pointed.size+"");
        tfSize.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    float size = Float.parseFloat(tfSize.getText());
                    display.Resize(size);
                    //shape.Resize(size);
                }
            }
        });
    }
    private Shape NewCircle()
    {
        Shape.Circle circle = new Shape.Circle();
        circle.pos.set(w/2,h/2);
        circle.radius = w/2;
        return circle;
    }
    private Shape NewRectangle()
    {
        Shape.Polygon polygon = new Shape.Polygon();
        polygon.points.add(new Vector2());
        polygon.points.add(new Vector2(w,0));
        polygon.points.add(new Vector2(w,h));
        polygon.points.add(new Vector2(0,h));
        polygon.points.add(new Vector2());
        return polygon;
    }
    private void InitShape(Shape shape)
    {
        if (shape instanceof Shape.Circle) display.CircleShape(shape);
        else display.PolygonShape(shape);
    }
}
