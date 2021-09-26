package Extend;

import GameGDX.GUIData.IChild.Component;
import GameGDX.Scene;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class ILineRenderer extends Component {
    @Override
    protected void Update(float delta) {
        try {
            Update();
        }catch (Exception e){}
    }
    private void Update()
    {
        Vector2 p1 = Scene.GetPosition(FindIChild("p1").GetActor(),Align.center);
        Vector2 p2 = Scene.GetPosition(FindIChild("p2").GetActor(),Align.center);
        Actor line = FindIChild("line").GetActor();
        Vector2 dir = Util.GetDirect(p1,p2);
        line.setWidth(dir.len());
        line.setPosition(p1.x,p1.y, Align.left);
        line.setRotation(dir.angleDeg());
    }
}
