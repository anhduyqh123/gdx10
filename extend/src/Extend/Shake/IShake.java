package Extend.Shake;

import GameGDX.GUIData.IAction.IDelay;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;

public class IShake extends IDelay {
    public int offsetX=3,offsetY=3;

    public IShake()
    {
        name = "shake";
    }

    @Override
    public Action Get(IActor iActor) {
        return GShake.Get(offsetX,offsetY,duration);
    }
}
