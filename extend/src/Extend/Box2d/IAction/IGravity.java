package Extend.Box2d.IAction;

import Extend.Box2d.GBox2d;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IGravity extends IAction {

    public Vector2 value = new Vector2(0,-10);
    public IGravity()
    {
        name = "gravity";
    }
    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        GBox2d.world.setGravity(value);
    }
}
