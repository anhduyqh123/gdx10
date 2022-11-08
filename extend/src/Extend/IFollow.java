package Extend;

import GameGDX.GDX;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IFollow extends IAction {
    private String target = "";//name of actor
    private IAlign targetAlign = IAlign.center, align = IAlign.center;

    private boolean usedPos,useRotate;
    private GDX.Func<Vector2> getDir;

    public IFollow()
    {
        name = "follow";
    }

    @Override
    protected void Init(IActor iActor) {
        getDir = null;
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        try {
            if (usedPos) UpdatePosition(iActor);
        }catch (Exception e){}
    }
    private void InitDirect(IActor iActor)
    {
        Vector2 p0 = iActor.GetIActor(target).GetStagePos(targetAlign.value);
        Vector2 p1 = iActor.GetStagePos(align.value);
        Vector2 dir = Util.GetDirect(p0,p1);
        getDir = ()->dir;
    }
    private void UpdatePosition(IActor iActor)
    {
        if (getDir==null) InitDirect(iActor);
        Vector2 p0 = iActor.GetIActor(target).GetStagePos(targetAlign.value);
        iActor.SetStagePos(p0.add(getDir.Run()), align.value);
    }
}
