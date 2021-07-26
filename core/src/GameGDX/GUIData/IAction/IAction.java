package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class IAction {
    public String name = "";
    public abstract Action Get();
    public abstract Action Get(IActor iActor);

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
