package Editor.UITool.Pointed;

import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class CirclePointed extends Pointed {
    private Object circle;
    public CirclePointed(Object circle, Group group) {
        super(group);
        this.circle = circle;
        Vector2 pos = Reflect.GetValue("pos",circle);
        float radius = Reflect.GetValue("radius",circle);

        Vector2 p = new Vector2(pos).add(radius,0);
        Image img = NewPoint(p, x->{
            p.set(x);
            Reflect.SetValue("radius",circle,Util.GetDistance(p,pos));
            //iShape.radius = Util.GetDistance(p,pos);
            SetRender();
        });
        NewPoint(pos,x->{
            pos.set(x);
            //iShape.pos.set(x);
            float r = Reflect.GetValue("radius",circle);
            p.set(x.x+r,x.y);
            img.setPosition(p.x,p.y, Align.center);
            SetRender();
        });
        SetRender();
    }
    private void SetRender()
    {
        renderer.Clear();
        Vector2 pos = Reflect.GetValue("pos",circle);
        float radius = Reflect.GetValue("radius",circle);
        renderer.NewCircle(GetPos(pos),radius);
    }
}
