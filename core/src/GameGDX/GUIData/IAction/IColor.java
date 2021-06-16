package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IColor extends IBaseAction{

    public String hexColor = Color.WHITE.toString();

    public IColor()
    {
        name = "color";
    }
    private Color GetColor()
    {
        return Color.valueOf(hexColor);
    }
    public Action Get(Color color) {
        return Actions.color(color,duration, iInter.value);
    }
    @Override
    public Action Get() {
        return Get(GetColor());
    }

    @Override
    public Action Get(IActor iActor) {
        if (relocation) return Get(iActor.GetActor().getColor());
        return Get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IColor)) return false;
        if (!super.equals(o)) return false;
        IColor iColor = (IColor) o;
        return hexColor.equals(iColor.hexColor);
    }
}
