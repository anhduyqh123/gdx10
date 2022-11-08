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
    private JPanel pnInfo;
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

        String[] types = {"Circle","Polygon","Line","Rect","Grid","Path"};
        ui.ComboBox(cbShape,types,iShape.shape.getClass().getSimpleName());
        cbShape.addActionListener(e->{
            int id = cbShape.getSelectedIndex();
            Shape shape = null;
            if (id==0) shape = NewCircle();
            if (id==1) shape = NewRectangle();
            if (id==2) shape = NewLine();
            if (id==3) shape = NewRect();
            if (id==4) shape = NewGrid();
            if (id==5) shape = NewPath();
            iShape.shape = shape;
            MakeNewShape(iShape.shape);
        });
        MakeNewShape(iShape.shape);

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
    private Shape NewGrid()
    {
        Shape.Grid grid = new Shape.Grid();
        grid.pos2.set(w,h);
        return grid;
    }
    private Shape NewRect()
    {
        Shape.Rectangle rect = new Shape.Rectangle();
        rect.pos2.set(w,h);
        return rect;
    }
    private Shape NewLine()
    {
        Shape.Line line = new Shape.Line();
        line.pos2.set(w,h);
        return line;
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
    private Shape NewPath()
    {
        Shape.Path path = new Shape.Path();
        path.points.add(new Vector2(0,0));
        path.points.add(new Vector2(w,h));
        return path;
    }
    private void InitShape(Shape shape)
    {
        if (shape instanceof Shape.Circle){
            display.CircleShape(shape);
            return;
        }
        if (shape instanceof Shape.Line){
            display.LineShape(shape);
            return;
        }
        if (shape instanceof Shape.Path){
            display.ChainShape(shape);
            return;
        }
        display.PolygonShape(shape);
    }
    private void MakeNewShape(Shape shape)
    {
        GDX.PostRunnable(()->InitShape(shape));
        ui.ClearPanel(pnInfo);
        ui.InitComponents(shape,pnInfo);
    }
}
