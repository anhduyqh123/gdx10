package Editor.UITool.Pointed;

import Extend.Box2d.IShape;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class CirclePointed extends Pointed {

    private IShape.ICircle iShape;
    public CirclePointed(IShape.ICircle iShape, Group group) {
        super(group);
        this.iShape = iShape;

        Vector2 p = new Vector2(iShape.pos).add(iShape.radius,0);
        Image img = NewPoint(p, x->{
            p.set(x);
            iShape.radius = Util.GetDistance(p,iShape.pos);
            SetRender();
        });
        NewPoint(iShape.pos,x->{
            iShape.pos.set(x);
            p.set(x.x+iShape.radius,x.y);
            img.setPosition(p.x,p.y, Align.center);
            SetRender();
        });
        SetRender();
    }
    private void SetRender()
    {
        renderer.Clear();
        renderer.NewCircle(GetPos(iShape.pos),iShape.radius);
    }
}
