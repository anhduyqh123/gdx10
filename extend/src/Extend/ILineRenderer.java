package Extend;

import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.Scene;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class ILineRenderer extends Component {
    public String p1="p1",p2="p2",line = "line";

    @Override
    protected void Update(float delta) {
        try {
            Update();
        }catch (Exception e){}
    }
    private void Update()
    {
        Vector2 pos1 = Scene.GetPosition(FindIChild(p1).GetActor(),Align.center);
        Vector2 pos2 = Scene.GetPosition(FindIChild(p2).GetActor(),Align.center);
        IActor iLine = FindIChild(line);
        Vector2 dir = Util.GetDirect(pos1,pos2);
        iLine.iSize.width = dir.len();
        iLine.iSize.rotate = dir.angleDeg();
        iLine.iPos.Set(pos1, IAlign.left);
        iLine.Runnable(a->{
            a.RefreshSize();
            a.RefreshPosition();
        });
    }
//    private void Update()
//    {
//        Vector2 pos1 = Scene.GetPosition(FindIChild(p1).GetActor(),Align.center);
//        Vector2 pos2 = Scene.GetPosition(FindIChild(p2).GetActor(),Align.center);
//        Actor aline = FindIChild(line).GetActor();
//        Vector2 dir = Util.GetDirect(pos1,pos2);
//        aline.setWidth(dir.len());
//        aline.setPosition(pos1.x,pos1.y, Align.left);
//        aline.setRotation(dir.angleDeg());
//    }
}
