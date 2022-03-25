package Extend.GShape;

import GameGDX.GUIData.IChild.Component;
import com.badlogic.gdx.math.Vector2;

public class IShape extends Component {
    public Shape shape = new Shape.Circle();
    public IShape(){}
    @Override
    public void New() {
        float w = GetActor().getWidth(),h = GetActor().getHeight();
        Shape.Polygon shape = new Shape.Polygon();
        shape.points.add(new Vector2());
        shape.points.add(new Vector2(w,0));
        shape.points.add(new Vector2(w,h));
        shape.points.add(new Vector2(0,h));
        shape.points.add(new Vector2());
        this.shape = shape;
    }
}
